package AnimEngine.mobile.models;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;

import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.User;
import AnimEngine.mobile.classes.UserAndToken;
public class UserModel extends Model{

    private final UserAndToken creator;
    private final UserAndToken fan;

    public static final String REGISTER = "REGISTER";
    public static final String LOGIN = "LOGIN";
    public static final String FORGOT = "FORGOT";

    public UserModel(UserAndToken creator, UserAndToken fan){
        this.creator=creator;
        this.fan=fan;
    }

    public void register(User user){
        this.action = REGISTER;

        // Create the arguments to the callable function.
        Gson gson = new Gson();
        String jsonToSend = gson.toJson(user);

        Log.d("register_json", jsonToSend);
        this.mFunctions
                .getHttpsCallable("register")
                .call(jsonToSend).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap map = (HashMap) task.getResult().getData();
                        if(map == null){
                            result = "ERROR";
                        }else {
                            if (map.containsKey("ok")) {
                                result = "OK";
                            } else {
                                result = "ERROR:"+map.get("error");
                            }
                        }
                    }
                    else{
                        result = "ERROR";
                    }
                    setChanged();
                    notifyObservers();
                });
    }

    public void login(String email, String password){
        this.action = LOGIN;

        Gson gson = new Gson();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = task.getResult().getUser();
                        assert user != null;
                        user.getIdToken(true).addOnCompleteListener(getTokenTask -> {
                            if (getTokenTask.isSuccessful()) {
                                String token = getTokenTask.getResult().getToken();
                                creator.setToken(token);
                                fan.setToken(token);

                                String json = String.format("{\"Token\":\"%s\"}", token);

                                Log.d("login_json", json);

                                mFunctions
                                        .getHttpsCallable("login")
                                        .call(json).addOnCompleteListener(functionTask -> {
                                            if (functionTask.isSuccessful()) {
                                                HashMap map = (HashMap) functionTask.getResult().getData();
                                                if(map == null){
                                                    result = "ERROR";
                                                }else {
                                                    if (map.containsKey("ok")) {
                                                        Creator innerCreator = null;
                                                        Fan innerFan = null;
                                                        if (!Objects.equals((String) ((HashMap) map.get("ok")).get("creator"), "null")){
                                                            innerCreator = gson.fromJson((String) ((HashMap) map.get("ok")).get("creator"), Creator.class);
                                                            innerCreator.setEmail(email);
                                                            innerCreator.setPassword(password);
                                                        }

                                                        if (!Objects.equals((String) ((HashMap) map.get("ok")).get("fan"), "null")){
                                                            innerFan = gson.fromJson((String) ((HashMap) map.get("ok")).get("fan"), Fan.class);
                                                            innerFan.setEmail(email);
                                                            innerFan.setPassword(password);
                                                        }
                                                        result = "OK";


                                                        creator.setUser(innerCreator);
                                                        fan.setUser(innerFan);

                                                        Log.d("login_map", map.toString());
                                                        Log.d("login_creator", creator.toString());
                                                        Log.d("login_fan", fan.toString());

                                                    } else {
                                                        result = "ERROR:"+map.get("error")+"(CLOUD_FUNCTION_RETURN)";

                                                        creator.setUser(null);
                                                        fan.setUser(null);
                                                    }
                                                }
                                            }
                                            else{
                                                Log.w("login_firebase_exception", functionTask.getException());
                                                result = "ERROR:Login Failed!(CLOUD_FUNCTION_CALL)";

                                                creator.setUser(null);
                                                fan.setUser(null);
                                            }
                                            setChanged();
                                            notifyObservers();
                                        });

                                Log.d("firebaseAuthToken: ", token);
                            }else {
                                result = "ERROR: Login failed!(GET_TOKEN)";

                                creator.setUser(null);
                                fan.setUser(null);

                                setChanged();
                                notifyObservers();
                            }
                        });

                    } else {
                        // If sign in fails, display a message to the user.
                        result="ERROR: Login failed!(Incorrect Password)";

                        creator.setUser(null);
                        fan.setUser(null);

                        setChanged();
                        notifyObservers();
                    }
                });
    }

    public void forgot(String email, String password){
        this.action = FORGOT;

        String json = String.format("{\"Email\":\"%s\", \"Password\":\"%s\"}", email, password);
        Log.d("forgot_json", json);

        this.mFunctions
                .getHttpsCallable("forgot")
                .call(json).addOnCompleteListener(functionTask -> {
                    if (functionTask.isSuccessful()) {
                        HashMap map = (HashMap) functionTask.getResult().getData();
                        if(map == null){
                            result = "ERROR";
                        }
                        else{
                            if (map.containsKey("ok")) {
                                result = "OK";
                                Log.d("forgot_map", map.toString());

                                //fan.setUser(gson.);
                            }
                            else {
                                result = "ERROR:"+map.get("error")+"(CLOUD_FUNCTION_RETURN)";
                            }
                        }
                    }
                    else{
                        Log.w("testingos_firebase", functionTask.getException());
                        result = "ERROR:Password update Failed!(CLOUD_FUNCTION_CALL)";
                    }
                    setChanged();
                    notifyObservers();
                });

    }


    public String getResult() {
        if(super.result != null)
            return super.result;
        else
            return "";
    }

    public String getAction() {
        return action;
    }

    public UserAndToken getCreator() {
        return creator;
    }

    public UserAndToken getFan() {
        return fan;
    }
}
