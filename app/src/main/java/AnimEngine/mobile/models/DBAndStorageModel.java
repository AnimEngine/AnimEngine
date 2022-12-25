package AnimEngine.mobile.models;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import AnimEngine.mobile.classes.Anime;

public class dbAndStorageModel extends Model{
    List animeList;
    ArrayList<Anime> animeArrayList;

    HashMap getAnimeResult;

    Gson gson = new Gson();

    public void getAllAnime(){

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
    public void getBestKAnime(String token){
        Gson gson = new Gson();
        String json = String.format("{\"Token\":\"%s\"}", token);

        final List[] animeNames = new List[1];

        Log.d("engine_json",json);
        this.mFunctions
                .getHttpsCallable("getBestKAnime")
                .call(json).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap map = (HashMap) task.getResult().getData();
                        if (map == null) {
                            result = "ERROR";
                        } else {
                            if (map.containsKey("ok")) {
                                result = "OK";
                                String animeArrayString = (String) map.get("ok");
                                if (!Objects.equals(animeArrayString, "null")) {

                                    animeNames[0] = gson.fromJson((String) animeArrayString, List.class);

                                    this.getAnime(animeNames[0]);

                                    result = "OK";
                                }
                                } else {
                                    result = "ERROR:" + map.get("error");
                                }
                            }

                        }else{
                            result = "ERROR";
                        }

                        setChanged();
                        notifyObservers();
                    });
    }

    public void getAnime(List<String> animeNames){
        HashMap<String, List<String>> inputMap = new HashMap<>();
        inputMap.put("anime", animeNames);
        String json = gson.toJson(inputMap, HashMap.class);
        Log.d("getAnime_json", json);
        this.mFunctions
                .getHttpsCallable("getAnime")
                .call(json).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap outputMap = (HashMap) task.getResult().getData();
                        if (outputMap == null) {
                            result = "ERROR";
                        } else {
                            if (outputMap.containsKey("ok")) {
                                result = "OK";
                                String animeDictString = (String) outputMap.get("ok");
                                if (!Objects.equals(animeDictString, "null")) {

                                    getAnimeResult = gson.fromJson((String) animeDictString, HashMap.class);
                                    result = "OK";
                                }
                            } else {
                                result = "ERROR:" + outputMap.get("error");
                            }
                        }

                    }else{
                        result = "ERROR";
                    }

                    setChanged();
                    notifyObservers();
                });
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
