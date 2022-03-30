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

import geez.jdevs.keradion.adapters.RecyclerView.PeoplesUserRecyclerViewAdapter;
import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.models.Story$UserResponseModel;
import geez.jdevs.keradion.models.StoryModel;
import geez.jdevs.keradion.models.UserDataModel;
import geez.jdevs.keradion.R;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeoplesSingleTabFragment extends Fragment {

    private ArrayList<UserDataModel> usersList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private PeoplesUserRecyclerViewAdapter mAdapter;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        mAdapter = new PeoplesUserRecyclerViewAdapter(getActivity(), usersList);
        Call<Story$UserResponseModel> call = apiService.getExploreUser(SharedPrefsManager.
                getSharedPreferences(getContext().getApplicationContext()).
                getString("userId", ""));
        call.enqueue(new Callback<Story$UserResponseModel>() {
            @Override
            public void onResponse(Call<Story$UserResponseModel> call, Response<Story$UserResponseModel> response) {
                if (response.body().getUserList() != null && response.body().getUserList().size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    for(UserDataModel user : response.body().getUserList()) {
                        mAdapter.addUser(user);
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
        View tabFragmentView = inflater.inflate(R.layout.fragment_peoples_tab_fragment, container, false);
        mRecyclerView = tabFragmentView.findViewById(R.id.peoples_frag_tab_recycler_view);
        progressBar = tabFragmentView.findViewById(R.id.peoples_frag_progressBar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        return tabFragmentView;
    }
}
