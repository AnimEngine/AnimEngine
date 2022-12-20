package AnimEngine.mobile.classes;

public class Creator extends User{
    private String webAddress, country;

    public Creator(String email, String password, String type, String webAddress, String country) {
        super(email, password, type);
        this.webAddress = webAddress;
        this.country = country;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
