package com.buah.farmconnect.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.buah.farmconnect.fragment.FragmentSignUp5;
import com.buah.farmconnect.R;
import com.buah.farmconnect.WebActivity;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.api.SignUp;
import com.buah.farmconnect.fragment.FragmentSignUp1;
import com.buah.farmconnect.fragment.FragmentSignUp2;
import com.buah.farmconnect.fragment.FragmentSignUp3;
import com.buah.farmconnect.fragment.FragmentSignUp4;
import com.buah.farmconnect.fragment.FragmentSignUp6;
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
    TextInputEditText answer;
    Spinner securityQuestion;



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

            password.setError("Enter Password!");

        } else {

            SignUp.setPassword(password.getText().toString().trim());
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);

        }

    }

    public void onNextClick5(View view) {

        securityQuestion = findViewById(R.id.signUp5_spnSecurityQ);
        answer = findViewById(R.id.signUp5_txtAnswer);

        int spinner_pos = securityQuestion.getSelectedItemPosition();
        String[] id_values = getResources().getStringArray(R.array.security_questions_id);
        int securityQuestion_Id = Integer.valueOf(id_values[spinner_pos]);

        if (securityQuestion_Id == 0) {

            Snackbar.make(
                    findViewById(R.id.forgotPassword1_rootLayout),
                    "Please Select A Security Question!",
                    Snackbar.LENGTH_LONG
            ).show();

        } else if (TextUtils.isEmpty(answer.getText().toString())) {

            answer.setError("Enter An Answer!");

        } else {

            SignUp.setSecurityQuestion_id(String.valueOf(securityQuestion_Id));
            SignUp.setAnswer(answer.getText().toString().trim());

            mPager.setCurrentItem(mPager.getCurrentItem() + 1);

        }

    }

    public void onTermsOfUseClick(View view) {

        Intent  terms_of_use_intent = new Intent(this, WebActivity.class);
        startActivity(terms_of_use_intent);

    }

    public void onPreviousClick(View view) {

        ViewGroup layPrevious = findViewById(R.id.signUp_layPrevious);
        layPrevious.setVisibility(View.VISIBLE);
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);

    }



    public void onSignUpClick(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        String fullname = SignUp.getFullName();
        String email = SignUp.getEmail();
        String contact = SignUp.getNumber();
        String password = SignUp.getPassword();

        String answer = SignUp.getAnswer();
        String securityQuestion_Id = SignUp.getSecurityQuestion_id();

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userSignup(fullname, email, password, contact, securityQuestion_Id, answer);


        call.enqueue(new Callback<Result>() {

            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {

                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getBaseContext(), ActivityLogin.class);
                        startActivity(intent);
                        finish();

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
            fragmentArrayList.add(new FragmentSignUp6());

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
