package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import AnimEngine.mobile.adapters.CatalogRVAdapter;
import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.DBAndStorageModel;

public class CatalogActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, Observer {

    ArrayList<Anime> arrayList;
    RecyclerView rv;

    DBAndStorageModel model;
    boolean isCreator;

    UserAndToken creator;
    UserAndToken fan;
    Creator creatorObj;
    ArrayList<Pair<String, String>> keyValuePairs;
    Fan fanObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        arrayList = new ArrayList<>();

        keyValuePairs = new ArrayList<>();

        model = new DBAndStorageModel();
        model.addObserver(this);
        model.getAllAnime();

        TextView tv_title = findViewById(R.id.catalog_tv);
        rv = findViewById(R.id.catalog_recycler);
        //BottomNavigationView bottomNavigation = findViewById(R.id.catalog_bottom_navigation);

        BottomNavigationView bottomNavigationViewCreator = findViewById(R.id.bottom_navigation_creator_catalog);
        bottomNavigationViewCreator.setSelectedItemId(R.id.item_catalog_creator);
        bottomNavigationViewCreator.setOnItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_catalog);
        bottomNavigationView.setSelectedItemId(R.id.catalog_page);
        bottomNavigationView.setOnItemSelectedListener(this);

        creator = (UserAndToken) getIntent().getSerializableExtra("creator");
        if (creator != null) {
            isCreator = true;
            creatorObj = (Creator) creator.getUser();

            keyValuePairs.add(new Pair<>("Email: ", creatorObj.getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", creatorObj.getPassword()));

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


//        arrayList.add(new Anime("Erased"));
//        arrayList.add(new Anime("Another"));
//        arrayList.add(new Anime("Dragon Ball"));
//
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));
//        arrayList.add(new Anime("Dragon Ball"));


    }

    public boolean showNavigationBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    @Override
    public void update(Observable o, Object arg) {
        String result = this.model.getResult();

        if (Objects.equals(result, ""))
            return;

        if (result.startsWith("OK")) {
            arrayList.addAll(this.model.getAnimeArrayList());

            CatalogRVAdapter customCatalogRVAdapter = new CatalogRVAdapter(CatalogActivity.this, getLayoutInflater(), arrayList);
            rv.setAdapter(customCatalogRVAdapter);
            rv.setLayoutManager(new GridLayoutManager(CatalogActivity.this, 4));

        } else {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (isCreator) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.item_profile_creator:
                    intent = new Intent(this, ProfileActivity.class);
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
        } else {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.profile_page:
                    intent = new Intent(this, ProfileActivity.class);
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
}