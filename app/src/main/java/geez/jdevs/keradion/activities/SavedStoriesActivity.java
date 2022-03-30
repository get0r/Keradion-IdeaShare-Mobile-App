package geez.jdevs.keradion.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import geez.jdevs.keradion.R;
import geez.jdevs.keradion.adapters.RecyclerView.ExploreStoryAndPeopleUserAdapter;
import geez.jdevs.keradion.adapters.RecyclerView.PeoplesUserRecyclerViewAdapter;
import geez.jdevs.keradion.dbManagers.SQLiteDBHelper;
import geez.jdevs.keradion.models.Story$UserModel;

public class SavedStoriesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ExploreStoryAndPeopleUserAdapter mAdapter;
    private ArrayList<Story$UserModel> storiesList = new ArrayList<>();
    private SQLiteDBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        Toolbar mToolbar = findViewById(R.id.act_offline_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Saved Articles");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.act_offline_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ExploreStoryAndPeopleUserAdapter(this,storiesList);
        mRecyclerView.setAdapter(mAdapter);

        dbHelper = new SQLiteDBHelper(getApplicationContext());
        storiesList = dbHelper.allArticles();

        if(storiesList.size() != 0) {
            for(Story$UserModel story : storiesList) {
                mAdapter.addStory(story);
            }
        }

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

    public void deleteAll(View view) {
        System.out.println("Deleted");
        dbHelper.deleteAllArticle();
        mAdapter.removeAll();
    }
}
