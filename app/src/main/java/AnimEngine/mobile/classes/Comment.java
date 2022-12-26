package AnimEngine.mobile.classes;

public class Comment {
    private String animeRef;
    private String authorName;
    private String content;
    private float stars; //stars = [1,5]

    public Comment(String animeRef, String authorName, String content, float stars){
        this.animeRef = animeRef;
        this.authorName = authorName;
        this.content = content;
        this.stars = stars;
    }

    public String getAnimeRef() {
        return animeRef;
    }

    public void setAnimeRef(String animeRef) {
        this.animeRef = animeRef;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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
