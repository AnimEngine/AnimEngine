package AnimEngine.mobile.models;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import AnimEngine.mobile.classes.Anime;

public class dbAndStorageModel extends Model{
    List animeList;
    ArrayList<Anime> animeArrayList;

    public void getAllAnime(){
        Gson gson = new Gson();
        final JSONObject[] animeArray = {null};
        animeArrayList = new ArrayList<>();
        this.mFunctions
                .getHttpsCallable("getAllAnime")
                .call().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap map = (HashMap) task.getResult().getData();
                        if (map == null) {
                            result = "ERROR";

                            setChanged();
                            notifyObservers();
                        } else {
                            if (map.containsKey("ok")) {
                                result = "OK";
//                                JSONObject jsonObject = new JSONObject(map);
//                                try {
//                                    JSONArray jsonArray = jsonObject.getJSONArray("ok");
//                                    jsonArray.length();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                                String animeArrayString = (String) map.get("ok");
                                if (!Objects.equals(animeArrayString, "null")){

                                    animeList = gson.fromJson((String) animeArrayString, List.class);
                                    for (int i = 0; i < animeList.size(); i++) {
                                        Anime current = gson.fromJson(gson.toJson(animeList.get(i)), Anime.class);
                                        animeArrayList.add(current);
                                    }
                                    result = "OK";

                                    setChanged();
                                    notifyObservers();
                                }
                            } else {
                                result = "ERROR:" + map.get("error");

                                setChanged();
                                notifyObservers();
                            }
                        }

                    }else{
                        result = "ERROR";

                        setChanged();
                        notifyObservers();
                    }
                });
    }

    public void getBestKAnime(){

    }



    public List getAnimeList() {
        return animeList;
    }

    public void setAnimeList(List animeList) {
        this.animeList = animeList;
    }

    public ArrayList<Anime> getAnimeArrayList() {
        return animeArrayList;
    }

    public void setAnimeArrayList(ArrayList<Anime> animeArrayList) {
        this.animeArrayList = animeArrayList;
    }
}
