package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import AnimEngine.mobile.adapters.CatalogRVAdapter;
import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Comment;
import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.User;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.DBAndStorageModel;
import AnimEngine.mobile.models.FanModel;
import AnimEngine.mobile.util.InitialContext;
import AnimEngine.mobile.util.ModelLocator;

public class CatalogActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, Observer {

    ArrayList<Anime> arrayList;
    ArrayList<Anime> copyArraylist;

    ArrayList<Comment> commentsList;

    RecyclerView rv;
    CatalogRVAdapter catalogRVAdapter;

    ModelLocator modelLocator;
    DBAndStorageModel model;
    boolean isCreator;

    UserAndToken creator;
    UserAndToken fan;
    Creator creatorObj;
    ArrayList<Pair<String, String>> keyValuePairs;
    Fan fanObj;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        arrayList = new ArrayList<>();
        copyArraylist = new ArrayList<>();

        commentsList = new ArrayList<>();


        keyValuePairs = new ArrayList<>();

        modelLocator = ((MyApplication)getApplication()).getModelLocator();

        try {
            model = (DBAndStorageModel) modelLocator.getModel(InitialContext.DBSTORAGE, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addObserver(this);
        model.getAllAnime();

        TextView tv_title = findViewById(R.id.catalog_tv);


        //BottomNavigationView bottomNavigation = findViewById(R.id.catalog_bottom_navigation);

        BottomNavigationView bottomNavigationViewCreator = findViewById(R.id.bottom_navigation_creator_catalog);
        bottomNavigationViewCreator.setSelectedItemId(R.id.item_catalog_creator);
        bottomNavigationViewCreator.setOnItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_catalog);
        bottomNavigationView.setSelectedItemId(R.id.catalog_page);
        bottomNavigationView.setOnItemSelectedListener(this);

        searchView = findViewById(R.id.text_search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        creator = (UserAndToken) getIntent().getSerializableExtra("creator");
        UserAndToken toPass;
        if (creator != null) {
            isCreator = true;
            creatorObj = (Creator) creator.getUser();
            toPass = creator;

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
            toPass = fan;

            keyValuePairs.add(new Pair<>("Email: ", fan.getUser().getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", fan.getUser().getPassword()));

            keyValuePairs.add(new Pair<>("First Name: ", fanObj.getFName()));
            keyValuePairs.add(new Pair<>("Last Name: ", fanObj.getLName()));
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

        rv = findViewById(R.id.catalog_recycler);
        catalogRVAdapter = new CatalogRVAdapter(this, getLayoutInflater(), arrayList, isCreator, toPass);
        rv.setAdapter(catalogRVAdapter);
        rv.setLayoutManager(new GridLayoutManager(this, 4));


    }

//    @Override
//    public void onClick(View view) {
//        if(view == findViewById(R.id.button_search_anime)){
//            String query = search_dialog.getText().toString();
//            arrayList.sort(new Comparator<Anime>() {
//                @Override
//                public int compare(Anime o1, Anime o2) {
//                    return calcScore(o1, query) - calcScore(o2, query);
//                }
//            });
//        }
//    }

//    private int calcScore(Anime anime, String query){
//        String animeName = anime.getName();
//        return Math.abs(animeName.compareTo(query));
//    }

    public static int calcScore(Anime anime, String str2) {
        String str1 = anime.getName();

        // Set the initial length of the prefix to 0
        int prefixLength = 0;
        // Set the minimum length of the strings to the length of the shorter string
        int minLength = Math.min(str1.length(), str2.length());
        // Iterate through the characters of both strings
        for (int i = 0; i < minLength; i++) {
            // If the characters at the current index are not equal, break out of the loop
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
            // Otherwise, increment the prefix length
            prefixLength++;
        }
        // Return the prefix length
        return prefixLength;
    }

    public boolean showNavigationBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    @Override
    public void update(Observable o, Object arg) {
        String result = this.model.getResult();
        String action = this.model.getAction();

        if (Objects.equals(result, ""))
            return;

        if (result.startsWith("OK")) {
            if(Objects.equals(action, DBAndStorageModel.GET_ALL)) {
                arrayList.addAll(this.model.getGetAllAnimeResult());

                catalogRVAdapter.notifyDataSetChanged();

                this.model.getAllComments();
            }

            if(Objects.equals(action, DBAndStorageModel.GET_ALL_COMMENTS)){

            }

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        copyArraylist.clear();
        copyArraylist.addAll(arrayList);


        arrayList.sort(new Comparator<Anime>() {
                @Override
                public int compare(Anime o1, Anime o2) {
                    return calcScore(o2, query) - calcScore(o1, query);
                }
            });

            catalogRVAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onClose() {
        searchView.setQuery("", false);

        arrayList.clear();
        arrayList.addAll(copyArraylist);

        catalogRVAdapter.notifyDataSetChanged();
        searchView.clearFocus();
        return true;
    }
}