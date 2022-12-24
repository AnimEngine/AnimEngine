package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import AnimEngine.mobile.adapters.CatalogRVAdapter;
import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.models.dbAndStorageModel;

public class CatalogActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, Observer {

    ArrayList<Anime> arrayList;
    RecyclerView rv;

    dbAndStorageModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        arrayList = new ArrayList<>();


        model = new dbAndStorageModel();
        model.addObserver(this);
        model.getAllAnime();

        TextView tv_title = findViewById(R.id.catalog_tv);
        rv = findViewById(R.id.catalog_recycler);
        //BottomNavigationView bottomNavigation = findViewById(R.id.catalog_bottom_navigation);



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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        String result = this.model.getResult();

        if(Objects.equals(result, ""))
            return;

        if(result.startsWith("OK")){
            arrayList.addAll(this.model.getAnimeArrayList());

            CatalogRVAdapter customCatalogRVAdapter = new CatalogRVAdapter(CatalogActivity.this, getLayoutInflater(), arrayList);
            rv.setAdapter(customCatalogRVAdapter);
            rv.setLayoutManager(new GridLayoutManager(CatalogActivity.this,4));

        }else{
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}