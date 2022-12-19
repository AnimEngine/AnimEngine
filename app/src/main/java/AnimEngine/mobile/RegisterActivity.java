package AnimEngine.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AnimEngine.mobile.util.CheckEmail;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.button_sign_up).setOnClickListener(this);
        findViewById(R.id.text_link_sign_in).setOnClickListener(this);

        TextInputLayout fNameInput = findViewById(R.id.text_input_first_name_sign_up);
        editTextFirstName = fNameInput.getEditText();

        TextInputLayout lNameInput = findViewById(R.id.text_input_last_name_sign_up);
        editTextLastName = lNameInput.getEditText();

        TextInputLayout mailInput = findViewById(R.id.text_input_email_sign_up);
        editTextEmail = mailInput.getEditText();

        TextInputLayout passwordInput = findViewById(R.id.text_input_password_sign_up);
        editTextPassword = passwordInput.getEditText();

    }





    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.button_sign_up)) {
            String email = String.valueOf(editTextEmail.getText());
            CheckEmail emailChecker = new CheckEmail();

            if(!emailChecker.isValidEmail(email))
                Toast.makeText(getApplicationContext(),"Invalid Email!",Toast.LENGTH_SHORT).show();

            String password = String.valueOf(editTextPassword.getText());
            if(password.length()<6)
                Toast.makeText(getApplicationContext(),"Password has to be at least 6 characters long!",Toast.LENGTH_SHORT).show();

            String fName = String.valueOf(editTextFirstName.getText());
            String lName = String.valueOf(editTextLastName.getText());
            if(fName.length()==0 || lName.length()==0)
                Toast.makeText(getApplicationContext(),"Please make sure to fill all fields!",Toast.LENGTH_SHORT).show();


        }

        if(view == findViewById(R.id.text_link_sign_in)) {
            Intent ActivitysignIn = new Intent(this, LoginActivity.class);
            startActivity(ActivitysignIn);
            finish();
        }
    }
}