package AnimEngine.mobile.classes;

import java.nio.channels.FileLock;

public class Comment {
    private String animeRef;
    private String content;
    private float stars; //stars = [1,5]

    public Comment(String id, String content, int stars){
        this.animeRef = animeRef;
        this.content = content;
        this.stars = stars;
    }

    public String getAnimeRef() {
        return animeRef;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }
}
