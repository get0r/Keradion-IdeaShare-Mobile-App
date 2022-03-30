package geez.jdevs.keradion.fragments;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import geez.jdevs.keradion.adapters.RecyclerView.HomeStoryRecyclerViewAdapter;
import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.models.Story$UserResponseModel;
import geez.jdevs.keradion.models.StoryModel;
import geez.jdevs.keradion.R;
import geez.jdevs.keradion.models.UserDataModel;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private List<Story$UserModel> storiesList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private HomeStoryRecyclerViewAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        mAdapter = new HomeStoryRecyclerViewAdapter(storiesList, getActivity());
        Call<Story$UserResponseModel> call =
                apiService.getStoryAndUser(SharedPrefsManager.getSharedPreferences(getContext().getApplicationContext()).getString("userId",""));

        call.enqueue(new Callback<Story$UserResponseModel>() {
            @Override
            public void onResponse(Call<Story$UserResponseModel> call, Response<Story$UserResponseModel> response) {
                int i = 0;
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                if(response.body() != null) {
                    ArrayList<StoryModel> storyList = response.body().getStoryList();
                    ArrayList<UserDataModel> userList;
                    System.out.println("Stories..." + storyList.size() + " Users list" + response.body().getUserList().size());

                    for (; i < storyList.size(); i++) {
                        System.out.println(storyList.get(i).getFull_name());
                        mAdapter.addStory(new Story$UserModel(
                                storyList.get(i).getSid(),
                                storyList.get(i).getTitle(),
                                storyList.get(i).getContent(),
                                storyList.get(i).getNo_of_well_written(),
                                storyList.get(i).getWritten_date(),
                                storyList.get(i).getFull_name(),
                                storyList.get(i).getS_Topic()));
                        userList = new ArrayList<>(response.body().getUserList());
                        if(i == 2)
                            mAdapter.addUserList(new Story$UserModel(userList));
                    }
                }
            }

            @Override
            public void onFailure(Call<Story$UserResponseModel> call, Throwable t) {
                //Toast.makeText(, "Connection Failure",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = fragmentView.findViewById(R.id.fragment_home_recyclerview);
        mProgressBar = fragmentView.findViewById(R.id.home_frag_progressBar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        return fragmentView;
    }

}
