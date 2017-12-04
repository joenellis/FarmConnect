package com.buah.farmconnect.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.session.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMyAccount extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView TextFullName, TextEmail,TextContact;
    private String FullName;
    private String Contact;
    private String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("My Account");
        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        TextFullName = findViewById(R.id.fname);
        TextEmail = findViewById(R.id.email);
        TextContact =  findViewById(R.id.number);


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        String userid = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getUser_id();
        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userprofile(userid);


        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
            progressDialog.dismiss();
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                        FullName = response.body().getObjectUser().getFullname();
                        Contact = response.body().getObjectUser().getContact();
                        Email = response.body().getObjectUser().getEmail();

                        TextFullName.setText(FullName);
                        TextEmail.setText(Email);
                        TextContact.setText(Contact);

                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });



        FloatingActionButton fab = findViewById(R.id.myAccount_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ActivityEditProfile.class);
                startActivity(intent);
            }
        });
    }
}
