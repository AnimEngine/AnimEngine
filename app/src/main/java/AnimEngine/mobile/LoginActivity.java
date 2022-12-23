package AnimEngine.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.userModel;
import AnimEngine.mobile.util.CheckEmail;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Observer {

    userModel model;
    UserAndToken creator;
    UserAndToken fan;

    EditText editTextEmail, editTextPassword;

    ProgressBar progressBar;
    Button submit;
    TextView pleaseWait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.creator = new UserAndToken();
        this.fan = new UserAndToken();

        model = new userModel(creator, fan);
        model.addObserver(this);

        findViewById(R.id.text_link_forgot_password).setOnClickListener(this);
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.text_link_sign_up).setOnClickListener(this);

        TextInputLayout mi = findViewById(R.id.text_input_email_login);
        editTextEmail = mi.getEditText();

        TextInputLayout pi = findViewById(R.id.text_input_password_login);
        editTextPassword = pi.getEditText();

        progressBar = findViewById(R.id.progress_login);
        submit = findViewById(R.id.button_login);
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
        if(view == findViewById(R.id.text_link_forgot_password)){
            BottomSheetDialog bsd = new BottomSheetDialog(this);
            bsd.setContentView(R.layout.bottom_sheet_forgot_password);
            bsd.show();
            Button submit = bsd.findViewById(R.id.button_submit);
            assert submit != null;
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(view == findViewById(R.id.button_login)){
            String email = editTextEmail.getText().toString();
            CheckEmail emailChecker = new CheckEmail();

            if(!emailChecker.isValidEmail(email)){
                Toast.makeText(getApplicationContext(),"Invalid Email!",Toast.LENGTH_SHORT).show();
                return;
            }

            String password = editTextPassword.getText().toString();
            this.model.login(email, password);
            startLoadingAnimation();
        }

        if(view == findViewById(R.id.text_link_sign_up)){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        String result = this.model.getResult();
        if(Objects.equals(result, ""))
            return;
        if(result.startsWith("OK")){
            Toast.makeText(getApplicationContext(), "Logged in Successfully!", Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
        }else{
            //stopLoadingAnimation();

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}