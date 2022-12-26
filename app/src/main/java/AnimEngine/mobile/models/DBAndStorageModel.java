package AnimEngine.mobile.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Comment;

class CommentAndToken {
    String Token;
    Comment comment;
}

public class DBAndStorageModel extends Model {
    //getAllAnime
    static Type getAllAnimeResultType = new TypeToken<List<Anime>>() {
    }.getType();
    List<Anime> getAllAnimeResult;

    // getBestKAnime
    static Type getBestKAnimeResultType = new TypeToken<List<String>>() {
    }.getType();
    List<String> getBestKAnimeResult;

    // getAnime
    static Type getAnimeResultType = new TypeToken<HashMap<String, Anime>>() {
    }.getType();
    HashMap<String, Anime> getAnimeResult;


    public static final String GET_ALL = "GET_ALL";
    public static final String GET_BEST_K = "GET_BEST_K";
    public static final String GET_ANIME = "GET_ANIME";
    public static final String GET_ALL_COMMENTS = "GET_ALL_COMMENTS";


    Gson gson = new Gson();

    public void getAllAnime() {
        action = GET_ALL;
        getAllAnimeResult = new ArrayList<>();

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
                                if (!Objects.equals(animeArrayString, "null")) {


                                    getAllAnimeResult = gson.fromJson((String) animeArrayString, getAllAnimeResultType);
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

                    } else {
                        result = "ERROR";

                        setChanged();
                        notifyObservers();
                    }
                });
    }

    public void getBestKAnime(String token) {
        action = GET_BEST_K;

        Gson gson = new Gson();
        String json = String.format("{\"Token\":\"%s\"}", token);

        Log.d("engine_json", json);
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

                                    getBestKAnimeResult = gson.fromJson((String) animeArrayString, DBAndStorageModel.getBestKAnimeResultType);
                                    this.getAnime(getBestKAnimeResult, false);

                                    result = "OK";
                                }
                            } else {
                                result = "ERROR:" + map.get("error");
                            }
                        }

                    } else {
                        result = "ERROR";
                    }
                });
    }

    public void getAnime(List<String> animeNames, boolean isIndependent) {
        if (isIndependent)
            action = GET_ANIME;

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
                                    getAnimeResult = gson.fromJson((String) animeDictString, DBAndStorageModel.getAnimeResultType);
                                    result = "OK";
                                }
                            } else {
                                result = "ERROR:" + outputMap.get("error");
                            }
                        }

                    } else {
                        result = "ERROR";
                    }

                    setChanged();
                    notifyObservers();
                });
    }

    public void getAllComments() {
        action = GET_ALL_COMMENTS;

        this.mFunctions
                .getHttpsCallable("getAllComments")
                .call().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap outputMap = (HashMap) task.getResult().getData();
                        if (outputMap == null) {
                            result = "ERROR";
                        } else {
                            if (outputMap.containsKey("ok")) {
                                result = "OK";
                            } else {
                                result = "ERROR:" + outputMap.get("error");
                            }
                        }

                    } else {
                        result = "ERROR";
                    }

                    setChanged();
                    notifyObservers();
                });
    }


    public List<Anime> getGetAllAnimeResult() {
        return getAllAnimeResult;
    }

    public List<String> getGetBestKAnimeResult() {
        return getBestKAnimeResult;
    }

    public HashMap<String, Anime> getGetAnimeResult() {
        return getAnimeResult;
    }
}
