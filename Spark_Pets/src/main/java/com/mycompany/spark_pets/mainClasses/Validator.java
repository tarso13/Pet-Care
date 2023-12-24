
package mainClasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author kelet
 */
public class Validator {
       protected static boolean isUsernameValid(String username){
        if (username.length() < 8) {
            return false;
        }

        char firstChar = username.charAt(0);
        return !Character.isDigit(firstChar);
    }
    
    protected static boolean isEmailValid(String email) {
        String EMAIL_REGEX =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    protected static boolean isPasswordValid(String password) {
        String PASSWORD_REGEX =
            "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    protected static boolean isNameOrSurnameValid(String name) {
        String TEXT_REGEX = "^[^0-9]{3,30}$";
        return name.matches(TEXT_REGEX);
    }
    
     
    protected static boolean isTownValid(String town) {
        String TEXT_REGEX = "^[^0-9]{3,30}$";
        return town.matches(TEXT_REGEX);
    }
    
    protected static boolean isΑddressValid(String address) {
        int length = address.length();
        return length >= 10 && length <= 150;
    }
    
    protected static boolean isPersonalPageValid(String personalPage) {
        try {
            new URL(personalPage);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
    
    protected static boolean isJobValid(String job) {
        String TEXT_REGEX = "^[^0-9]{3,30}$";
        return job.matches(TEXT_REGEX);
    }
    
    protected static boolean isTelephoneValid(String telephone){
        String regex = "^\\d{10,14}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telephone);
        return matcher.matches();
    }
}
