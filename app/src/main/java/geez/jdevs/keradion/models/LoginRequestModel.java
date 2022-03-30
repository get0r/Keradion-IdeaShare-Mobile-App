package geez.jdevs.keradion.models;

public class LoginRequestModel {

    private String username;
    private String password;
    private String bio;

    public LoginRequestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
