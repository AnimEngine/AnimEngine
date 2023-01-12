const functions = require("firebase-functions");

const admin = require('firebase-admin');
const { auth } = require("firebase-admin/auth");
const { user } = require("firebase-functions/v1/auth");
const { error } = require("firebase-functions/logger");

const serviceAccount = require('./animengine-fb858-firebase-adminsdk-1l8jr-a00cb09e5c.json');
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
const commentRef = db.ref("/Comments")

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
            }).catch(error => {
                response.json({"data":{"error":`${error}`}});
                return;
            });
        })
        .catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
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
   var uid = await getUIDUsingToken(token).catch(error => {
            response.json({"data":{"error":`${error}`}});
                return;
            });
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
        .catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
        
    await fanRef.child(uid).get()
        .then((dataSnapshot) => {fanJson=dataSnapshot.toJSON()})
        .catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
    
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
                .catch(error => {
                    response.json({"data":{"error":`${error}`}});
                    return;
                });

            console.log({"email": `${email}`, "password":`${password}`});
        })
        .catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });

});

exports.upload = functions.https.onRequest(async (request, response) => {
    const json = request.body["data"];
    
    console.log(json);
    const inputObj = JSON.parse(json);

    const animeObj = inputObj["anime"];

    //create variable which contains the data from the body request
    const token = inputObj["token"];

    var uid = await getUIDUsingToken(token).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

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
        
      }).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

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
        .catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });

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
    delete user["Email"];
    delete user["Password"];

    var uid = await getUIDUsingToken(token).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    
    var currentRef=determineRef(type);

    await currentRef.child(uid).set(user)
        .then((completed) => {
            response.json({"data":{"ok":"User updated successfully!"}});
            return;
        })
        .catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
});

exports.editAnime = functions.https.onRequest(async (request, response) => {
    const json = request.body["data"];
    userObj = JSON.parse(json);

    const token = userObj["Token"];
    console.log(token);

    const anime = userObj["Anime"];
    
 
    const animeName = anime["name"]

    var uid = await getUIDUsingToken(token).catch(error => {
        console.log(error);
        response.json({"data":{"error":`${error}`}});
        return;
    });
    var currAnime;
    await animeRef.child(animeName).get().then((dataSnapshot) => {
        currAnime = dataSnapshot.val()
    })
    .catch(error => {
        response.json({"data":{"error":`${error}`}});
        console.log(error);
        return;
    });
    console.log(currAnime);
    for (const [key, value] of Object.entries(anime)) {
        if (value != null && currAnime.hasOwnProperty(key)){
            currAnime[key] = value;
        }
    }
    if(!currAnime.hasOwnProperty('likeCounter'))
        currAnime['likeCounter']=0
    
    if(!currAnime.hasOwnProperty('dislikeCounter'))
        currAnime['dislikeCounter']=0

    console.log(currAnime);

    animeRef.child(animeName).set(currAnime).then(obj => {
        response.json({"ok":'1'});
    })
    .catch(error => {
        response.json({"data":{"error":`${error}`}});
        console.log(error);
        return;
    });

/*
    private String name;
    private String description;
    private String imageURL;
    private HashMap<String, Float> genres;
    private String ownerUID;
    private int likeCounter = 0;
    private int dislikeCounter = 0;
*/
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

    var uid = await getUIDUsingToken(token).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    
    var currentRef=determineRef(type);

    await currentRef.child(uid).child("genres").set(genres)
        .then((completed) => {
            response.json({"data":{"ok":"User genres updated successfully!"}});
            return;
        })
        .catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
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

    // console.log("AnimeObj: ",animeObj)
    // console.log("animeArr: ",animeArr)
    //const animeMap = new Map();
    var animeRetObj = {};

     for(var i = 0; i<animeArr.length; i+=1){
        await animeRef.child(animeArr[i]).get().then((dataSnapshot) => {
            animeRetObj[animeArr[i]] = dataSnapshot.val()
            // console.log("data",dataSnapshot.val())
        }).catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
     }

    // await animeArr.forEach((element) => {
    //     console.log("element: ", element);
    //     animeRef.child(element).get().then((dataSnapshot) => {
    //         animeMap.set(element ,dataSnapshot.val())
    //         console.log("data",dataSnapshot.val())
    //     }).catch(error => {
    //         response.json({"data":{"error":`${error}`}});
    //         return;
    //     });
        
    // });

    console.log("animeRetObj: ",animeRetObj)

    response.json({"data":{"ok":JSON.stringify(animeRetObj)}});
    return;
});

exports.getAllAnimeOfCreator = functions.https.onRequest(async (request, response) => {
    const json = request.body["data"];
    inputObj = JSON.parse(json);

    const token = userObj["Token"];
    var uid = await getUIDUsingToken(token).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    animes = []

    await animeRef.once('value').then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
            var anime = childSnapshot.val();
            anime.name=childSnapshot.key;
            
            animes.push(anime);
            
        });
    }).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    response.json({"data":{"ok":JSON.stringify(animes)}});
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
    }).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    response.json({"data":{"ok":JSON.stringify(objects)}});
    return;
    
}); 

exports.getBestKAnime = functions.https.onRequest(async (request, response) => {
    const json = request.body["data"];
    inputObj = JSON.parse(json);

    const token = inputObj["Token"];
    console.log("token: ",token);

    var uid = await getUIDUsingToken(token).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    var userObj; 
    await fanRef.child(uid).get().then(dataSnapshot => {
        userObj=dataSnapshot.val()
    })
    //console.log("userObj",userObj);

    const blackListedAnimes = userObj["blacklist"];
    const whiteListedAnimes = userObj["whitelist"];

    const userGenres = userObj["genres"];
    console.log("userGenres: ",userGenres);

    var objects = [];
    await animeRef.once('value').then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
            var object = childSnapshot.val();
            object.name=childSnapshot.key;
            
            var animeObj = {
                name: object.name,
                genres: object.genres
            };
            
            var checkBlackList = blackListedAnimes != undefined && blackListedAnimes != null && blackListedAnimes[object.name] != undefined;
            var checkWhiteList = whiteListedAnimes != undefined && whiteListedAnimes != null && whiteListedAnimes[object.name] != undefined;
            if(object.name == 'Another'){
                console.log("checkBlackList",checkBlackList);
                console.log("checkWhiteList",checkWhiteList);
            }

            if(!checkBlackList && !checkWhiteList){
                objects.push(animeObj);
            }
        });
    }).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    //console.log("objects: ",objects);

    const userGenresMap = new Map(Object.entries(userGenres));
    console.log("userGenresMap", userGenresMap)
    const calcDistance = (currAnimeObj) => {
        var sum = 0;
        userGenresMap.forEach((value, key) => {
            // console.log("map value, key",value,key)
            // console.log("curr value",currAnimeObj.genres[key])
            var curr_value = currAnimeObj.genres[key]
            if(curr_value != undefined && curr_value != null)
                sum += Math.abs(value - currAnimeObj.genres[key])
        });
        console.log("sum",sum);
        return sum;
    };

    objects = objects.sort((a, b) => calcDistance(a) - calcDistance(b))
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
  
exports.getAllComments = functions.https.onRequest(async (request, response) => {K
    const fans = await fanRef.get().catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    var comments = {}




    await commentRef.once('value').then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
            var commentList = []

            var commentsForAnime = childSnapshot.val();
            var map = new Map(Object.entries(commentsForAnime));
            var animeName =childSnapshot.key;
            for (let [key, value] of map) {
                var currentComment = value;
                currentComment["authorName"] = `${fans[key].fName} ${fans[key].lName}`;
                
                commentList.push(currentComment);
            }


            comments[animeName] = commentList;
        }
        );
    }).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    response.json({"data":{"ok":JSON.stringify(comments)}});
    return;
});

exports.uploadComment =functions.https.onRequest(async (request, response) => {
    /*
    {
        "Token": "l;sdmflksdf"
        "comment": {
            "animeRef" : animeName
            "authorName" : "john baker"
            "content" : comments
            "stars" : 1-5 stars 
        }

    }
    const json = request.body["data"];
    */  
    const json = request.body["data"];
    console.log(json);
    const inputObj = JSON.parse(json);

    const token = inputObj["Token"];
    console.log(token);

    var uid = await getUIDUsingToken(token).catch(error => {
        response.json({"data":{"error":`${error}`}});
        return;
    });

    const commentObj = inputObj["comment"];

    const animeName = commentObj["animeRef"];
    console.log(animeName);

    delete commentObj["authorName"];

    const content = commentObj["content"];
    console.log(content);

    const stars = commentObj["stars"];
    console.log(stars);

    
    await commentRef.child(animeName).child(uid).set(commentObj)
        .then((obj) => {
            response.json({"data":{"ok":"Comment uploaded successfully!"}});
            return;
        })
        .catch(error => {
            response.json({"data":{"error":`${error}`}});
            return;
        });

});
