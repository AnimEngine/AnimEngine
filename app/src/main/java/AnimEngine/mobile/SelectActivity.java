package AnimEngine.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import AnimEngine.mobile.utils.DB;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {

    final private String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice of Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
    ImageView img;
    ImageButton like, dislike;
    TextView anime_name;
    final private String[] anime = {"Naruto", "One Piece", "Bleach", "Dragon Ball", "Hunter x Hunter"};
    final private String[] genres = {"Adventure Fantasy Comedy Martial-arts", "Adventure Fantasy", "Adventure Supernatural",
            "Adventure Fantasy Martial-arts", "Adventure Fantasy Martial-arts"};
    final private String[] URLs = {"https://www.giantbomb.com/a/uploads/original/3/33873/1700999-naruto.png",
            "https://i.ytimg.com/vi/ha0-qytMD9k/maxresdefault.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxtWN2AF8vQ193DFg-UKViqXeu_SfHHF-AyQ&usqp=CAU",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQARjrRsYHUpOzVKsPyYX_uFo6wp10M1-GgIg&usqp=CAU",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-YxGQ0w4EZBuurlwmOQazoc7-EuoAmbMzPQ&usqp=CAU"};
    Map<String, Integer> likes;
    int index;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        index = 0;
        likes = new HashMap<>();
        for (int i = 0; i < Gen.length; i++) {
            likes.put(Gen[i], 0);
        }
        Bundle extra = getIntent().getExtras();
        uid=extra.getString("uid");
        img = (ImageView) findViewById(R.id.imageID);
        like = (ImageButton) findViewById(R.id.ibLike);
        dislike = (ImageButton) findViewById(R.id.ibUnLike);
        anime_name = (TextView) findViewById(R.id.animeNameSeries);
        Glide.with(this).load(URLs[index]).into(img);
        anime_name.setText(anime[index]);
        like.setOnClickListener(this);
        dislike.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == like.getId()) {
            String[] parsed = genres[index].split(" ");
            for (int i = 0; i < parsed.length; i++) {
                likes.put(parsed[i], likes.get(parsed[i]) + 1);
            }
        }
        index++;
        if (index>=anime.length){
            DatabaseReference myRef= DB.getDB().getReference("Likes").child(uid);
            myRef.setValue(likes);
            Toast.makeText(SelectActivity.this,"Updated.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),home_screen.class));
        }
        Glide.with(this).load(URLs[index]).into(img);
        anime_name.setText(anime[index]);
    }
}