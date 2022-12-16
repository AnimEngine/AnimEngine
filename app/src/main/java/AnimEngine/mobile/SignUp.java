package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import AnimEngine.mobile.utils.DB;
import AnimEngine.mobile.utils.User;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase root;
    DatabaseReference myRef;
    Button signup;
    EditText etNickname, etPassword, etEmail, etUserName;
    TextView  back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup = (Button) findViewById(R.id.btnSignUp);
        back = (TextView) findViewById(R.id.tvSignIn);
        etNickname = (EditText) findViewById(R.id.etNickname);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etUserName = findViewById(R.id.etUserName);
//        root= DB.getDB();
        signup.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==signup.getId()){
            String uname = etUserName.getText().toString();
            String nick = etNickname.getText().toString();
            String pass = etPassword.getText().toString();
            String email = etEmail.getText().toString();
            if (uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty()) {
                Toast.makeText(SignUp.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth myAuth = DB.getAU();
                myAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            User user = new User(uname, email, pass, nick, false, uid);
                            user.InsertUser();
                            Toast.makeText(SignUp.this, "Welcome to AnimEngine, " + uname, Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(), SelectActivity.class);
                            intent.putExtra("uid",uid);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SignUp.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        }else{
            startActivity(new Intent(getApplicationContext(),home_screen.class));
        }

    }
//
//            String uname=name.getText().toString();
//            String nick=nickname.getText().toString();
//            String pass=password.getText().toString();
//            String email=mail.getText().toString();
//            if(uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty())
//            {
//                Toast.makeText(SignUp.this,"Please fill all the fields!",Toast.LENGTH_SHORT).show();
//            }
//            else if(DB.validator(nickname.getText().toString(),true)){
//                Log.d("key2",DB.validator(nickname.getText().toString(),true)+"");
//                User user=new User(uname, email,  pass,  nick);
//                user.InsertUser();
//                Toast.makeText(SignUp.this,"Welcome to AnimEngine, "+nick,Toast.LENGTH_LONG).show();
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Intent intent=new Intent(getApplicationContext(),home_screen.class);
//                startActivity(intent);
//            }else{
//                Log.d("ok","ok");
//                Toast.makeText(SignUp.this,"Username already taken!",Toast.LENGTH_SHORT).show();
//            }
//        }
//        DatabaseReference r = DB.getDB().getReference("Users").child("clients").child(nickname.getText().toString());
//        r.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(!snapshot.exists()){
//                    String uname=name.getText().toString();
//                    String nick=nickname.getText().toString();
//                    String pass=password.getText().toString();
//                    String email=mail.getText().toString();
//                    if(uname.isEmpty() || nick.isEmpty() || pass.isEmpty() || email.isEmpty())
//                    {
//                        Toast.makeText(SignUp.this,"Please fill all the fields!",Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        User user=new User(uname, email,  pass,  nick);
//                        user.InsertUser();
//                        Toast.makeText(SignUp.this,"Welcome to AnimEngine, "+nick,Toast.LENGTH_LONG).show();
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        Intent intent=new Intent(getApplicationContext(),home_screen.class);
//                        startActivity(intent);
//                    }
//                }else{
//                    Toast.makeText(SignUp.this,"Username already taken!",Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(SignUp.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
//            }
//        });
}
