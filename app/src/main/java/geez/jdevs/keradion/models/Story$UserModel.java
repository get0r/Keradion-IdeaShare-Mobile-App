package geez.jdevs.keradion.models;

import java.util.ArrayList;

public class Story$UserModel {

    private int viewType;


    private String sid;
    private String title;
    private String storyContent;
    private String writerUsername;
    private String no_of_well_written;
    private String written_date;
    private String writerName;
    private String sTopic;

    public Story$UserModel(String sid, String title, String storyContent, String no_of_well_written, String written_date, String writerName,String sTopic) {
        this.viewType = 0;
        this.sid = sid;
        this.title = title;
        this.storyContent = storyContent;
        this.writerUsername = writerUsername;
        this.no_of_well_written = no_of_well_written;
        this.writerName = writerName;
        this.written_date = written_date;
        this.sTopic = sTopic;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getsTopic() {
        return sTopic;
    }

    public void setsTopic(String sTopic) {
        this.sTopic = sTopic;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getWritten_date() {
        return written_date;
    }

    public void setWritten_date(String written_date) {
        this.written_date = written_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    public String getWriterUsername() {
        return writerUsername;
    }

    public void setWriterUsername(String writerUsername) {
        this.writerUsername = writerUsername;
    }

    public String getNo_of_well_written() {
        return no_of_well_written;
    }

    public void setNo_of_well_written(String no_of_well_written) {
        this.no_of_well_written = no_of_well_written;
    }

    private ArrayList<UserDataModel> usersList = new ArrayList<>();

    public Story$UserModel(ArrayList<UserDataModel> usersList) {
        this.viewType = 1;
        this.usersList = usersList;
    }

    public ArrayList<UserDataModel> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<UserDataModel> usersList) {
        this.usersList = usersList;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
