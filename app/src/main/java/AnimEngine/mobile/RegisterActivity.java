package AnimEngine.mobile;

import static java.lang.System.out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.User;
import AnimEngine.mobile.util.CheckEmail;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private FirebaseFunctions mFunctions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.mFunctions = FirebaseFunctions.getInstance();

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

            if(!emailChecker.isValidEmail(email)) {
                Toast.makeText(getApplicationContext(), "Invalid Email!", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = String.valueOf(editTextPassword.getText());
            if(password.length()<6) {
                Toast.makeText(getApplicationContext(), "Password has to be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }
            String fName = String.valueOf(editTextFirstName.getText());
            String lName = String.valueOf(editTextLastName.getText());
            if(fName.length()==0 || lName.length()==0) {
                Toast.makeText(getApplicationContext(), "Please make sure to fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                register(new Fan(email, password,"fan", fName,lName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //
        }

        if(view == findViewById(R.id.text_link_sign_in)) {
            Intent ActivitysignIn = new Intent(this, LoginActivity.class);
            startActivity(ActivitysignIn);
            finish();
        }
    }

    private void register(User user) throws IOException {
        // Create the arguments to the callable function.
        Gson gson = new Gson();
        String jsonToSend = gson.toJson(user);

        String URL = "https://us-central1-animengine-fb858.cloudfunctions.net/register";

        this.mFunctions
                .getHttpsCallableFromUrl(new URL(URL))
                .call(jsonToSend).addOnCompleteListener(task -> {
                    HashMap map = (HashMap) task.getResult().getData();
                    if(map == null){
                        Toast.makeText(getApplicationContext(),"Registering Failed, Try again!",Toast.LENGTH_SHORT).show();
                    }else {
                        if (map.containsKey("ok")) {
                            Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), (String) map.get("error"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                URL,
//                json,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        // Handle the response
//
//                    }
//                },
//
//                new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        // Handle the error
//
//                    }
//                });

//        try {
//            URL url = new URL("https://us-central1-animengine-fb858.cloudfunctions.net/register"); //in the real code, there is an ip and a port
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Accept","application/json");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//
//
//
//            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//            os.writeBytes(jsonToSend);
//
//            os.flush();
//            os.close();
//            conn.connect();
//            Toast.makeText(getApplicationContext(),conn.getResponseMessage(),Toast.LENGTH_SHORT).show();
//
//            conn.disconnect();
//        } catch (Exception e) {
//
//        }

//        OutputStream out = null;
//        try {
//            URL url = new URL("https://us-central1-animengine-fb858.cloudfunctions.net/register");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type", "application/json");
//            out = new BufferedOutputStream(urlConnection.getOutputStream());
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
//            writer.write(jsonToSend);
//            writer.flush();
//            writer.close();
//            out.close();
//
//            urlConnection.connect();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }


//
    }
}