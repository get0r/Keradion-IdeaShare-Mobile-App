package geez.jdevs.keradion.models;

import com.google.gson.annotations.SerializedName;

public class Search$BioModel  {

    @SerializedName("userName")
    private String userName;
    @SerializedName("title")
    private String title;

    public Search$BioModel(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
