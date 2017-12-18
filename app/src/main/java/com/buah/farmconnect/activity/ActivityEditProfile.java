package com.buah.farmconnect.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.session.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditProfile extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputEditText editTextFirstName,editTextLastName, editTextEmail, editTextPassword ,editTextContact;
    private String FirstName;
    private String Contact;
    private String Email;
    private String usr,pwd;

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

        editTextFirstName = findViewById(R.id.editProduct_txtProductName);
        editTextLastName = findViewById(R.id.editProduct_txtProductDescription);
        editTextEmail = findViewById(R.id.editProduct_txtEmail);
        editTextContact = findViewById(R.id.editProduct_txtPrice);

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

    }

    public void updateChanges() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        String userid = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getUser_id();
        String firstname = editTextFirstName.getText().toString().trim();
        String lastname = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String fullname = firstname +" "+ lastname;

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userUpdate(userid,fullname,email,contact);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        usr = response.body().getObjectUser().getEmail();
                        pwd = response.body().getObjectUser().getPassword();
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

            }
        });
    }

    public void onLoginBackground() {

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userLogin(usr, pwd);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (!response.body().getError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getObjectUser());
                    startActivity(new Intent(getBaseContext(), ActivityMyAccount.class));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_saveEditAccount:
                updateChanges();
                break;
        }

        return true;
    }

}
