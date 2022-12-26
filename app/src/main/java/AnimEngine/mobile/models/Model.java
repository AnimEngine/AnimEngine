package AnimEngine.mobile.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.Observable;

public class Model extends Observable {

    public Model(){

    }

    protected String result = "";
    protected String action = NONE;
    public static final String NONE = "NONE";

    protected final FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    protected final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public String getResult() {
        return result;
    }

    public String getAction() {
        return action;
    }
}
