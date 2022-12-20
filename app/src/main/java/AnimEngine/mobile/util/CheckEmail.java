package AnimEngine.mobile.util;

import java.util.regex.Pattern;

public class CheckEmail {
    Pattern pattern;
    public CheckEmail(){
        pattern = Pattern.compile("([a-zA-Z0-9]+)([_.-]{1}[a-zA-Z0-9]+)*@([a-zA-Z0-9])+(-{1}[a-zA-Z0-9]+)*(\\.([a-zA-Z]{2,}))+", Pattern.CASE_INSENSITIVE);
    }

    public boolean isValidEmail(String email){
        return pattern.matcher(email).matches();
    }
}
