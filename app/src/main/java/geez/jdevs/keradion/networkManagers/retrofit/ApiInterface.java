package geez.jdevs.keradion.networkManagers.retrofit;

import geez.jdevs.keradion.models.LoginRequestModel;
import geez.jdevs.keradion.models.LoginSignUpResponseModel;
import geez.jdevs.keradion.models.Search$BioModel;
import geez.jdevs.keradion.models.Story$UserResponseModel;
import geez.jdevs.keradion.models.StoryModel;
import geez.jdevs.keradion.models.UserDataModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/signUp")
    Call<LoginSignUpResponseModel> getToken(@Body UserDataModel user);
    @POST("/login")
    Call<LoginSignUpResponseModel> getLoginToken(@Body LoginRequestModel loginData);
    @POST("/{userId}/bio")
    Call<LoginSignUpResponseModel> setBio(@Body String bio);
    @POST("/{userId}/post")
    Call<LoginSignUpResponseModel> postStory(@Body StoryModel story);
    @POST("/search")
    Call<Story$UserResponseModel> searchUser(@Body Search$BioModel userName);
    @POST("/search/article")
    Call<Story$UserResponseModel> searchArticle(@Body Search$BioModel articleTitle);
    @GET("/{userId}")
    Call<UserDataModel> getUserDetail(@Path("userId") String userId);
    @GET("/{userId}/home")
    Call<Story$UserResponseModel> getStoryAndUser(@Path("userId") String userId);
    @GET("/{userId}/explore/{topic}")
    Call<Story$UserResponseModel> getExploreStory(@Path("userId") String userId, @Path("topic") String topic);
    @GET("/{userId}/expeople")
    Call<Story$UserResponseModel> getExploreUser(@Path("userId") String userId);
    @GET("/{userId}/{uid}/follow")
    Call<LoginSignUpResponseModel> followUser(@Path("userId") String userId, @Path("uid") String uid);
    @GET("/{userId}/{uid}/unfollow")
    Call<LoginSignUpResponseModel> unfollowUser(@Path("userId") String userId, @Path("uid") String uid);
    @GET("/{storyId}/well")
    Call<LoginSignUpResponseModel> setStoryWellWritten(@Path("storyId") String storyId);
    @GET("/{userId}/{storyId}/report/{type}")
    Call<LoginSignUpResponseModel> reportStory(@Path("userId") String userId, @Path("storyId") String storyId, @Path("type") String type);
}
