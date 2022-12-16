package AnimEngine.mobile.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class User {

    private String name;
    private String mail;
    private String password;
    private String nickname;
    private boolean creator;
    private String id;
//    private Map<String, Integer> genres;
//    private List<Anime> likes;
//    final private String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice of Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
//            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime"};


    public User(String name, String mail, String password, String nickname, boolean creator, String id) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.nickname = nickname;
        this.creator=creator;
        //String regex="[.#$\\[\\]@]";
        //this.id=mail.replaceAll(regex,"");
        this.id=id;
    }

    public User() {
        this.name = "name";
        this.mail = "mail";
        this.password = "password";
        this.nickname = "nickname";
        this.creator=false;
    }

    public User(String name, String mail, String password, String nickname, Map<String, Integer> genres, List<Anime> likes) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.nickname = nickname;

    }

    public User(User other) {
        this.name = other.getName();
        this.mail = other.getMail();
        this.password = other.getPassword();
        this.creator=other.creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isCreator() {
        return creator;
    }

    public void setCreator(boolean creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void InsertUser() {
        FirebaseDatabase db = DB.getDB();
        DatabaseReference myRef = db.getReference("Users").child(id);
        myRef.setValue(this);
    }
}
