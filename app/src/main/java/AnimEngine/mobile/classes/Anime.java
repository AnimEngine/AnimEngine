package AnimEngine.mobile.classes;

import java.util.HashMap;

public class Anime {

    private String name;
    private String description;
    private String imageURL;
    private HashMap<String, Float> genres;
    private String ownerUID;



    public Anime(String name){

    }

    public Anime(String name, String description, String imageURL, HashMap<String, Float> genres, String ownerUID) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.genres = genres;
        this.ownerUID = ownerUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public HashMap<String, Float> getGenres() {
        return genres;
    }

    public void setGenres(HashMap<String, Float> genres) {
        this.genres = genres;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }
}
