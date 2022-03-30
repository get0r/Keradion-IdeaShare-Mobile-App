package geez.jdevs.keradion.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.mukesh.MarkdownView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import geez.jdevs.keradion.R;
import geez.jdevs.keradion.dbManagers.SQLiteDBHelper;
import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.LoginSignUpResponseModel;
import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.models.StoryModel;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewStoryActivity extends AppCompatActivity {

    private ApiInterface apiService;
    private Intent intent;
    private SQLiteDBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
        ImageButton mToolbar = findViewById(R.id.act_view_story_toolbar);
        final TextView titleView = findViewById(R.id.act_write_title_view);
        TextView writerName = findViewById(R.id.act_view_story_writerName);
        TextView writtenDate = findViewById(R.id.act_view_story_writtenDate);
        ImageButton wellWritten = findViewById(R.id.act_view_story_well_written);
        final TextView noOfWell = findViewById(R.id.act_write_no_of_well);
        ImageButton report = findViewById(R.id.act_view_story_report);
        ImageButton saveForOffline = findViewById(R.id.act_view_story_save_offline);
        dbHelper = new SQLiteDBHelper(getApplicationContext());

        intent = getIntent();
        registerForContextMenu(report);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        apiService = APIClient.getClient().create(ApiInterface.class);
        wellWritten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<LoginSignUpResponseModel> call = apiService.setStoryWellWritten(intent.getStringExtra("sid"));
                call.enqueue(new Callback<LoginSignUpResponseModel>() {
                    @Override
                    public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                        if (response.body().getSuccess()) {
                            noOfWell.setText(Integer.toString(Integer.parseInt(noOfWell.getText().toString().trim())+1));
                            Toast.makeText(ViewStoryActivity.this, "You have just Admired the article", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                        Toast.makeText(ViewStoryActivity.this, "Connection Failure!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContextMenu(view);

            }
        });

        saveForOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Story$UserModel article = new Story$UserModel(
                        intent.getStringExtra("sid"),
                        intent.getStringExtra("title"),
                        intent.getStringExtra("content"),
                        intent.getStringExtra("noOfWell"),
                        intent.getStringExtra("offDate"),
                        intent.getStringExtra("writerName"),
                        intent.getStringExtra("topic")
                        );
                System.out.println(intent.getStringExtra("offDate"));
                dbHelper.addArticle(article);
                Toast.makeText(ViewStoryActivity.this, "Saved For Offline Use", Toast.LENGTH_SHORT).show();
            }
        });

        Typeface titleFont = ResourcesCompat.getFont(this, R.font.titlefont);
        titleView.setTypeface(titleFont);
        titleView.setText(intent.getStringExtra("title"));
        noOfWell.setText(intent.getStringExtra("noOfWell").concat(" "));
        writerName.setText("By: ".concat(intent.getStringExtra("writerName").concat(" ")));
        writtenDate.setText("On: ".concat(intent.getStringExtra("writtenDate").concat(" \nat ").concat(intent.getStringExtra("writtenTime"))));
        MarkdownView markdownView = findViewById(R.id.act_view_story_viewer);
        markdownView.setMarkDownText(intent.getStringExtra("content"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select");
        menu.add(0, v.getId(), 0, "Abusive");
        menu.add(0, v.getId(), 0, "Hate Speech");
        menu.add(0, v.getId(), 0, "Pornography");

    }

    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        Call<LoginSignUpResponseModel> call;
            call = apiService.reportStory(
                    SharedPrefsManager.getSharedPreferences(getApplicationContext()).getString("userId","")
                    ,intent.getStringExtra("sid"),item.getTitle().toString());
            call.enqueue(new Callback<LoginSignUpResponseModel>() {
                @Override
                public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                    if(response.body().getSuccess())
                        Toast.makeText(ViewStoryActivity.this, "Reported as "+ item.getTitle().toString()+ "!", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                    Toast.makeText(ViewStoryActivity.this, "Connection Failure!", Toast.LENGTH_LONG).show();
                }
            });
        return true;
    }
}
