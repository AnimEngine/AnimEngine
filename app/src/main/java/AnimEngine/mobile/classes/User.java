package AnimEngine.mobile.classes;

import android.widget.EditText;

import java.util.HashMap;

public class User {
    private String Email, Password, Type;
    public User(String email, String password, String type){
        this.Email =email;
        this.Password =password;
        this.Type =type;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }
}
