package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.text_link_sign_in_creator).setOnClickListener(this);
        findViewById(R.id.text_top_navigation_type).setOnClickListener(this);

    }





    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.text_link_sign_in_creator)) {
            Intent ActivitysignIn = new Intent(this, LoginActivity.class);
            startActivity(ActivitysignIn);
            finish();
        }

        if(view == findViewById(R.id.text_top_navigation_type)){


        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Toast.makeText(getApplicationContext(),item.getItemId(),Toast.LENGTH_SHORT).show();

        return false;
    }
}