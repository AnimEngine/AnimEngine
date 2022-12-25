package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.DBAndStorageModel;
import AnimEngine.mobile.util.RunnableWithStatus;

public class EngineActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, Observer {

    TextView textViewAnimeName;
    ImageView imageViewAnimeImage;

    UserAndToken fan;
    Fan fanObj;

    DBAndStorageModel model;

    BottomNavigationView bottomNavigationView;
    Queue<Anime> animeQueue;
    Queue<RunnableWithStatus<Bitmap>> animeImageTasksQueue;
    HashMap<String, RunnableWithStatus<Bitmap>> animeImageTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine);

        textViewAnimeName = findViewById(R.id.text_view_anime_name_engine);
        imageViewAnimeImage = findViewById(R.id.image_anime_engine);

        model = new DBAndStorageModel();
        model.addObserver(this);

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

        model.getBestKAnime(fan.getToken());

    }

    private void displayNextAnime(){
        if(animeQueue.isEmpty()){
            Toast.makeText(getApplicationContext(),"No more anime's left to recommend!",Toast.LENGTH_SHORT).show();
        }
        else{
            Anime currentAnime = animeQueue.poll();

            if(animeImageTasksQueue.peek().isFinished()) {
                Bitmap currentImage = Objects.requireNonNull(animeImageTasks.get(currentAnime.getName()).getRet());
                imageViewAnimeImage.setImageBitmap(currentImage);
            }

            textViewAnimeName.setText(currentAnime.getName());

        }
    }
    private void displayFirstAnime(Anime anime){
        Picasso.get().load(anime.getImageURL()).into(imageViewAnimeImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
        textViewAnimeName.setText(anime.getName());
    }

    @Override
    public void onClick(View view){
        if(view == findViewById(R.id.button_show_information)){
            // Create the object of AlertDialog Builder class
            AlertDialog.Builder builder = new AlertDialog.Builder(EngineActivity.this);

            // Set Alert Title
            builder.setTitle("Description");

            // Set the message show for the Alert time
            builder.setMessage("Several hundred years ago, humans were nearly exterminated by giants. Giants are typically several stories tall, seem to have no intelligence, devour human beings and, worst of all, seem to do it for the pleasure rather than as a food source. A small percentage of humanity survived by walling themselves in a city protected by extremely high walls, even taller than the biggest of giants. Flash forward to the present and the city has not seen a giant in over 100 years. Teenage boy Eren and his foster sister Mikasa witness something horrific as the city walls are destroyed by a super giant that appears out of thin air. As the smaller giants flood the city, the two kids watch in horror as their mother is eaten alive. Eren vows that he will murder every single giant and take revenge for all of mankind.");


            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false);

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("close", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
        }

        if(view == findViewById(R.id.button_not_watch_later_anime)){
            displayNextAnime();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int curr = item.getItemId();

        if (R.id.profile_page == curr) {
            intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("fan", fan);
            startActivity(intent);
            finish();

            return true;
        }

        if (R.id.catalog_page == curr) {
            intent = new Intent(this, CatalogActivity.class);
            intent.putExtra("fan", fan);
            startActivity(intent);
            finish();

            return true;
        }


        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        String result = this.model.getResult();


        if(Objects.equals(result, ""))
            return;

        if(result.startsWith("OK")){
            String currentAction = model.getAction();

            if (Objects.equals(currentAction, DBAndStorageModel.GET_BEST_K)) {
                animeQueue.clear();
                animeImageTasksQueue.clear();

                List<String> animeNames = model.getGetBestKAnimeResult();
                HashMap<String, Anime> animeObjects = model.getGetAnimeResult();
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


                //displayNextAnime();
            }

            //Toast.makeText(getApplicationContext(), "Anime recommendations get success!", Toast.LENGTH_SHORT).show();


        }else{
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

}