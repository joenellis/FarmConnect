package com.buah.farmconnect.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.api.SignUp;
import com.buah.farmconnect.fragment.FragmentSignUp1;
import com.buah.farmconnect.fragment.FragmentSignUp2;
import com.buah.farmconnect.fragment.FragmentSignUp3;
import com.buah.farmconnect.fragment.FragmentSignUp4;
import com.buah.farmconnect.fragment.FragmentSignUp5;
import com.buah.farmconnect.view.CustomViewPager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignUp extends AppCompatActivity {

    CustomViewPager mPager;
    PagerAdapter mPagerAdapter;
    Toolbar mToolbar;

    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText number;
    TextInputEditText email;
    TextInputEditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Sign Up");
        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mPager = findViewById(R.id.sign_Up_viewPager);
        mPager.setPagingEnabled(false);
        mPagerAdapter = new SignUpPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    public void onNextClick1(View view) {
        firstName = findViewById(R.id.signUp1_txtFirstName);
        lastName = findViewById(R.id.signUp1_txtLastName);
        if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError("Enter First name");
        } else if (TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError("Enter Last name");
        } else {
            SignUp.setFirstName(firstName.getText().toString().trim());
            SignUp.setLastName(lastName.getText().toString().trim());
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    public void onNextClick2(View view) {
        number = findViewById(R.id.signUp2_txtNumber);
        if (TextUtils.isEmpty(number.getText().toString())) {
            number.setError("Enter Number!");
        } else {
            SignUp.setNumber(number.getText().toString().trim());
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    public void onNextClick3(View view) {
        email = findViewById(R.id.signUp3_txtEmail);
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Enter Email!");
        } else {
            SignUp.setEmail(email.getText().toString().trim());
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    public void onNextClick4(View view) {
        password = findViewById(R.id.signUp4_txtPassword);
        if (TextUtils.isEmpty(password.getText().toString())) {
            TextInputLayout x = findViewById(R.id.signUp4_txtPasswordLay);
            x.setPasswordVisibilityToggleEnabled(false);
            password.setError("Enter Password!");
        } else {
            SignUp.setPassword(password.getText().toString().trim());
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    public void onPreviousClick(View view) {
        ViewGroup layPrevious = findViewById(R.id.signUp_layPrevious);
        layPrevious.setVisibility(View.VISIBLE);
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    public void onPasswordEditTextClick(View view) {
        TextInputLayout x = findViewById(R.id.signUp4_txtPasswordLay);
        TextInputEditText x1 = (TextInputEditText) view;
        if (!x.isPasswordVisibilityToggleEnabled()){
            x.setPasswordVisibilityToggleEnabled(true);
        }

    }


    public void onSignUpClick(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        String fullName = SignUp.getFullName();
        String email = SignUp.getEmail();
        String contact = SignUp.getNumber();
        String password = SignUp.getPassword();

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userSignup(fullName, email, password, contact);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.body() != null) {
                    if (!response.body().getError()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
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

        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class SignUpPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        SignUpPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentArrayList.add(new FragmentSignUp1());
            fragmentArrayList.add(new FragmentSignUp2());
            fragmentArrayList.add(new FragmentSignUp3());
            fragmentArrayList.add(new FragmentSignUp4());
            fragmentArrayList.add(new FragmentSignUp5());
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
    }
}
