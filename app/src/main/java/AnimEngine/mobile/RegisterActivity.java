package AnimEngine.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import AnimEngine.mobile.adapters.SectionsPagerAdapter;
import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.User;
import AnimEngine.mobile.models.UserModel;
import AnimEngine.mobile.util.CheckEmail;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Observer {

    UserModel model;

    HashMap<String, EditText> creatorEditTexts = new HashMap<>();
    HashMap<String, EditText> fanEditTexts = new HashMap<>();

    ViewPager2 myViewPager2;
    SectionsPagerAdapter myAdapter;

    Fragment creatorFragment, fanFragment;

    ProgressBar progressBar;
    Button submit;
    TextView pleaseWait;
    private static final int[] TAB_TITLES = new int[]{R.string.register_creator_fragment_label, R.string.register_fan_fragment_label};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        model = new UserModel(null, null);
        model.addObserver(this);

        myViewPager2 = findViewById(R.id.view_pager_register);
        myAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle());

        creatorFragment = new CreatorFragment(creatorEditTexts);
        fanFragment = new FanFragment(fanEditTexts);

        // add Fragments in your ViewPagerFragmentAdapter class
        myAdapter.addFragment(creatorFragment);
        myAdapter.addFragment(fanFragment);

        // set Orientation in your ViewPager2
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);

        TabLayout tabs = findViewById(R.id.tabs_register);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabs, myViewPager2,
                (tab, position) -> tab.setText(TAB_TITLES[position]));
        tabLayoutMediator.attach();

        findViewById(R.id.text_link_sign_in_creator).setOnClickListener(this);
        findViewById(R.id.button_sign_up_creator).setOnClickListener(this);

        progressBar = findViewById(R.id.progress_register);
        submit = findViewById(R.id.button_sign_up_creator);
        pleaseWait = findViewById(R.id.text_message_please_wait);
        stopLoadingAnimation();

    }

    public void startLoadingAnimation(){
        progressBar.setVisibility(View.VISIBLE);
        submit.setClickable(false);
        submit.setEnabled(false);
        submit.setVisibility(View.GONE);
        pleaseWait.setVisibility(View.VISIBLE);
    }

    public void stopLoadingAnimation(){
        progressBar.setVisibility(View.GONE);
        submit.setClickable(true);
        submit.setEnabled(true);
        submit.setVisibility(View.VISIBLE);
        pleaseWait.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.text_link_sign_in_creator)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if(view == findViewById(R.id.button_sign_up_creator)){
            boolean isCreator;

            HashMap<String, EditText> currentMap;
            CheckEmail emailChecker = new CheckEmail();
            String studioName = "", website = "";
            String fName = "", lName = "";

            switch (myViewPager2.getCurrentItem()){
                case 0:// Creator
                    currentMap = creatorEditTexts;
                    isCreator = true;

                    studioName = String.valueOf(creatorEditTexts.get("studioName").getText());
                    website = String.valueOf(creatorEditTexts.get("website").getText());

                    if(studioName.length()==0 || website.length()==0) {
                        Toast.makeText(getApplicationContext(), "Please make sure to fill all fields!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;
                case 1:// Fan
                    currentMap = fanEditTexts;
                    isCreator = false;

                    fName = String.valueOf(fanEditTexts.get("fName").getText());
                    lName = String.valueOf(fanEditTexts.get("lName").getText());

                    if(fName.length()==0 || lName.length()==0) {
                        Toast.makeText(getApplicationContext(), "Please make sure to fill all fields!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;
                default:
                    currentMap = fanEditTexts;
                    isCreator = false;
                    break;
            }

            String email = String.valueOf(currentMap.get("email").getText());
            String password = String.valueOf(currentMap.get("password").getText());

            if(!emailChecker.isValidEmail(email)) {
                Toast.makeText(getApplicationContext(), "Invalid Email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.length()<6) {
                Toast.makeText(getApplicationContext(), "Password has to be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }

            User user;
            if(isCreator)
                user = new Creator(email, password, "creator", studioName, website);
            else
                user = new Fan(email, password, "fan", fName, lName, new String[0], createGenresHashMap());

            this.model.register(user);

            startLoadingAnimation();
        }
    }

    private HashMap<String, Float> createGenresHashMap(){
        HashMap<String, Float> ret = new HashMap<>();
        String genresJson="";
        try {
            InputStream is = getApplicationContext().getAssets().open("genres.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            genresJson = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(genresJson);

            JSONArray genres = obj.getJSONArray("genres");

            for (int i = 0; i < genres.length(); i++) {
                String genre = (String) genres.get(i);
                ret.put(genre, 0.0f);
            }

            return ret;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        String result = this.model.getResult();
        if(result.startsWith("OK")){
            Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            stopLoadingAnimation();

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}