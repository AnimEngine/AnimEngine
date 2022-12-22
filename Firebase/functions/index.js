const functions = require("firebase-functions");

const admin = require('firebase-admin');
const { auth } = require("firebase-admin");

admin.initializeApp();

const db = admin.database();
const creatorRef = db.ref("/Users/Creator");
const fanRef = db.ref("/Users/Fan");

const animeRef = db.ref("/Anime");

exports.register = functions.https.onRequest(async (request, response) => {
    // Grab the request body as JSON and parse it into a JS object.
    // console.log(request.body);

    const json = request.body["data"];
    userObj = JSON.parse(json);

    // Extract variables
    const email = userObj["Email"];
    const password = userObj["Password"];

    const userType = userObj["Type"];
    if(userType == "fan")
        currentRef = fanRef;
    else
        currentRef = creatorRef;
    

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
            }).catch((error) => {
                response.json({"data":{"error":`${error}`}});
            });
        })
        .catch((error) => {
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
    // catch(exception){
    //     console.log(exception);
    //     response.json({"data":{"error":`input parsing`}});
    //     return;
    // }
    
    // Extract variables
    const token = inputObj["Token"];
    console.log(token);

    var uid = "";
    await admin.auth().verifyIdToken(token)
        .then((decodedToken) => {
            uid = decodedToken.uid;
        })
        .catch((error) => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
    
    var creatorJson;
    var fanJson;

    await creatorRef.child(uid).get()
        .then((dataSnapshot) => {creatorJson=dataSnapshot.toJSON()})
        .catch((error) => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
        
    await fanRef.child(uid).get()
        .then((dataSnapshot) => {fanJson=dataSnapshot.toJSON()})
        .catch((error) => {
            response.json({"data":{"error":`${error}`}});
            return;
        });
    
    response.json({"data":{"ok":{"creator":`${JSON.stringify(creatorJson)}`, "fan":`${JSON.stringify(fanJson)}`}}});
    return;
});
