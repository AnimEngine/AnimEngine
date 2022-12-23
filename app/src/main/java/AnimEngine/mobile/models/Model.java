package AnimEngine.mobile.models;

import java.util.Observable;

public class Model extends Observable {
    protected String result = "";

    public String getResult() {
        return result;
    }
}
