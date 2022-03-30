package geez.jdevs.keradion.models;


import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDataModel {

    private String uid;
    private String full_name;
    private String username;
    private String ppicLink;
    private String email;
    private String bio;
    private String noOfFollowers;
    private String password;
    private String noOfWrittenArticles;

    public UserDataModel(String name, String username, String ppicLink, String email, String userbio, String noOfFollowers, String password) {

        this.full_name = name;
        this.username = username;
        this.ppicLink = ppicLink;
        this.email = email;
        this.bio = userbio;
        this.noOfFollowers = noOfFollowers;
        this.password = password;
        this.noOfWrittenArticles = "";
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNoOfWrittenArticles() {
        return noOfWrittenArticles;
    }

    public void setNoOfWrittenArticles(String noOfWrittenArticles) {
        this.noOfWrittenArticles = noOfWrittenArticles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String name) {
        this.full_name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPpicLink() {
        return ppicLink;
    }

    public void setPpicLink(String ppicLink) {
        this.ppicLink = ppicLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String userbio) {
        this.bio = userbio;
    }

    public String getNoOfFollowers() {
        return noOfFollowers;
    }

    public void setNoOfFollowers(String noOfFollowers) {
        this.noOfFollowers = noOfFollowers;
    }
}
