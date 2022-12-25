package AnimEngine.mobile.classes;

public class Comment {
    private String id;
    private String content;
    private int stars; //stars = [1,5]

    public Comment(String id, String content, int stars){
        this.id = id;
        this.content = content;
        this.stars = stars;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
