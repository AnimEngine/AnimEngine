const functions = require("firebase-functions");

const admin = require('firebase-admin');
const { auth } = require("firebase-admin");

// var serviceAccount = require("C:\\Users\\gilig\\Desktop\\Firebase\\functions\\animengine-fb858-firebase-adminsdk-1l8jr-9df9aa102d.json");
// admin.initializeApp({
//     credential: admin.credential.cert(serviceAccount),
//     databaseURL: "https://animengine-fb858-default-rtdb.firebaseio.com"
// });
admin.initializeApp();

const db = admin.database();
const creatorRef = db.ref("/Users/Creator");
const fanRef = db.ref("/Users/Fan");

const animeRef = db.ref("/Anime");

exports.register = functions.https.onRequest(async (request, response) => {
    // Grab the request body as JSON and parse it into a JS object.
    // console.log(request.body);
    // response.json(request.body);

    const json = request.body["data"];
    userObj = JSON.parse(json);
    //userObj = userObj["data"];

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
    const json = request.body;
    const userObj = JSON.parse(JSON.stringify(json));

    // Extract variables
    const token = userObj["Token"];
    const userType = userObj["Type"];

    if(userType == "fan")
        currentRef = fanRef;
    else
        currentRef = creatorRef;

    await admin.auth().verifyIdToken(token)
        .then((decodedToken) => {
            const uid = decodedToken.uid;

            response.json(currentRef.child(uid).get());
            return;
        })
        .catch((error) => {
            response.json({"error":`${error}`});
            return;
        });
});