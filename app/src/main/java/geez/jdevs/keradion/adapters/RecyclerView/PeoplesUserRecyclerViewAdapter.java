package geez.jdevs.keradion.adapters.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import geez.jdevs.keradion.activities.LoginActivity;
import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.LoginSignUpResponseModel;
import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.models.UserDataModel;
import geez.jdevs.keradion.R;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeoplesUserRecyclerViewAdapter extends RecyclerView.Adapter<PeoplesUserRecyclerViewAdapter.PeoplesItemViewHolder> {

    private Context mContext;
    private ArrayList<UserDataModel> usersList;

    public PeoplesUserRecyclerViewAdapter(Context mContext, ArrayList<UserDataModel> usersList) {

        this.mContext = mContext;
        this.usersList = usersList;

    }

    public void addUser(UserDataModel user) {
        usersList.add(user);
        notifyDataSetChanged();
    }

    protected class PeoplesItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView userPageImg;
        private TextView userPageName, userPageUsername, userPageDetail;
        private Button userPageFollowBtn;

        public PeoplesItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Typeface titleFont = ResourcesCompat.getFont(mContext, R.font.titlefont);
            userPageImg = itemView.findViewById(R.id.tab_frag_people_userimg);
            userPageName = itemView.findViewById(R.id.tab_frag_people_userName);
            userPageUsername = itemView.findViewById(R.id.tab_frag_people_username);
            userPageDetail = itemView.findViewById(R.id.tab_frag_people_userdetail);
            userPageFollowBtn = itemView.findViewById(R.id.tab_frag_people_followBtn);
            userPageName.setTypeface(titleFont);

        }
    }

    @NonNull
    @Override
    public PeoplesItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_fragment_peoples_pages_item_view,
                parent, false);
        return new PeoplesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PeoplesItemViewHolder holder, int position) {
        final UserDataModel user = usersList.get(position);
        if (!user.getUid().equals(SharedPrefsManager.
                getSharedPreferences(mContext.getApplicationContext()).
                getString("userId", ""))) {
            holder.userPageName.setText(user.getFull_name());
            holder.userPageUsername.setText("@".concat(user.getUsername()));
            holder.userPageDetail.setText(user.getBio());
            final ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

            holder.userPageFollowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!holder.userPageFollowBtn.getTag().equals("ic_follow_green_24dp")) {
                        holder.userPageFollowBtn.setText("Following");
                        holder.userPageFollowBtn.setTextColor(Color.GREEN);
                        Call<LoginSignUpResponseModel> call =
                                apiService.followUser(SharedPrefsManager.
                                        getSharedPreferences(mContext.getApplicationContext()).
                                        getString("userId", ""), user.getUid());
                        System.out.println(call.request().body());
                        call.enqueue(new Callback<LoginSignUpResponseModel>() {
                            @Override
                            public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                                System.out.println(response.raw());
                                if(response.body() == null) Toast.makeText(mContext, "Connection Failed!", Toast.LENGTH_LONG).show();
                                else if (response.body().getSuccess()) {
                                    holder.userPageFollowBtn.setText("Following");
                                    holder.userPageFollowBtn.setTextColor(Color.GREEN);
                                    holder.userPageFollowBtn.setTag("ic_follow_green_24dp");
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                                Toast.makeText(mContext, "Connection Failed!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        holder.userPageFollowBtn.setTextColor(Color.BLACK);
                        holder.userPageFollowBtn.setText("Follow");
                        Call<LoginSignUpResponseModel> call =
                                apiService.unfollowUser(SharedPrefsManager.
                                        getSharedPreferences(mContext.getApplicationContext()).
                                        getString("userId", ""), user.getUid());
                        call.enqueue(new Callback<LoginSignUpResponseModel>() {
                            @Override
                            public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                                if (response.body().getSuccess()) {
                                    holder.userPageFollowBtn.setTextColor(Color.BLACK);
                                    holder.userPageFollowBtn.setText("Follow");
                                    holder.userPageFollowBtn.setTag("ic_person_add_black_24dp");
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
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
