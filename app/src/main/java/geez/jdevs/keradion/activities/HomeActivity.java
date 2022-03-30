package geez.jdevs.keradion.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.fragments.ExploreFragment;
import geez.jdevs.keradion.fragments.HomeFragment;
import geez.jdevs.keradion.fragments.PeoplesFragment;
import geez.jdevs.keradion.R;
import geez.jdevs.keradion.models.LoginSignUpResponseModel;
import geez.jdevs.keradion.models.UserDataModel;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionDrawerTogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //setting up toolbar and NavigationView contents
        setUpToobarAndNav();

    }

    /**
     * positions and renders toolbar and navigation view.
     */

    private void setUpToobarAndNav() {
        DrawerLayout mDrawerLayout = findViewById(R.id.act_home_drawer_layout);
        mToolbar = findViewById(R.id.act_home_toolbar);
        mNavigationView = findViewById(R.id.act_home_nav_view);
        View navigationHeader = mNavigationView.getHeaderView(0);
        ImageView profilePic = navigationHeader.findViewById(R.id.navigation_profile_image);
        final TextView name = navigationHeader.findViewById(R.id.act_home_nav_header_name),
                 username = navigationHeader.findViewById(R.id.act_home_nav_header_username);
        final ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        final EditText bio = new EditText(this);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Write Something about yourself")
                .setCancelable(true)
                .setView(bio)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<LoginSignUpResponseModel> call = apiService.setBio(bio.getText().toString());
                        call.enqueue(new Callback<LoginSignUpResponseModel>() {
                            @Override
                            public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                                System.out.println(response.toString());
                                if(response.body().getSuccess())
                                    Toast.makeText(HomeActivity.this, "uploaded!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                                Toast.makeText(HomeActivity.this, "Connection Failure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).create();

        FloatingActionButton writeBtn = findViewById(R.id.act_home_write_fab);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setSubtitle("One step to forward to wisdom.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


        mNavigationView.setCheckedItem(R.id.nav_menu_home);



        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WriteActivity.class));
            }
        });

        mActionDrawerTogle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        mDrawerLayout.addDrawerListener(mActionDrawerTogle);
        mActionDrawerTogle.syncState();
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.act_home_fragment_field,new HomeFragment()).commit();
        //setting Naviagation View items click listener
        setNavigationViewItemSlectedListener(mDrawerLayout);

        String userId = SharedPrefsManager.getSharedPreferences(getApplicationContext()).getString("userId","");
        if(SharedPrefsManager.getSharedPreferences(getApplicationContext()).getString("name",null) == null) {
            Call<UserDataModel> call = apiService.getUserDetail(userId);
            call.enqueue(new Callback<UserDataModel>() {
                @Override
                public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                    System.out.println(response.raw());
                    SharedPreferences.Editor editor = SharedPrefsManager.getEditor(getApplicationContext());
                    editor.putString("name",response.body().getFull_name()).commit();
                    editor.putString("email",response.body().getEmail()).commit();
                    editor.putString("noOfFollowers",response.body().getNoOfFollowers()).commit();
                    editor.putString("noOfWrittenArticle",response.body().getNoOfWrittenArticles()).commit();
                    name.setText(response.body().getFull_name());
                    username.setText("@".concat(response.body().getUsername()));
                }

                @Override
                public void onFailure(Call<UserDataModel> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Connection Failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            name.setText(SharedPrefsManager.getSharedPreferences(getApplicationContext()).getString("name","Loading..."));
            username.setText("@".concat(SharedPrefsManager.getSharedPreferences(getApplicationContext()).getString("userName","Loading...")));
        }
    }

    /**
     * sets a click listener for the navigation menu items.
     */

    private void setNavigationViewItemSlectedListener(final DrawerLayout mDrawerLayout) {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (!item.isChecked())
                    item.setChecked(true);
                mDrawerLayout.closeDrawers();
                FragmentManager mFragmentManager = getSupportFragmentManager();
                FragmentTransaction mFragmentTransaction;

                switch (item.getItemId()) {
                    case R.id.nav_menu_home:
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        getSupportActionBar().setTitle("Home");
                        getSupportActionBar().setSubtitle("One Step Forward to Wisdom.");
                        mFragmentTransaction.replace(R.id.act_home_fragment_field,new HomeFragment()).commit();
                        break;
                    case R.id.nav_menu_explore:
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        getSupportActionBar().setTitle("Explore");
                        getSupportActionBar().setSubtitle("");
                        mFragmentTransaction.replace(R.id.act_home_fragment_field,new ExploreFragment()).commit();
                        break;
                    case R.id.nav_menu_peoples:
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        getSupportActionBar().setTitle("Peoples");
                        getSupportActionBar().setSubtitle("");
                        mFragmentTransaction.replace(R.id.act_home_fragment_field,new PeoplesFragment()).commit();
                        break;
                    case R.id.nav_menu_logout:
                        SharedPrefsManager.getEditor(getApplicationContext()).clear().commit();
                        SharedPrefsManager.getEditor(getApplicationContext()).putString("firstTime","true").commit();
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        break;
                    case R.id.nav_menu_savedArt:
                        startActivity(new Intent(HomeActivity.this,SavedStoriesActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            if(mNavigationView.getCheckedItem().getItemId() == R.id.nav_menu_explore)
                intent.putExtra("fragment","Explore");
            else intent.putExtra("fragment","Ep");
            startActivity(intent);
        }
        return true;
    }
}
