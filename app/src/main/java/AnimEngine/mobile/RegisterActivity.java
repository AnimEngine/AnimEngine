package AnimEngine.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.text_link_sign_in).setOnClickListener(this);

    }





    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.text_link_sign_in)) {
            Intent ActivitysignIn = new Intent(this, LoginActivity.class);
            startActivity(ActivitysignIn);
            finish();
        }
    }
}