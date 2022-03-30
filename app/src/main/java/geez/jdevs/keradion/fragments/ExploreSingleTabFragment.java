package geez.jdevs.keradion.fragments;

import android.app.AlertDialog;
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

import geez.jdevs.keradion.adapters.RecyclerView.ExploreStoryAndPeopleUserAdapter;
import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.R;
import geez.jdevs.keradion.models.Story$UserResponseModel;
import geez.jdevs.keradion.models.StoryModel;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreSingleTabFragment extends Fragment {

    private ArrayList<Story$UserModel> storyUserList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ExploreStoryAndPeopleUserAdapter mAdapter;
    private String tabName;
    private ProgressBar progressBar;

    public ExploreSingleTabFragment(String tabName) {
        this.tabName = tabName;
        System.out.println(tabName);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        mAdapter = new ExploreStoryAndPeopleUserAdapter(getActivity(), storyUserList);
        Call<Story$UserResponseModel> call =
                apiService.getExploreStory(SharedPrefsManager.
                        getSharedPreferences(getContext().getApplicationContext()).
                        getString("userId", ""),tabName);

        call.enqueue(new Callback<Story$UserResponseModel>() {
            @Override
            public void onResponse(Call<Story$UserResponseModel> call, Response<Story$UserResponseModel> response) {

                if (response.body().getStoryList() != null && response.body().getStoryList().size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    for (StoryModel story : response.body().getStoryList()) {
                        mAdapter.addStory(new Story$UserModel(
                                story.getSid(),
                                story.getTitle(),
                                story.getContent(),
                                story.getNo_of_well_written(),
                                story.getWritten_date(),
                                story.getFull_name(),
                                story.getS_Topic()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Story$UserResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Connection Failure", Toast.LENGTH_LONG).show();
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
        View tabFragmentView = inflater.inflate(R.layout.fragment_explore_tab_fragment, container, false);
        mRecyclerView = tabFragmentView.findViewById(R.id.explore_frag_tab_recycler_view);
        progressBar = tabFragmentView.findViewById(R.id.explore_frag_progressBar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        return tabFragmentView;
    }
}
