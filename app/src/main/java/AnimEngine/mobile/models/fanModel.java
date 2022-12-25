package AnimEngine.mobile.models;

import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Comment;

public class fanModel extends Model{


    public void uploadComment(Comment comment){
        Gson gson = new Gson();
        AnimeAndToken animeAndToken = new AnimeAndToken();

        String json = gson.toJson(comment);

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
}
