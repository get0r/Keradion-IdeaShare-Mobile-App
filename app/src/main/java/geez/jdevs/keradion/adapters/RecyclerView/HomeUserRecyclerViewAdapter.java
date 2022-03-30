package geez.jdevs.keradion.adapters.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.LoginSignUpResponseModel;
import geez.jdevs.keradion.models.UserDataModel;
import geez.jdevs.keradion.R;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeUserRecyclerViewAdapter extends RecyclerView.Adapter<HomeUserRecyclerViewAdapter.UserProfileViewHolder> {

    protected class UserProfileViewHolder extends RecyclerView.ViewHolder {

        TextView name, username, bio, followBtn;

        public UserProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.home_frag_horz_recyc_item_userName);
            username = itemView.findViewById(R.id.home_frag_horz_recyc_item_username);
            bio = itemView.findViewById(R.id.home_frag_horz_recyc_item_userdetail);
            followBtn = itemView.findViewById(R.id.home_frag_horz_recy_item_folwBtn);
        }
    }

    private ArrayList<UserDataModel> usersList;
    private Context mContext;

    public HomeUserRecyclerViewAdapter(Context mContext, ArrayList<UserDataModel> usersList) {
        this.mContext = mContext;
        this.usersList = usersList;
    }

    public void addUser(UserDataModel user) {
        usersList.add(user);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_frag_horz_recyc_item_view,
                parent, false);
        return new UserProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserProfileViewHolder holder, int position) {
        final UserDataModel user = usersList.get(position);
        holder.name.setText(user.getFull_name());
        holder.username.setText("@".concat(user.getUsername()));
        holder.bio.setText(user.getBio());
        final ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.followBtn.getText().toString().equals("FOLLOW")) {
                    holder.followBtn.setText("Following");
                    holder.followBtn.setTextColor(Color.GREEN);
                    Call<LoginSignUpResponseModel> call =
                            apiService.followUser(SharedPrefsManager.
                                    getSharedPreferences(mContext.getApplicationContext()).
                                    getString("userId", ""), user.getUid());
                    call.enqueue(new Callback<LoginSignUpResponseModel>() {
                        @Override
                        public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                            System.out.println(response.raw());
                            if (response.body() == null)
                                Toast.makeText(mContext, "Connection Failed!", Toast.LENGTH_LONG).show();
                            else if (response.body().getSuccess()) {
                                holder.followBtn.setText("Following");
                                holder.followBtn.setTextColor(Color.GREEN);
                                holder.followBtn.setTag("ic_follow_green_24dp");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                            Toast.makeText(mContext, "Connection Failed!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    holder.followBtn.setTextColor(Color.BLACK);
                    holder.followBtn.setText("Follow");
                    Call<LoginSignUpResponseModel> call =
                            apiService.unfollowUser(SharedPrefsManager.
                                    getSharedPreferences(mContext.getApplicationContext()).
                                    getString("userId", ""), user.getUid());
                    call.enqueue(new Callback<LoginSignUpResponseModel>() {
                        @Override
                        public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                            if (response.body().getSuccess()) {
                                holder.followBtn.setTextColor(Color.BLACK);
                                holder.followBtn.setText("Follow");
                                holder.followBtn.setTag("ic_person_add_black_24dp");
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                            Toast.makeText(mContext, "Connection Failed!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
