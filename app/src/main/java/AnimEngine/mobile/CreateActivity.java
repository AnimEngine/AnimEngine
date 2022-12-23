package AnimEngine.mobile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import AnimEngine.mobile.adapters.CreateRVAdapter;
import AnimEngine.mobile.classes.Genre;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    ImageView animeImage;
    EditText animeName, animeDescription;
    Uri animeURI;

    RecyclerView recyclerView;
    ArrayList<Genre> genres;
    ArrayList<Pair<CheckBox, Slider>> checkBox_Slider_pairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        findViewById(R.id.button_choose_image).setOnClickListener(this);
        findViewById(R.id.button_submit_create).setOnClickListener(this);

        genres = new ArrayList<>();
        loadGenres();

        checkBox_Slider_pairs = new ArrayList<>();

        TextInputLayout textInputLayoutName = findViewById(R.id.text_input_anime_name);
        animeName = textInputLayoutName.getEditText();

        TextInputLayout textInputLayoutNameDescription = findViewById(R.id.text_input_anime_description);
        animeDescription = textInputLayoutNameDescription.getEditText();



        animeImage = findViewById(R.id.image_create);
        recyclerView = findViewById(R.id.recycler_create);

        CreateRVAdapter customCreateRVAdapter = new CreateRVAdapter(CreateActivity.this, getLayoutInflater(), genres, checkBox_Slider_pairs);
        recyclerView.setAdapter(customCreateRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CreateActivity.this));

        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        this.animeURI = uri;
                        animeImage.setImageURI(uri);
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.button_choose_image)){
            ActivityResultContracts.PickVisualMedia.VisualMediaType mediaType = (ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;

            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(mediaType)
                    .build());
        }

        if(view == findViewById(R.id.button_submit_create)){
            String name = String.valueOf(animeName.getText());
            if(name.length() < 1){
                Toast.makeText(getApplicationContext(),"Please enter anime name!",Toast.LENGTH_SHORT).show();
                return;
            }

            String description = String.valueOf(animeDescription.getText());
            if(description.length() < 1){
                Toast.makeText(getApplicationContext(),"Please enter anime description!",Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, Float> map = new HashMap<>();
            for(Pair<CheckBox, Slider> pair: this.checkBox_Slider_pairs){
                float value= 0.0F;
                if(pair.first.isChecked()){
                    value = pair.second.getValue();
                }
                map.put((String) pair.first.getText(), value);
            }

            if(this.animeURI == null){
                Toast.makeText(getApplicationContext(),"Please choose an image for the Anime!",Toast.LENGTH_SHORT).show();
                return;
            }

            InputStream is;
            String encodedImage;
            try {
                is = getApplicationContext().getContentResolver().openInputStream(this.animeURI);

                Bitmap bm = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                byte[] b = baos.toByteArray();

                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private void loadGenres(){
        String genresJson="";
        try {
            InputStream is = getApplicationContext().getAssets().open("genres.json");

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
                this.genres.add(new Genre(genre));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}