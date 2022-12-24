package AnimEngine.mobile.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.Observable;

public class Model extends Observable {
    protected String result = "";
    protected FirebaseFunctions mFunctions;
    protected FirebaseAuth mAuth;

    public String getResult() {
        return result;
    }
}
