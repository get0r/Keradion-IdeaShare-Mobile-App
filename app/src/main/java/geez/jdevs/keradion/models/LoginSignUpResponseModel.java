package geez.jdevs.keradion.models;

import com.google.gson.annotations.SerializedName;

public class LoginSignUpResponseModel {

    @SerializedName("success")
    private String success;
    @SerializedName("token")
    private String token;
    @SerializedName("reason")
    private String reason;
    @SerializedName("userId")
    private String userId;

    public LoginSignUpResponseModel(String success, String token) {
        this.success = success;
        this.token = token;
        this.reason = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getSuccess() {
        return success.equals("true");
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
