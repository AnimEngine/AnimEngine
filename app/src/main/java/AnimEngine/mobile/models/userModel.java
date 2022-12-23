package AnimEngine.mobile.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;
import java.util.Observable;

import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.User;
import AnimEngine.mobile.classes.UserAndToken;
public class userModel extends Observable {

    private String result = "";
    private String action;
    private final FirebaseFunctions mFunctions;
    private final FirebaseAuth mAuth;

    private UserAndToken creator, fan;

    public static final String REGISTER = "REGISTER";
    public static final String LOGIN = "LOGIN";
    public static final String FORGOT = "FORGOT";

    public userModel(UserAndToken creator, UserAndToken fan){
        this.mFunctions = FirebaseFunctions.getInstance();
        this.mAuth = FirebaseAuth.getInstance();

        this.creator=creator;
        this.fan=fan;
    }

    public void register(User user){
        // Create the arguments to the callable function.
        Gson gson = new Gson();
        String jsonToSend = gson.toJson(user);

        Log.d("testingos_register", jsonToSend);
        this.mFunctions
                .getHttpsCallable("register")
                .call(jsonToSend).addOnCompleteListener(task -> {
                    HashMap map = (HashMap) task.getResult().getData();
                    if(map == null){
                        result = "ERROR";
                    }else {
                        if (map.containsKey("ok")) {
                            result = "OK";
                            this.action = REGISTER;
                        } else {
                            result = "ERROR:"+map.get("error");
                        }
                    }
                    setChanged();
                    notifyObservers();
                });
    }

    public void login(String email, String password){
        Gson gson = new Gson();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            assert user != null;
                            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        String token = task.getResult().getToken();
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
                                                                action = LOGIN;

                                                                creator.setUser(innerCreator);
                                                                fan.setUser(innerFan);

                                                                Log.d("login_map", map.toString());
                                                                Log.d("login_creator", creator.toString());
                                                                Log.d("login_fan", fan.toString());

                                                            } else {
                                                                result = "ERROR:"+map.get("error")+"(CLOUD_FUNCTION_RETURN)";
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        Log.w("testingos_firebase", functionTask.getException());
                                                        result = "ERROR:Login Failed!(CLOUD_FUNCTION_CALL)";
                                                    }
                                                    setChanged();
                                                    notifyObservers();
                                                });

                                        Log.d("firebaseAuthToken: ", token);
                                    }else
                                        result="ERROR: Login failed!(GET_TOKEN)";

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
                    }
                });
    }

    public void forgot(String email, String password){
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
                                this.action = FORGOT;
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
        if(this.result != null)
            return result;
        else
            return "";
    }

    public String getAction() {
        return action;
    }
}
