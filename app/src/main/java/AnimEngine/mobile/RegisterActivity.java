package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import AnimEngine.mobile.adapters.SectionsPagerAdapter;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    ViewPager2 myViewPager2;
    SectionsPagerAdapter myAdapter;

    Fragment creatorFragment, fanFragment;

    ArrayList<EditText> creatorEditTexts = new ArrayList<>();
    ArrayList<EditText> fanEditTexts = new ArrayList<>();

    private static final int[] TAB_TITLES = new int[]{R.string.register_creator_fragment_label, R.string.register_fan_fragment_label};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.text_link_sign_in_creator)) {
            Intent ActivitysignIn = new Intent(this, LoginActivity.class);
            startActivity(ActivitysignIn);
            finish();
        }

        if(view == findViewById(R.id.button_sign_up_creator)){
            switch (myViewPager2.getCurrentItem()){
                case 0:// Creator
                    String studioNameInput = String.valueOf(creatorEditTexts.get(0).getText());
                    String websiteInput = String.valueOf(creatorEditTexts.get(1).getText());
                    String creatorEmailInput = String.valueOf(creatorEditTexts.get(2).getText());
                    String creatorPasswordInput = String.valueOf(creatorEditTexts.get(3).getText());

                    Toast.makeText(getApplicationContext(),studioNameInput+ " "+websiteInput,Toast.LENGTH_SHORT).show();
                    break;
                case 1:// Fan
                    String fNameInput = String.valueOf(fanEditTexts.get(0).getText());
                    String lNameInput = String.valueOf(fanEditTexts.get(1).getText());
                    String fanEmailInput = String.valueOf(fanEditTexts.get(2).getText());
                    String fanPasswordInput = String.valueOf(fanEditTexts.get(3).getText());
                    break;
                default:
                    break;
            }
        }


    }
}