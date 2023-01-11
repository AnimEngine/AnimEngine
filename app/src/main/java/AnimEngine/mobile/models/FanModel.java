package AnimEngine.mobile.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Comment;
import AnimEngine.mobile.classes.Fan;

public class FanModel extends Model{

    public static final String POST_COMMENT = "POST_COMMENT";

    static Type getUploadCommentInputType = new TypeToken<HashMap<String, Comment>>() {}.getType();

    public void uploadComment(Comment comment){
        action = POST_COMMENT;

        Gson gson = new Gson();
        HashMap<String, Comment> inputMap = new HashMap<>();
        inputMap.put("comment", comment);

        String json = gson.toJson(inputMap, getUploadCommentInputType);

        Log.d("update_comment_json", json);

        this.mFunctions
                .getHttpsCallable("uploadComment")
                .call(json).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap map = (HashMap) task.getResult().getData();
                        if (map == null) {
                            result = "ERROR";
                        } else {
                            if (map.containsKey("ok")) {
                                result = "OK";
                            } else {
                                result = "ERROR:" + map.get("error");
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
    public void editFan(Fan fan, String token){
        Gson gson = new Gson();
        String json = String.format("{\"Token\":\"%s\", \"Type\": \"fan\", \"User\":%s}",token,gson.toJson(fan, Fan.class));
        Log.d("json_editFan",json);

        this.mFunctions
                .getHttpsCallable("editUser")
                .call(json).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap map = (HashMap) task.getResult().getData();
                        if (map == null) {
                            result = "ERROR";
                        } else {
                            if (map.containsKey("ok")) {
                                result = "OK";
                            } else {
                                result = "ERROR:" + map.get("error");
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
}
