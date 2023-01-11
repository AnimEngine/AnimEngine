package AnimEngine.mobile;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
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

import AnimEngine.mobile.adapters.CatalogRVAdapter;
import AnimEngine.mobile.adapters.ProfileRVAdapter;
import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.CreatorModel;
import AnimEngine.mobile.models.DBAndStorageModel;
import AnimEngine.mobile.models.FanModel;
import AnimEngine.mobile.util.InitialContext;
import AnimEngine.mobile.util.ModelLocator;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, Observer {

    boolean isCreator;
    UserAndToken creator;
    Creator creatorObj;

    UserAndToken fan;
    Fan fanObj;

    ModelLocator modelLocator;
    CreatorModel model;

    ArrayList<Anime> animeList;
    RecyclerView animeRecyclerView;
    CatalogRVAdapter customCatalogRVAdapter;

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
        animeList = new ArrayList<>();

        modelLocator = ((MyApplication)getApplication()).getModelLocator();

        try {
            model = (CreatorModel) modelLocator.getModel(InitialContext.CREATOR, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addObserver(this);

        creator = (UserAndToken) getIntent().getSerializableExtra("creator");
        if (creator != null) {
            isCreator = true;
            creatorObj = (Creator) creator.getUser();
            keyValuePairs.add(new Pair<>("Email: ", creator.getUser().getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", creator.getUser().getPassword()));

            keyValuePairs.add(new Pair<>("Studio Name: ", creatorObj.getStudioName()));
            keyValuePairs.add(new Pair<>("Website: ", creatorObj.getWebAddress()));

            this.model.getAllAnimeOfCreator(creator.getToken());
            animeRecyclerView = findViewById(R.id.recycler_profile_creator_anime);
            animeRecyclerView.setVisibility(View.VISIBLE);

            customCatalogRVAdapter = new CatalogRVAdapter(this, getLayoutInflater(), animeList, true, creator);
            animeRecyclerView.setAdapter(customCatalogRVAdapter);
            animeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

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

            if(password.length()<6) {
                Toast.makeText(getApplicationContext(), "Password has to be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }

                String field1 = adapter.getmFields().get(2).second;
                String field2 = adapter.getmFields().get(3).second;
            if(isCreator) {
                Creator newCreator = new Creator(email, password, "creator", field1, field2);
                model.editUser(creator.getToken(), "creator", newCreator);

            }else{
                fanObj.setFName(field1);
                fanObj.setLName(field2);

                model.editUser(fan.getToken(), "fan", fanObj);
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
        String action = this.model.getAction();

        if(Objects.equals(result, ""))
            return;

        if(result.startsWith("OK")){
            if(Objects.equals(action, CreatorModel.EDIT_USER)) {
                Toast.makeText(getApplicationContext(), "User updated Successfully!", Toast.LENGTH_SHORT).show();
                adapter.setEdited(false);
                hideActionButtons();
            }

            if(Objects.equals(action, CreatorModel.GET_ALL_ANIME_OF_CREATOR)){
                animeList.clear();
                animeList.addAll(model.getGetAllAnimeOfCreatorResult());

                customCatalogRVAdapter.notifyDataSetChanged();
            }

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