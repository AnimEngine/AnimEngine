package AnimEngine.mobile.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Favorites {
    private List<String> favorites;
    private String nickname;
    private String id;

    public Favorites(String nickname,String id) {
        this.nickname = nickname;
        this.id=id;
        this.favorites = new ArrayList<>();
    }

    public Favorites(String nickname,String id, List<String> favorites) {
        this.nickname = nickname;
        this.id=id;
        this.favorites = favorites;
    }

    public Favorites() {
        this.nickname = "nickname";
        this.id="id";
        this.favorites = new ArrayList<>();
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void InsertDB(){
        FirebaseDatabase db = DB.getDB();
        DatabaseReference myRef = db.getReference("Favorites").child(id);
        myRef.setValue(this);
    }
}
