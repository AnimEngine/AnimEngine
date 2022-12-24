package AnimEngine.mobile.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.Observable;

public class Model extends Observable {

    public Model(){

    }

    protected String result = "";
    protected final FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    protected final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public String getResult() {
        return result;
    }
}
