package AnimEngine.mobile;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import AnimEngine.mobile.adapters.ProfileRVAdapter;
import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.CreatorModel;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, Observer {

    boolean isCreator;
    UserAndToken creator;
    Creator creatorObj;

    CreatorModel model;

    UserAndToken fan;
    Fan fanObj;

    RecyclerView userFieldsRecyclerView;
    ProfileRVAdapter adapter;
    ArrayList<Pair<String, String>> keyValuePairs;

    LinearLayout buttonCancelCommitPair;
    Button commit, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        commit = findViewById(R.id.button_commit);
        cancel = findViewById(R.id.button_cancel);
        buttonCancelCommitPair = findViewById(R.id.button_cancel_commit_pair);
        buttonCancelCommitPair.setVisibility(GONE);

        commit.setOnClickListener(this);
        cancel.setOnClickListener(this);


        BottomNavigationView bottomNavigationViewCreator = findViewById(R.id.bottom_navigation_creator);
        bottomNavigationViewCreator.setSelectedItemId(R.id.item_profile_creator);
        bottomNavigationViewCreator.setOnItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile_page);
        bottomNavigationView.setOnItemSelectedListener(this);

        findViewById(R.id.button_logout_account).setOnClickListener(this);


        keyValuePairs = new ArrayList<>();

        model = new CreatorModel();
        model.addObserver(this);

        creator = (UserAndToken) getIntent().getSerializableExtra("creator");
        if (creator != null) {
            isCreator = true;
            creatorObj = (Creator) creator.getUser();
            keyValuePairs.add(new Pair<>("Email: ", creator.getUser().getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", creator.getUser().getPassword()));

            keyValuePairs.add(new Pair<>("Studio Name: ", creatorObj.getStudioName()));
            keyValuePairs.add(new Pair<>("Website: ", creatorObj.getWebAddress()));

            bottomNavigationViewCreator.setVisibility(View.VISIBLE);
        }
        else {
            fan = (UserAndToken) getIntent().getSerializableExtra( "fan");
            isCreator = false;
            fanObj = (Fan) fan.getUser();

            keyValuePairs.add(new Pair<>("Email: ", fan.getUser().getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", fan.getUser().getPassword()));

            keyValuePairs.add(new Pair<>("First Name: ", fanObj.getFName()));
            keyValuePairs.add(new Pair<>("Last Name: ", fanObj.getLName()));

            bottomNavigationView.setVisibility(View.VISIBLE);
        }


        userFieldsRecyclerView = findViewById(R.id.recycler_profile);

        adapter =
                new ProfileRVAdapter(ProfileActivity.this, getLayoutInflater(), keyValuePairs, this::showActionButtons);
        userFieldsRecyclerView.setAdapter(adapter);
        userFieldsRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.button_commit)) {
            String email = adapter.getmFields().get(0).second;
            String password = adapter.getmFields().get(1).second;




                String field1 = adapter.getmFields().get(2).second;
                String field2 = adapter.getmFields().get(3).second;
            if(isCreator) {
                Creator newCreator = new Creator(email, password, "creator", field1, field2);
                model.editUser(creator.getToken(), "creator", newCreator);
            }else{
                Fan newFan = new Fan(email, password, "fan", field1, field2, fanObj.getBlacklist(), fanObj.getGenres());
                model.editUser(fan.getToken(), "fan", newFan);
            }



        }

        if(view == findViewById(R.id.button_cancel)) {
            hideActionButtons();
        }

        if(view == findViewById(R.id.button_logout_account)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(isCreator){
            Intent intent;
            switch (item.getItemId()) {

                case R.id.item_catalog_creator:
                    intent = new Intent(this, CatalogActivity.class);
                    intent.putExtra("creator", creator);
                    startActivity(intent);
                    finish();

                    return true;

                case R.id.item_add_creator:
                    intent = new Intent(this, CreateActivity.class);
                    intent.putExtra("creator", creator);
                    startActivity(intent);
                    finish();

                    return true;

            }
            return false;
        }
        else{
            Intent intent;
            switch (item.getItemId()) {

                case R.id.catalog_page:
                    intent = new Intent(this, CatalogActivity.class);
                    intent.putExtra("fan", fan);
                    startActivity(intent);
                    finish();

                    return true;

                case R.id.home_page:
                    intent = new Intent(this, EngineActivity.class);
                    intent.putExtra("fan", fan);
                    startActivity(intent);
                    finish();

                    return true;

            }
            return false;
        }


    }

    @Override
    public void update(Observable o, Object arg) {
        String result = this.model.getResult();

        if(Objects.equals(result, ""))
            return;

        if(result.startsWith("OK")){
            Toast.makeText(getApplicationContext(), "User updated Successfully!", Toast.LENGTH_SHORT).show();
            adapter.setEdited(false);
            hideActionButtons();

        }else {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    public void showActionButtons(){
        buttonCancelCommitPair.setVisibility(View.VISIBLE);
    }

    public void hideActionButtons(){
        buttonCancelCommitPair.setVisibility(GONE);
        if(adapter.isEdited())
            adapter.rollback();
        adapter.setEdited(false);
    }


}