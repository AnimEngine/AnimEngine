package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import AnimEngine.mobile.utils.DB;

public class change_password extends AppCompatActivity implements View.OnClickListener {
    Button change;
    EditText mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        change= (Button) findViewById(R.id.reset_btn);
        mail= (EditText) findViewById(R.id.reset_mail);
        change.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        FirebaseAuth myAuth= DB.getAU();
        String email=mail.getText().toString().trim();
        myAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(change_password.this,"Reset link sent to your email.",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),home_screen.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(change_password.this,"Error, couldn't send a link.",Toast.LENGTH_LONG).show();
            }
        });
    }
}