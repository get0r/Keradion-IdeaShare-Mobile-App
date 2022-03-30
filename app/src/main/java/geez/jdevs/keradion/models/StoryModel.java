package geez.jdevs.keradion.models;

public class StoryModel {

    private String sid;
    private String title;
    private String content;
    private String writerId;
    private String no_of_well_written;
    private String full_name;
    private String g_topic;
    private String s_topic;
    private String written_date;

    public StoryModel(String title, String storyContent, String writerId, String no_of_well_written, String g_topic, String s_topic, String written_date) {
        this.title = title;
        this.content = storyContent;
        this.writerId = writerId;
        this.no_of_well_written = no_of_well_written;
        this.g_topic = g_topic;
        this.s_topic = s_topic;
        this.written_date = written_date;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getNo_of_well_written() {
        return no_of_well_written;
    }

    public void setNo_of_well_written(String no_of_well_written) {
        this.no_of_well_written = no_of_well_written;
    }

    public String getG_topic() {
        return g_topic;
    }

    public void setG_topic(String g_topic) {
        this.g_topic = g_topic;
    }

    public String getS_Topic() {
        return s_topic;
    }

    public void setS_Topic(String s_Topic) {
        this.s_topic = s_Topic;
    }

    public String getWritten_date() {
        return written_date;
    }

    public void setWritten_date(String written_date) {
        this.written_date = written_date;
    }
}
