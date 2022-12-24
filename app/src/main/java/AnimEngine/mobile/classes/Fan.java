package AnimEngine.mobile.classes;

import java.util.HashMap;

public class Fan extends User{
    private String fName, lName;
    private String[] blacklist;
    private HashMap<String, Float> genres;

    public Fan(String email, String password, String type, String fName, String lName, String[] blacklist, HashMap<String, Float> genres) {
        super(email, password, type);
        this.fName = fName;
        this.lName = lName;
        this.blacklist = blacklist;
        this.genres = genres;
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

    public String[] getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String[] blacklist) {
        this.blacklist = blacklist;
    }

    public HashMap<String, Float> getGenres() {
        return genres;
    }

    public void setGenres(HashMap<String, Float> genres) {
        this.genres = genres;
    }
}
