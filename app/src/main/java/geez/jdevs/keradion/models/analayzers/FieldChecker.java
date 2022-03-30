package geez.jdevs.keradion.models.analayzers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldChecker {

    private static final String NON_WORD = "[^\\w+\\s+]";
    private static final String NON_WORD_AND_DIGIT = "[^\\w+\\d+]";
    private static final String NON_WORD_AND_DIGIT_EMAIL = "[^\\w+\\d+@.]";
    private static final String NON_WORD_AND_DIGIT_BIO = "[^\\w+\\d+\\s+]";

    private static Pattern pattern = null;
    private static Matcher matcher = null;

    public static boolean checkName(String name) {
        name = name.trim();
        pattern = Pattern.compile(NON_WORD);
        matcher = pattern.matcher(name);
        return !matcher.find() && name.length() > 2;
    }

    public static boolean checkUsername(String username) {
        username = username.trim();
        pattern = Pattern.compile(NON_WORD_AND_DIGIT);
        matcher = pattern.matcher(username);
        return !matcher.find() && username.length() > 3;
    }

    public static boolean checkEmail(String email) {
        email = email.trim();
        pattern = Pattern.compile(NON_WORD_AND_DIGIT_EMAIL);
        matcher = pattern.matcher(email);
        return !matcher.find() && email.length() > 5 && email.indexOf('@') != -1 && email.indexOf('.') != -1;
    }

    public static boolean checkPassword(String password) {
        password = password.trim();
        return password.length() > 8;
    }

    public static boolean confirmPassword(String pass1, String pass2) {
        pass1 = pass1.trim();
        pass2 = pass2.trim();
        return pass1.equals(pass2);
    }

    public static boolean checkBio(String bio) {
        bio = bio.trim();
        pattern = Pattern.compile(NON_WORD_AND_DIGIT_BIO);
        matcher = pattern.matcher(bio);
        return !matcher.find() && bio.length() > 15;
    }
}
