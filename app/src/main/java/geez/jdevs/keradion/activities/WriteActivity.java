package geez.jdevs.keradion.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import geez.jdevs.keradion.R;
import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.LoginSignUpResponseModel;
import geez.jdevs.keradion.models.StoryModel;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xute.markdeditor.EditorControlBar;
import xute.markdeditor.MarkDCore;
import xute.markdeditor.MarkDEditor;
import xute.markdeditor.utilities.MarkDownConverter;
import xute.markdeditor.utilities.MarkDownFormat;

import static xute.markdeditor.utilities.FontSize.NORMAL;

public class WriteActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MarkDEditor mkEditor;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        setUpToolbar();
        setUpEditor();
        final EditText titleField = findViewById(R.id.act_write_title_field);
        final EditText topicField = findViewById(R.id.act_write_topic_field);
        final Spinner topicSpinner = findViewById(R.id.act_write_topic_spinner);
        Button postBtn = findViewById(R.id.act_write_postBtn);

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
                new String[]{"Tech", "Philosphy", "Pshychology"});
        topicSpinner.setAdapter(spinnerAdapter);
        final ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).
                setMessage("Final Draft?")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String storyContent = new MarkDownConverter().processData(mkEditor).getMarkDown();

                        if (titleField.getText().toString().length() != 0 &&
                                topicField.getText().toString().length() != 0 &&
                                storyContent.length() > 400) {
                            String userId = SharedPrefsManager.getSharedPreferences(getApplicationContext())
                                    .getString("userId", "");
                            StoryModel story = new StoryModel(
                                    titleField.getText().toString(),
                                    storyContent,
                                    userId,
                                    "",
                                    topicSpinner.getSelectedItem().toString(),
                                    topicField.getText().toString(),
                                    "");
                            progressDialog.setMessage("Posting...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            Call<LoginSignUpResponseModel> call = apiService.postStory(story);
                            call.enqueue(new Callback<LoginSignUpResponseModel>() {
                                @Override
                                public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                                    if (response.body().getSuccess()) {
                                        Toast.makeText(WriteActivity.this, "Posted Successfully!", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(WriteActivity.this, "Connection Failure!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            if(storyContent.length() < 400)
                                Toast.makeText(WriteActivity.this, "Article must at least contain 500 characters.", Toast.LENGTH_LONG).show();
                            else Toast.makeText(WriteActivity.this, "Title or Specific Topic is left Empty!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog = builder.create();
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
    }

    private void setUpEditor() {
        mkEditor = findViewById(R.id.act_write_mdEditor);
        EditorControlBar editorControlBar = findViewById(R.id.act_write_editorControlBar);
        editorControlBar.setEditorControlListener(new EditorControlBar.EditorControlListener() {
            @Override
            public void onInsertImageClicked() {

            }

            @Override
            public void onInserLinkClicked() {

            }
        });
        mkEditor.configureEditor("",
                "",
                false,
                "Type here...",
                NORMAL
        );
        editorControlBar.setEditor(mkEditor);
    }

    private void setUpToolbar() {
        mToolbar = findViewById(R.id.act_write_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Write");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
