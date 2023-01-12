package AnimEngine.mobile;

import static AnimEngine.mobile.util.Helper.createGenresHashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.DBAndStorageModel;
import AnimEngine.mobile.models.FanModel;
import AnimEngine.mobile.models.Model;
import AnimEngine.mobile.util.InitialContext;
import AnimEngine.mobile.util.ModelLocator;
import AnimEngine.mobile.util.RunnableWithStatus;

public class EngineActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, Observer {

    TextView textViewAnimeName;
    ImageView imageViewAnimeImage;

    UserAndToken fan;
    Fan fanObj;

    ModelLocator modelLocator;
    DBAndStorageModel DBModel;
    FanModel fanModel;

    BottomNavigationView bottomNavigationView;
    Queue<Anime> animeQueue;
    Queue<RunnableWithStatus<Bitmap>> animeImageTasksQueue;
    HashMap<String, RunnableWithStatus<Bitmap>> animeImageTasks;

    EditText content;
    RatingBar stars;

    HashMap<String, String> blacklist = new HashMap<>();
    HashMap<String, String> whitelist = new HashMap<>();
    Anime currentAnime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine);

        textViewAnimeName = findViewById(R.id.text_view_anime_name_engine);
        imageViewAnimeImage = findViewById(R.id.image_anime_engine);

        modelLocator = ((MyApplication)getApplication()).getModelLocator();

        try {
            DBModel = (DBAndStorageModel) modelLocator.getModel(InitialContext.DBSTORAGE, null);
            fanModel = (FanModel)  modelLocator.getModel(InitialContext.FAN, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DBModel.addObserver(this);
        fanModel.addObserver(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_page);

        bottomNavigationView.setOnItemSelectedListener(this);

        animeQueue = new LinkedList<>();
        animeImageTasksQueue = new LinkedList<>();
        animeImageTasks = new HashMap<>();

        fan = (UserAndToken) getIntent().getSerializableExtra("fan");
        fanObj = (Fan) fan.getUser();

        findViewById(R.id.button_show_information).setOnClickListener(this);
        findViewById(R.id.button_not_watch_later_anime).setOnClickListener(this);
        findViewById(R.id.button_watch_later_anime).setOnClickListener(this);
        findViewById(R.id.button_add_comment).setOnClickListener(this);

        DBModel.getBestKAnime(fan.getToken());

    }

    private void displayNextAnime(){
        if(animeQueue.isEmpty()){
            Toast.makeText(getApplicationContext(),"No more anime's left to recommend!",Toast.LENGTH_SHORT).show();
            currentAnime = null;
        }
        else{
            currentAnime = animeQueue.poll();

            if(animeImageTasksQueue.poll().isFinished()) {
                Bitmap currentImage = Objects.requireNonNull(animeImageTasks.get(currentAnime.getName()).getRet());
                imageViewAnimeImage.setImageBitmap(currentImage);
            }

            textViewAnimeName.setText(currentAnime.getName());

        }
    }
    private void displayFirstAnime(Anime anime){
        Picasso.get().load(anime.getImageURL()).into(imageViewAnimeImage);
        textViewAnimeName.setText(anime.getName());
        currentAnime = anime;
    }

    @Override
    public void onClick(View view){

        if(view == findViewById(R.id.button_not_watch_later_anime)){
            blacklist.put(currentAnime.getName(), currentAnime.getName());
            displayNextAnime();
        }

        if(view == findViewById(R.id.button_watch_later_anime)){
            whitelist.put(currentAnime.getName(),currentAnime.getName());
            displayNextAnime();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(fanObj.getWhitelist() != null)
            whitelist.putAll(fanObj.getWhitelist());

        if(fanObj.getBlacklist() != null)
            blacklist.putAll(fanObj.getBlacklist());

        fanObj.setWhitelist(whitelist);
        fanObj.setBlacklist(blacklist);

        HashMap<String, Float> currentGenres = fanObj.getGenres();
        if(currentGenres == null)
            currentGenres = createGenresHashMap(getApplicationContext());

        fanObj.setGenres(currentGenres);

        fanModel.editFan(fanObj, fan.getToken());

        Intent intent;
        int curr = item.getItemId();

        intent = new Intent(this, ProfileActivity.class);


        if (R.id.catalog_page == curr) {
            intent = new Intent(this, CatalogActivity.class);
        }

        intent.putExtra("fan", fan);
        startActivity(intent);
        finish();

        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        String result = ((Model) o).getResult();


        if(Objects.equals(result, ""))
            return;

        if(result.startsWith("OK")){
            String currentAction = ((Model) o).getAction();

            if (Objects.equals(currentAction, DBAndStorageModel.GET_BEST_K)) {
                animeQueue.clear();
                animeImageTasksQueue.clear();

                List<String> animeNames = DBModel.getGetBestKAnimeResult();
                HashMap<String, Anime> animeObjects = DBModel.getGetAnimeResult();
                if(!animeNames.isEmpty()) {
                    String animeName = animeNames.remove(0);
                    Anime currentAnime = animeObjects.remove(animeName);
                    currentAnime.setName(animeName);

                    displayFirstAnime(currentAnime);
                }else
                    return;


                for (String name : animeNames) {
                    Anime currentAnime = animeObjects.get(name);
                    currentAnime.setName(name);

                    animeQueue.add(currentAnime);


                    RunnableWithStatus<Bitmap> runnable = new RunnableWithStatus<Bitmap>(() -> {
                        try {
                            return Picasso.get().load(currentAnime.getImageURL()).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
                    animeImageTasksQueue.add(runnable);
                    animeImageTasks.put(currentAnime.getName(), runnable);

                }
                Executor executor = Executors.newFixedThreadPool(5);
                for (Runnable job : animeImageTasks.values()) {
                    executor.execute(job);
                }
            }

            if (Objects.equals(currentAction, FanModel.POST_COMMENT)){

            }
            //Toast.makeText(getApplicationContext(), "Anime recommendations get success!", Toast.LENGTH_SHORT).show();


        }else{
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

}