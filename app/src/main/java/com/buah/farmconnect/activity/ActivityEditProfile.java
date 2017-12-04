package com.buah.farmconnect.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.session.SharedPrefManager;
import com.buah.farmconnect.object.ObjectUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditProfile extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputEditText editTextFirstName,editTextLastName, editTextEmail, editTextPassword ,editTextContact;
    private String FirstName;
    private String Contact;
    private String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Edit your profile");
        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        editTextFirstName = findViewById(R.id.editProfile_txtFirstName);
        editTextLastName = findViewById(R.id.editProfile_txtLastName);
        editTextEmail = findViewById(R.id.editProfile_txtEmail);
        editTextContact = findViewById(R.id.editProfile_txtNumber);

        String userid = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getUser_id();
        String[] FullName = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getFullname().split(" ");
        String FirstName = FullName[0];
        String LastName = FullName[1];
        String Email = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getEmail();
        String Contact = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getContact();
        editTextFirstName.setText(FirstName);
        editTextLastName.setText(LastName);
        editTextEmail.setText(Email);
        editTextContact.setText(Contact);

//        /////test to call per product selected
//        Api api = new Api();
//        ApiCall service = api.getRetro().create(ApiCall.class);
//        Call<Result> call = service.productdetails("3");
//        call.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//
//                if (response.body() != null) {
//                    if (!response.body().getError()) {
//                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//      ;
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

    }

    public boolean onUpdateClick(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        String userid = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getUser_id();
        String fullname = editTextFirstName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userUpdate(userid,fullname,email,contact);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        onLoginBackground();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    return false;

    }

    public void onLoginBackground() {

        String uname = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getEmail();
        String password = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getPassword();
        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userLogin(uname, password);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (!response.body().getError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getObjectUser());
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_account_menu, menu);
        return true;
    }

    

    //        editTextPassword = findViewById(R.id.editProfile_)

}
