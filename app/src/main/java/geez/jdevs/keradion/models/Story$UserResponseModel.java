package geez.jdevs.keradion.models;

import java.util.ArrayList;

public class Story$UserResponseModel {

    private ArrayList<StoryModel> storyList;
    private ArrayList<UserDataModel> userList;

    public Story$UserResponseModel(ArrayList<StoryModel> storyList, ArrayList<UserDataModel> userList) {
        this.storyList = storyList;
        this.userList = userList;
    }

    public ArrayList<StoryModel> getStoryList() {
        return storyList;
    }

    public void setStoryList(ArrayList<StoryModel> storyList) {
        this.storyList = storyList;
    }

    public ArrayList<UserDataModel> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<UserDataModel> userList) {
        this.userList = userList;
    }
}
