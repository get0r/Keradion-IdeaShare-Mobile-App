package geez.jdevs.keradion.networkManagers.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient   {

    //https://powerful-mesa-42891.herokuapp.com/
    private static final String BASE_URL = "https://powerful-mesa-42891.herokuapp.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}