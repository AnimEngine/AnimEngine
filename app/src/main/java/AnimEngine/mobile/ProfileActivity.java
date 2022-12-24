package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import AnimEngine.mobile.adapters.ProfileRVAdapter;
import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    boolean isCreator;
    UserAndToken creator;
    Creator creatorObj;

    UserAndToken fan;
    Fan fanObj;

    RecyclerView recyclerView;
    ArrayList<Pair<String, String>> keyValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        keyValuePairs = new ArrayList<>();

        creator = (UserAndToken) getIntent().getSerializableExtra("creator");
        if (creator != null) {
            isCreator = true;
            creatorObj = (Creator) creator.getUser();

            keyValuePairs.add(new Pair<>("Email: ", creator.getUser().getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", creator.getUser().getPassword()));

            keyValuePairs.add(new Pair<>("Studio Name: ", creatorObj.getStudioName()));
            keyValuePairs.add(new Pair<>("Website: ", creatorObj.getWebAddress()));
        }
        else {
            fan = (UserAndToken) getIntent().getSerializableExtra("fan");
            isCreator = false;
            fanObj = (Fan) fan.getUser();

            keyValuePairs.add(new Pair<>("Email: ", fan.getUser().getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", fan.getUser().getPassword()));

            keyValuePairs.add(new Pair<>("First Name: ", fanObj.getFName()));
            keyValuePairs.add(new Pair<>("Last Name: ", fanObj.getLName()));
        }


        recyclerView = findViewById(R.id.recycler_profile_creator);

        ProfileRVAdapter customProfileRVAdapter = new ProfileRVAdapter(ProfileActivity.this, getLayoutInflater(), keyValuePairs);
        recyclerView.setAdapter(customProfileRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.item_profile_client)) {

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item){
//
//        }

        return false;
    }
}