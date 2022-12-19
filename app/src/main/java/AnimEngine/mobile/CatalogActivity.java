package AnimEngine.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;


import java.util.ArrayList;

import AnimEngine.mobile.Anime;

public class CatalogActivity extends AppCompatActivity {

    ArrayList<Anime> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        TextView tv_title = findViewById(R.id.catalog_tv);
        RecyclerView rv = findViewById(R.id.catalog_recycler);
        //BottomNavigationView bottomNavigation = findViewById(R.id.catalog_bottom_navigation);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

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

        CatalogRVAdapter customManufacturersRVAdapter = new CatalogRVAdapter(CatalogActivity.this, getLayoutInflater(), arrayList);
        rv.setAdapter(customManufacturersRVAdapter);
        rv.setLayoutManager(new GridLayoutManager(CatalogActivity.this,4));


        int screenHeight = displayMetrics.heightPixels + getNavigationBarHeight();
        //rv.setMinimumHeight(screenHeight-tv_title.getHeight());
    }

    private int getNavigationBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight)
            return realHeight - usableHeight;
        else
            return 0;
    }

    public boolean showNavigationBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }
}