package geez.jdevs.keradion.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.textfield.TextInputLayout;

import geez.jdevs.keradion.R;
import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.LoginRequestModel;
import geez.jdevs.keradion.models.LoginSignUpResponseModel;
import geez.jdevs.keradion.models.analayzers.FieldChecker;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar = findViewById(R.id.login_act_toolbar);
        Button signUp = findViewById(R.id.login_act_signup_btn);
        Button login = findViewById(R.id.login_act_login_btn);
        final EditText username = findViewById(R.id.login_act_email_field);
        final EditText password = findViewById(R.id.login_act_password_field);
        final TextInputLayout userNameIL = findViewById(R.id.login_act_email_input_layout);
        final TextInputLayout passwordIL = findViewById(R.id.login_act_password_input_layout);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        setSupportActionBar(mToolbar);

        //check if this is the first time the app runs.
        if(SharedPrefsManager.getSharedPreferences(getApplicationContext()).getString("firstTime","true").equals("false"))
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        //retrieving Retrofit client for http request.
        final ApiInterface apiService =
                APIClient.getClient().create(ApiInterface.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Logging In...");

                final String userName = username.getText().toString();
                final String pass = password.getText().toString();
                if(FieldChecker.checkUsername(userName) && FieldChecker.checkPassword(pass)) {
                    progressDialog.show();
                    // preparing the callback to send req.
                    Call<LoginSignUpResponseModel> call = apiService.getLoginToken(new LoginRequestModel(userName,pass));
                    call.enqueue(new Callback<LoginSignUpResponseModel>() {
                        @Override
                        public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                            progressDialog.dismiss();
                            if(response.body() == null)
                                Toast.makeText(LoginActivity.this, "Null Response", Toast.LENGTH_LONG).show();
                            else if(response.body().getSuccess()) {
                                SharedPreferences.Editor manager = SharedPrefsManager.getEditor(getApplicationContext());
                                manager.putString("token",response.body().getToken()).commit();
                                manager.putString("userId",response.body().getUserId()).commit();
                                manager.putString("userName",userName).commit();
                                manager.putString("password",pass).commit();
                                manager.putString("firstTime","false").commit();
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                            else {
                                Toast.makeText(LoginActivity.this, response.body().getReason(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Connection Failed!", Toast.LENGTH_LONG).show();
                            System.out.println(t.toString());
                        }
                    });
                }
                else {
                    if (!FieldChecker.checkUsername(userName))
                        userNameIL.setError("Invalid Username.");
                    if (!FieldChecker.checkPassword(pass))
                        passwordIL.setError("Invalid Password Input or doesn't match.");
                }

            }
        });

    }
}
