package AnimEngine.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.text_link_forgot_password).setOnClickListener(this);
        findViewById(R.id.text_link_forgot_password).setOnClickListener(this);
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.text_link_sign_up).setOnClickListener(this);

        TextInputLayout mi = findViewById(R.id.text_input_email_login);
        editTextEmail = mi.getEditText();

        TextInputLayout pi = findViewById(R.id.text_input_password_login);
        editTextPassword = pi.getEditText();




    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.text_link_forgot_password)){
            BottomSheetDialog bsd = new BottomSheetDialog(this);
            bsd.setContentView(R.layout.bottom_sheet_forgot_password);
            bsd.show();
            Button submit = bsd.findViewById(R.id.button_submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(view == findViewById(R.id.button_login)){
            String mailInput = editTextEmail.getText().toString();
            Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)([_.-]{1}[a-zA-Z0-9]+)*@([a-zA-Z0-9])+(-{1}[a-zA-Z0-9]+)*(\\.([a-zA-Z]{2,}))+", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(mailInput);
            if(matcher.matches()){
                Toast.makeText(getApplicationContext(),"you ok",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
            }
        }

        if(view == findViewById(R.id.text_link_sign_up)){
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
            finish();
        }
    }
}