package AnimEngine.mobile.models;

import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.User;

class AnimeAndToken{
    String token;
    Anime anime;
}

class UserAndTokenAndType{
    String Token;
    String Type;
    StrippedUser User;
}

class StrippedFan implements StrippedUser{
    String fName;
    String lName;
}

class StrippedCreator implements StrippedUser{
    String studioName;
    String webAddress;
}

interface StrippedUser{
}
public class creatorModel extends Model{

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

    public void editUser(String token, String type, User user){
        UserAndTokenAndType obj = new UserAndTokenAndType();
        obj.Token=token;
        obj.Type=type;
        if(Objects.equals(type, "fan")){
            StrippedFan strippedFan = new StrippedFan();
            strippedFan.fName = ((Fan) user).getFName();
            strippedFan.lName = ((Fan) user).getLName();

            obj.User=strippedFan;
        }else{
            StrippedCreator strippedCreator = new StrippedCreator();
            strippedCreator.studioName = ((Creator) user).getStudioName();
            strippedCreator.webAddress = ((Creator) user).getWebAddress();

            obj.User=strippedCreator;
        }

        Gson gson = new Gson();
        String json = gson.toJson(obj, UserAndTokenAndType.class);
        Log.e("edit_json", json);

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

    public void EditAnime(Anime anime, String token) {
        Gson gson = new Gson();
        AnimeAndToken animeAndToken = new AnimeAndToken();
        animeAndToken.token=token;
        animeAndToken.anime=anime;
        String json = gson.toJson(animeAndToken, AnimeAndToken.class);
        Log.d("update_json_anime", json);
        this.mFunctions
                .getHttpsCallable("editAnime")
                .call(json).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap map = (HashMap) task.getResult().getData();
                        if (map == null) {
                            result = "ERROR";
                        }
                        else {
                            if (map.containsKey("ok")) {
                                result = "OK";
                            }
                            else {
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
