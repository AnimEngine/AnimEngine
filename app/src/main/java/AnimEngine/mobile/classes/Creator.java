package AnimEngine.mobile.classes;

public class Creator extends User{
    private String webAddress, studioName;

    public Creator(String email, String password, String type, String webAddress, String studioName) {
        super(email, password, type);
        this.webAddress = webAddress;
        this.studioName = studioName;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }
}
