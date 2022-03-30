package geez.jdevs.keradion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import geez.jdevs.keradion.R;
import geez.jdevs.keradion.adapters.RecyclerView.ExploreStoryAndPeopleUserAdapter;
import geez.jdevs.keradion.adapters.RecyclerView.PeoplesUserRecyclerViewAdapter;
import geez.jdevs.keradion.models.Search$BioModel;
import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.models.Story$UserResponseModel;
import geez.jdevs.keradion.models.StoryModel;
import geez.jdevs.keradion.models.UserDataModel;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PeoplesUserRecyclerViewAdapter mAdapter;
    private ExploreStoryAndPeopleUserAdapter mAdapter1;
    private ArrayList<UserDataModel> userList = new ArrayList<>();
    private ArrayList<Story$UserModel> storyList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent transferredData = getIntent();
        final EditText searchField = findViewById(R.id.act_search_searchField);
        final ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        Button searchBtn = findViewById(R.id.act_search_searchBtn);
        mRecyclerView = findViewById(R.id.search_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PeoplesUserRecyclerViewAdapter(SearchActivity.this,userList);
        mAdapter1 = new ExploreStoryAndPeopleUserAdapter(SearchActivity.this,storyList);


        if (transferredData.getStringExtra("fragment").equals("Explore")) {
            searchField.setHint("Search Articles...");
            mRecyclerView.setAdapter(mAdapter1);
        } else {
            searchField.setHint("Search Peoples...");
            mRecyclerView.setAdapter(mAdapter);
        }
        searchField.requestFocus();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchField.getText().toString().length() > 0) {

                    String fragmentData = getIntent().getStringExtra("fragment");
                    Call<Story$UserResponseModel> call;
                    Call<Story$UserResponseModel> userDataModelCall;
                    Search$BioModel search$BioModel = new Search$BioModel(searchField.getText().toString().trim());
                    Search$BioModel search$BioModel1 = new Search$BioModel("test");
                    search$BioModel1.setTitle(searchField.getText().toString().trim());
                    call = apiService.searchArticle(search$BioModel1);
                    userDataModelCall = apiService.searchUser(search$BioModel);
                    if (fragmentData.equals("Explore")) {
                        call.enqueue(new Callback<Story$UserResponseModel>() {
                            @Override
                            public void onResponse(Call<Story$UserResponseModel> call, Response<Story$UserResponseModel> response) {
                                if (response.body() != null) {
                                    for(int i = 0; i < response.body().getStoryList().size(); i++) {
                                        System.out.println("DDDD: "+storyList.size());
                                        /*mAdapter1.addStory(new Story$UserModel(storyList.get(i).getSid(),
                                                storyList.get(i).getTitle(),
                                                storyList.get(i).getStoryContent(),
                                                storyList.get(i).getNo_of_well_written(),
                                                storyList.get(i).getWritten_date(),
                                                storyList.get(i).getWriterName(),
                                                storyList.get(i).getsTopic()
                                                ));*/

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Story$UserResponseModel> call, Throwable t) {
                                Toast.makeText(SearchActivity.this, "Connection Failure!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        userDataModelCall.enqueue(new Callback<Story$UserResponseModel>() {
                            @Override
                            public void onResponse(Call<Story$UserResponseModel> call, Response<Story$UserResponseModel> response) {
                                if (response.body() != null) {
                                    System.out.println(response.body().getUserList().size());
                                    userList = response.body().getUserList();
                                    for(int i = 0; i < response.body().getUserList().size(); i++) {
                                        System.out.println(userList.get(i).getFull_name());
                                        mAdapter.addUser(userList.get(i));

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Story$UserResponseModel> call, Throwable t) {
                                System.out.println(call.request().body());
                                Toast.makeText(SearchActivity.this, "Connection Failure!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

        });
    }
}
