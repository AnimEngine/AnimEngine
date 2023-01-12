package AnimEngine.mobile.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Helper {
    public static HashMap<String, Float> createGenresHashMap(Context context){
        /*
        read json from assets and load into genre objects
         */
        HashMap<String, Float> ret = new HashMap<>();
        String genresJson="";
        try {
            InputStream is = context.getAssets().open("genres.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            genresJson = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(genresJson);

            JSONArray genres = obj.getJSONArray("genres");

            for (int i = 0; i < genres.length(); i++) {
                String genre = (String) genres.get(i);
                ret.put(genre, 0.0f);
            }

            return ret;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
