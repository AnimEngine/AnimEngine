package AnimEngine.mobile.models;

import androidx.core.util.Consumer;

import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Observable;

import AnimEngine.mobile.classes.User;

public class userModel extends Observable {
    private String result = "";
    private final FirebaseFunctions mFunctions;
    public userModel(){
        this.mFunctions = FirebaseFunctions.getInstance();
    }

    public void register(User user){
        // Create the arguments to the callable function.
        Gson gson = new Gson();
        String jsonToSend = gson.toJson(user);

        this.mFunctions
                .getHttpsCallable("register")
                .call(jsonToSend).addOnCompleteListener(task -> {
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
                    setChanged();
                    notifyObservers();
                });
    }

    public String getResult() {
        return result;
    }
}
