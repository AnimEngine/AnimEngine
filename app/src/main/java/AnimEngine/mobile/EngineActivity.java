package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import AnimEngine.mobile.adapters.SectionsPagerAdapter;
import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Comment;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.dbAndStorageModel;

public class EngineActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, Observer {

    UserAndToken fan;
    Fan fanObj;

    dbAndStorageModel model;

    BottomNavigationView bottomNavigationView;
    ArrayList<Anime> animeArrayList;

    RatingBar RatingBarAnime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine);

        model = new dbAndStorageModel();
        model.addObserver(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_page);

        bottomNavigationView.setOnItemSelectedListener(this);

        animeArrayList = new ArrayList<>();

        fan = (UserAndToken) getIntent().getSerializableExtra("fan");
        fanObj = (Fan) fan.getUser();

        findViewById(R.id.button_show_information).setOnClickListener(this);

        model.getBestKAnime(fan.getToken());

        findViewById(R.id.button_add_comment).setOnClickListener(this);

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
//        if(view == findViewById(R.id.button_add_comment)){
//
//            setContentView(R.layout.activity_add_comment_fan);
//
//            final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.rate_anime_stars);
//            Button submitButton = (Button) findViewById(R.id.button_send_comment);
//            // perform click event on button
//            submitButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // get values and then displayed in a toast
//                    String animeName = ;
//                    String content;
//                    float stars = simpleRatingBar.getRating();;
//
//                    String totalStars = "Total Stars:: " + simpleRatingBar.getNumStars();
//                    String rating = "Rating :: " + stars;
//                    Toast.makeText(getApplicationContext(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();
//
//
//
//                    Comment comment = new Comment()
//                    uploadComment()
//
//
//                }
//            });
//
//
//        }

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
            Toast.makeText(getApplicationContext(), "Anime recommendations get success!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}