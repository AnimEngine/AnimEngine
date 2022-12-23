package AnimEngine.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;


import java.util.ArrayList;

import AnimEngine.mobile.adapters.CatalogRVAdapter;
import AnimEngine.mobile.classes.Anime;

public class CatalogActivity extends AppCompatActivity {

    ArrayList<Anime> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        TextView tv_title = findViewById(R.id.catalog_tv);
        RecyclerView rv = findViewById(R.id.catalog_recycler);
        //BottomNavigationView bottomNavigation = findViewById(R.id.catalog_bottom_navigation);

        arrayList = new ArrayList<>();

        arrayList.add(new Anime("Erased"));
        arrayList.add(new Anime("Another"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));
        arrayList.add(new Anime("Dragon Ball"));

        CatalogRVAdapter customCatalogRVAdapter = new CatalogRVAdapter(CatalogActivity.this, getLayoutInflater(), arrayList);
        rv.setAdapter(customCatalogRVAdapter);
        rv.setLayoutManager(new GridLayoutManager(CatalogActivity.this,4));
    }

    public boolean showNavigationBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }
}