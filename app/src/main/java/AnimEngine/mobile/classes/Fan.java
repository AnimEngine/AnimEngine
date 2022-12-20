package AnimEngine.mobile.classes;

public class Fan extends User{
    private String fName, lName;

    public Fan(String email, String password, String type, String fName, String lName) {
        super(email, password, type);
        this.fName = fName;
        this.lName = lName;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }
}
