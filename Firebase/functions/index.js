const functions = require("firebase-functions");

const admin = require('firebase-admin');
const { auth } = require("firebase-admin/auth");
const { user } = require("firebase-functions/v1/auth");
const { error } = require("firebase-functions/logger");

const serviceAccount = require('./animengine-fb858-firebase-adminsdk-1l8jr-9df9aa102d.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://animengine-fb858-default-rtdb.firebaseio.com/",
    storageBucket: "animengine-fb858.appspot.com"
});

const url = require('url');
const { request } = require("http");

const db = admin.database();
const creatorRef = db.ref("/Users/Creator");
const fanRef = db.ref("/Users/Fan");

const animeRef = db.ref("/Anime");

const storage = admin.storage().bucket();

function containsObject(obj, list) {
    var i;
    for (i = 0; i < list.length; i++) {
        if (list[i] === obj) {
            return true;
        }
    }

    return false;
}

function determineRef(type){
    if(type == "fan")
        return fanRef;
    else
        return creatorRef;
}

const errorCatcher = (error) => {
    response.json({"data":{"error":`${error}`}});
    return;
};

async function getUIDUsingToken(token, response){
    var uid;
    await admin.auth().verifyIdToken(token)
        .then((decodedToken) => {
            uid = decodedToken.uid;
        })
        .catch((error) => {throw error});

    return uid;
}

exports.register = functions.https.onRequest(async (request, response) => {
    // Grab the request body as JSON and parse it into a JS object.
    // console.log(request.body);

    const json = request.body["data"];
    userObj = JSON.parse(json);

    // Extract variables
    const email = userObj["Email"];
    const password = userObj["Password"];

    const userType = userObj["Type"];
    currentRef = determineRef(userType);
    

    delete userObj["Email"];
    delete userObj["Password"];
    delete userObj["Type"];

    // Check if creator already exists, returns error message if so.
    const emailExists = await admin.auth().getUserByEmail(email).then(() => true).catch(() => false);
    if(emailExists){
        response.json({"data":{"error":`${userType} email already exists`}});
        return;
    }
    else{
        await admin.auth().createUser({
            email: email,
            password: password,
        })
        .then((userRecord) => {
            const uid = userRecord.uid;
            currentRef.child(uid).set(userObj).then((obj) => {
                response.json({"data":{"ok":`Successfully created new ${userType} user: ${email}`}});
                return;
            }).catch(errorCatcher);
        })
        .catch(errorCatcher);
    }
});

exports.login = functions.https.onRequest(async (request, response) => {
    
    // Grab the request body as JSON and parse it into a JS object.
    const json = request.body["data"];
    console.log(json);

    const inputObj = JSON.parse(json);
    console.log(inputObj);
    
    // Extract variables
    const token = inputObj["Token"];
    console.log(token);
    /*
    {
        "data":{
            "Token":"dadlk;fjdfklgjsl;gjsgsl;"
        }
    }
    */
   var uid = await getUIDUsingToken(token).catch(errorCatcher);
    // var uid;
    // await admin.auth().verifyIdToken(token)
    //     .then((decodedToken) => {
    //         uid = decodedToken.uid;
    //     })
    //     .catch(errorCatcher);
    
    var creatorJson;
    var fanJson;

    await creatorRef.child(uid).get()
        .then((dataSnapshot) => {creatorJson=dataSnapshot.toJSON()})
        .catch(errorCatcher);
        
    await fanRef.child(uid).get()
        .then((dataSnapshot) => {fanJson=dataSnapshot.toJSON()})
        .catch(errorCatcher);
    
    response.json({"data":{"ok":{"creator":`${JSON.stringify(creatorJson)}`, "fan":`${JSON.stringify(fanJson)}`}}});
    return;
});

exports.forgot = functions.https.onRequest(async (request, response) => {
    // Grab the request body as JSON and parse it into a JS object.
    const json = request.body["data"];
    console.log(json);

    const inputObj = JSON.parse(json);
    console.log(inputObj);
    
    // Extract variables
    const email = inputObj["Email"];
    console.log(email);

    const password = inputObj["Password"];
    console.log(password);
    /*
    {
        "data":{
            "Token":"dadlk;fjdfklgjsl;gjsgsl;"
            "Email":"a@gmail.com"
            "Password":"555555"
        }
    }
    */
    
    await admin.auth().getUserByEmail(email)
        .then((user) =>{
            admin.auth().updateUser(user.uid, {email:`${email}`, password:`${password}`})
                .then((userRecord)=>{
                    response.json({"data":{"ok":"Password updated successfuly"}});
                    return;
                })
                .catch(errorCatcher);

            console.log({"email": `${email}`, "password":`${password}`});
        })
        .catch(errorCatcher);

});

exports.upload = functions.https.onRequest(async (request, response) => {
    const json = request.body["data"];
    
    console.log(json);
    const inputObj = JSON.parse(json);

    const animeObj = inputObj["anime"];

    //create variable which contains the data from the body request
    const token = inputObj["token"];

    var uid = await getUIDUsingToken(token).catch(errorCatcher);

    const animeName = animeObj["name"];
    console.log(animeName);

    const animeDescription = animeObj["description"];
    console.log(animeDescription);

    const animeImage = animeObj["imageURL"];

    const animeGenres = animeObj["genres"];
    console.log(animeGenres);
    
    var imageURL;
    // Step 1. Create reference for file name in cloud storage 
    const imageBuffer = Buffer.from(animeImage, 'base64');

    await storage.file(`images/${animeName}.jpg`)
    .save(imageBuffer, {
        metadata: {
          contentType: 'image/jpeg'
        }
      }).then((obj) => {
        console.log(`obj: ${obj}`);
        
      }).catch(errorCatcher);

    var uploadedFile = storage.file(`images/${animeName}.jpg`);
    const signedUrl = await uploadedFile.getSignedUrl({action: 'read',expires: new Date().getTime() + (60 * 60 * 1000)});

    const parsedUrl = new url.URL(signedUrl);
    parsedUrl.search = '';

    const strippedURL = parsedUrl.toString();

    delete animeObj["name"];
    animeObj["imageURL"]=strippedURL;
    animeObj["ownerUID"]=uid;
    
    await animeRef.child(animeName).set(animeObj)
        .then((obj) => {
            response.json({"data":{"ok":"Anime uploaded successfully!"}});
            return;
        })
        .catch(errorCatcher);

    /*
    {
        "data":{
            "ownerUID": String
            "name":"dadlk;fjdfklgjsl;gjsgsl;"
            "description":"a@gmail.com"
            "imageURL": image //to delete after extracting information
            //add "imageURL": String
            "genres":{
                "action": float
                "drama": float
                ...
            }
        }
    }
    */


});

exports.editUser = functions.https.onRequest(async (request, response) => {
    /*
        {
            "data":{
                "Type": "fan"/"creator"
                "Token":"asfnsdklfhawl1231434sdfv"
                "User":{
                    if fan
                        "fName":"nezer"
                        "lName":"ben moshe"
                        "genres": {"genre":float,
                                    ....}
                    if creator
                        "studioName":"Sony"
                        "webAddress":"www.google.com"
                }
            }
        }
    */

    const json = request.body["data"];
    userObj = JSON.parse(json);

    const type = userObj["Type"];

    const token = userObj["Token"];
    console.log(token);


    const user = userObj["User"];

    var uid = await getUIDUsingToken(token).catch(errorCatcher);

    
    var currentRef=determineRef(type);

    await currentRef.child(uid).set(user)
        .then((completed) => {
            response.json({"data":{"ok":"User updated successfully!"}});
            return;
        })
        .catch(errorCatcher);
});

exports.editGenres = functions.https.onRequest(async (request, response) => {
    const json = request.body["data"];
    userObj = JSON.parse(json);

    const type = userObj["Type"];

    const token = inputObj["Token"];
    console.log(token);


    const user = userObj["User"];
    console.log(user);
    const genres = user["genres"];
    console.log(genres);

    var uid = await getUIDUsingToken(token).catch(errorCatcher);

    
    var currentRef=determineRef(type);

    await currentRef.child(uid).child("genres").set(genres)
        .then((completed) => {
            response.json({"data":{"ok":"User genres updated successfully!"}});
            return;
        })
        .catch(errorCatcher);
});

exports.getAnime = functions.https.onRequest(async (request, response) => {
    /*
    {
        "data":{
            "anime":[
                "Another",
                "Golden time",
                "Attack On Titan",
                ...
            ]
        }
    }

    return
    "data":{
            "ok":[
                {},
                {},
                {},
                ...
            ]
        }

    */

    const json = request.body["data"];
    var animeObj = JSON.parse(json);
    var animeArr = animeObj["anime"];  
    
    var animeMap = new Map();

    animeArr.forEach((element) => {
        animeRef.child(element).get().then((dataSnapshot) => {
            animeMap.set(element ,dataSnapshot.val())
            console.log("data",dataSnapshot)
        }).catch(errorCatcher)
        
    });

    response.json({"data":{"ok":JSON.stringify(animeMap)}});
    return;
});

exports.getAllAnime = functions.https.onRequest(async (request, response) => {
    // Retrieve the contents of the folder
    const objects = [];
    await animeRef.once('value').then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
            var object = childSnapshot.val();
            object.name=childSnapshot.key;

            objects.push(object);
        });
    }).catch(errorCatcher);

    response.json({"data":{"ok":JSON.stringify(objects)}});
    return;
    
}); 

exports.getBestKAnime = functions.https.onRequest(async (request, response) => {
    const json = request.body["data"];
    inputObj = JSON.parse(json);

    const token = inputObj["Token"];
    const type = inputObj["Type"];

    console.log(token);

    var uid = await getUIDUsingToken(token).catch(errorCatcher);

    var currentRef = determineRef(type);

    const userObj = await currentRef.child(uid).get()
    const blackListedAnimes = userObj["blacklist"];
    const userGenres = userObj["genres"];

    const objects = [];
    await animeRef.once('value').then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
            var object = childSnapshot.val();
            object.name=childSnapshot.key;
            
            var animeObj = {
                name: object.name,
                genres: object.genres
            };
            if(!containsObject(object.name, blackListedAnimes))
                objects.push(animeObj);
        });
    }).catch(errorCatcher);

    const calcDistance = (animeObj) => {
        var sum = 0;
        userGenres.forEach((key) => {
            sum += Math.abs(userGenres[key] - animeObj.genres[key])
        });

        return sum;
    };

    objects.sort((a, b) => {return calcDistance(a) - calcDistance(b);})
    const animeNames = objects.map(x => x.name);

    response.json({"data":{"ok":JSON.stringify(animeNames)}});
    return;
    // const user = userObj["User"];
    // const genres = user["genres"];
    
    // const arrAnime = []
    // await animeRef.once('value').then((snapshot) => {
    //     snapshot.forEach((childSnapshot) => {
    //         arrAnime.push(childSnapshot.val());
    //     });
    // });

    // var countK = 0;
    // var sumDistances = 0;
    // const dictValuAnime = {}; 
    // arrAnime.forEach((animeObj) => {
    //     const anime = animeRef[animeObj];
    //     sumDistances+= abs(anime[] - genres[]) ;

    // });
});
  


