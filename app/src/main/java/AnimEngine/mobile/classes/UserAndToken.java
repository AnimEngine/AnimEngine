package AnimEngine.mobile.classes;

import java.io.Serializable;

public class UserAndToken implements Serializable {

    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
