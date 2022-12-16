package AnimEngine.mobile.utils;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Anime {
    private String name;
    private long likes;
    private long dislikes;
    private String description;
    private String creator_id;
    private int img_id;
    private List<String> genres;
    public Anime(String name,String creator_id, long likes, long dislikes, String description, int img_id,List<String> genres) {
        this.name = name;
        this.creator_id=creator_id;
        this.likes = likes;
        this.dislikes = dislikes;
        this.description = description;
        this.img_id = img_id;
        this.genres=genres;
    }

    public Anime(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getCreator() {
        return creator_id;
    }

    public void setCreator(String creator) {
        this.creator_id = creator;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public void addAnime(){
        DatabaseReference myRef= DB.getDB().getReference("Anime").child(name);
        myRef.setValue(this);
    }
}
