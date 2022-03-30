package geez.jdevs.keradion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import geez.jdevs.keradion.R;
import geez.jdevs.keradion.dbManagers.SharedPrefsManager;
import geez.jdevs.keradion.models.LoginSignUpResponseModel;
import geez.jdevs.keradion.models.UserDataModel;
import geez.jdevs.keradion.models.analayzers.FieldChecker;
import geez.jdevs.keradion.networkManagers.retrofit.APIClient;
import geez.jdevs.keradion.networkManagers.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mToolbar = findViewById(R.id.act_sign_up_toolbar);
        setSupportActionBar(mToolbar);

        initializeFields();

    }

    private void initializeFields() {
        final EditText name, username, email, password, conPassword, bioField;
        name = findViewById(R.id.act_sign_up_fullname_field);
        username = findViewById(R.id.act_sign_up_username_field);
        email = findViewById(R.id.act_sign_up_email_field);
        password = findViewById(R.id.act_sign_up_password_field);
        conPassword = findViewById(R.id.act_sign_up_conpassword_field);
        bioField = findViewById(R.id.act_sign_up_bio_field);
        Button signUp = findViewById(R.id.act_sign_up_signup_btn);
        Button sinupLogin = findViewById(R.id.act_sign_up_login_btn);

        setUpOnClickListeners(signUp, sinupLogin, name, username, email, password, conPassword, bioField);
    }

    private void setUpOnClickListeners(Button signUp, Button sinupLogin, final EditText name, final EditText username,
                                       final EditText email, final EditText password, final EditText conPassword,
                                       final EditText bioField) {
        final TextInputLayout nameIL = findViewById(R.id.act_sign_up_fullname_layout);
        final TextInputLayout emailIL = findViewById(R.id.act_sign_up_email_input_layout);
        final TextInputLayout usernameIL = findViewById(R.id.act_sign_up_username_input_layout);
        final TextInputLayout conPassIL = findViewById(R.id.act_sign_up_conpassword_input_layout);
        final TextInputLayout bioIL = findViewById(R.id.act_sign_up_bio_input_layout);
        final ProgressDialog progressDialog = new ProgressDialog(this);

        sinupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        final ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Signing Up...");

                final String fullName = name.getText().toString(),
                        userName = username.getText().toString(),
                        userEmail = email.getText().toString(),
                        pass1 = password.getText().toString(),
                        pass2 = conPassword.getText().toString(),
                        bio = bioField.getText().toString();
                if (FieldChecker.checkName((fullName)) &&
                        FieldChecker.checkUsername(userName) &&
                        FieldChecker.checkEmail(userEmail) &&
                        FieldChecker.checkPassword(pass1) &&
                        FieldChecker.checkBio(bio) &&
                        FieldChecker.confirmPassword(pass1, pass2)) {
                    progressDialog.show();
                    UserDataModel newUSer = new UserDataModel(fullName, userName, "", userEmail,
                            bio, "", pass1);
                    final Call<LoginSignUpResponseModel> call = apiService.getToken(newUSer);
                    call.enqueue(new Callback<LoginSignUpResponseModel>() {
                        @Override
                        public void onResponse(Call<LoginSignUpResponseModel> call, Response<LoginSignUpResponseModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getSuccess()) {
                                Intent homeAct = new Intent(SignUpActivity.this, HomeActivity.class);
                                SharedPreferences.Editor manager = SharedPrefsManager.getEditor(getApplicationContext());
                                manager.putString("token", response.body().getToken()).commit();
                                manager.putString("userId", response.body().getUserId()).commit();
                                manager.putString("userName", userName).commit();
                                manager.putString("password", pass1).commit();
                                manager.putString("bio",bio).commit();
                                manager.putString("firstTime", "false").commit();
                                homeAct.addCategory(Intent.CATEGORY_HOME);
                                homeAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(homeAct);
                            } else {
                                Toast.makeText(SignUpActivity.this, response.body().getReason(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginSignUpResponseModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Connection Failed!", Toast.LENGTH_LONG).show();
                            System.out.println(t.toString());
                        }
                    });
                } else {
                    if (!FieldChecker.checkName(fullName))
                        nameIL.setError("Invalid Name Input.");
                    else nameIL.setError("");
                    if (!FieldChecker.checkUsername(userName))
                        usernameIL.setError("Invalid Username or already taken.");
                    else usernameIL.setError("");
                    if (!FieldChecker.checkEmail(userEmail))
                        emailIL.setError("Invalid Email Input.");
                    else emailIL.setError("");
                    if (!FieldChecker.checkPassword(pass2) && FieldChecker.confirmPassword(pass1, pass2))
                        conPassIL.setError("Invalid Password Input or doesn't match.");
                    else conPassIL.setError("");
                    if (!FieldChecker.checkBio(bio))
                        bioIL.setError("Bio must contain more than 15 letters...");
                }
            }
        });
    }
}
