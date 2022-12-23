package AnimEngine.mobile.models;

import android.util.Log;

import androidx.core.util.Pair;

import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;

import java.util.HashMap;

import AnimEngine.mobile.classes.Anime;
class AnimeAndToken{
    String token;
    Anime anime;
}
public class creatorModel extends Model{
    private final FirebaseFunctions mFunctions;
    public creatorModel(){
        this.mFunctions = FirebaseFunctions.getInstance();
    }

    public void update(Anime anime, String token){
        Gson gson = new Gson();
        AnimeAndToken animeAndToken = new AnimeAndToken();

        animeAndToken.token=token;
        animeAndToken.anime=anime;
        //String animeJson = gson.toJson(anime, Anime.class);

        //String json = String.format("{\"Token\":\"%s\"}", token);
        String json = gson.toJson(animeAndToken, AnimeAndToken.class);
        Log.d("update_json", json);

        this.mFunctions
                .getHttpsCallable("upload")
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
