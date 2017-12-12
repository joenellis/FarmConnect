package com.buah.farmconnect.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.session.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {
    private TextInputEditText email, password;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Login");
        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public boolean onLoginClick(View view) {



        String txtEmail = email.getText().toString().trim();
        String txtPassword = password.getText().toString().trim();

        if(TextUtils.isEmpty(txtEmail)){
            email.setError("Enter email");
            return false; }

        if(TextUtils.isEmpty(txtPassword)){
            password.setError("Enter password");
            return false; }

         final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");
        progressDialog.show();


        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userLogin(txtEmail, txtPassword);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (!response.body().getError()) {
                       // Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getObjectUser());
                        startActivity(new Intent(getApplicationContext(), ActivityHome.class));
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return false;
    }

    private void init() {
        email = findViewById(R.id.login_txtEmail);
        password = findViewById(R.id.login_txtPassword);
    }

    public void onForgotPasswordClick(View view) {
    }

    public void onGotoSignUpClick(View view) {
        Intent intent = new Intent(this, ActivitySignUp.class);
        startActivity(intent);
    }
}
