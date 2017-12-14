package com.buah.farmconnect.activity;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.buah.farmconnect.R;
import com.buah.farmconnect.fragment.FragmentForgotPassword1;
import com.buah.farmconnect.fragment.FragmentForgotPassword2;
import com.buah.farmconnect.view.CustomViewPager;

import java.util.ArrayList;

public class ActivityForgotPassword extends AppCompatActivity {

    Button mNextButton;

    Toolbar mToolbar;
    TextInputEditText mAnswer;
    TextInputEditText mNewPassword;
    TextInputEditText mConfirmPassword;

    Spinner mSecurityQuestions;

    PagerAdapter mPagerAdapter;
    CustomViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
    }

    public void onNextClick(View view) {

        int spinner_pos = mSecurityQuestions.getSelectedItemPosition();
        String[] id_values = getResources().getStringArray(R.array.security_questions_id);
        int securityQuestion_Id = Integer.valueOf(id_values[spinner_pos]);

        if (securityQuestion_Id == 0) {

            Snackbar.make(
                    findViewById(R.id.forgotPassword1_rootLayout),
                    "Please Select Your Security Question!",
                    Snackbar.LENGTH_LONG
            ).show();

        } else if (TextUtils.isEmpty(mAnswer.getText().toString())) {

            mAnswer.setError("Enter Answer!");

        } else if (verify(securityQuestion_Id)) {

            mPager.setCurrentItem(mPager.getCurrentItem() + 1);

        } else {

            Snackbar.make(
                    findViewById(R.id.forgotPassword1_rootLayout),
                    "Your Question or Answer is Invalid!",
                    Snackbar.LENGTH_LONG
            ).show();

        }
    }

    private boolean verify(int securityQuestion_Id) {

        String answer = mAnswer.getText().toString().trim();

        return false;
    }

    private void update() {

        String newPassword = mNewPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();

    }

    private void init() {

        mToolbar = findViewById(R.id.toolbar);
        mAnswer = findViewById(R.id.forgotPassword_txtAnswer);
        mNextButton = findViewById(R.id.forgotPassword_btnNext);
        mNewPassword = findViewById(R.id.forgotPassword_txtNewPassword);
        mConfirmPassword = findViewById(R.id.forgotPassword_txtConfirmPassword);
        mSecurityQuestions = findViewById(R.id.forgotPassword_securityQuestion);

        mPager = findViewById(R.id.forgotPassword_ViewPager);
        mPager.setPagingEnabled(false);
        mPagerAdapter = new ForgotPasswordPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_account_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                break;
            default:
                break;
        }

        return true;

    }

    private class ForgotPasswordPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        ForgotPasswordPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentArrayList.add(new FragmentForgotPassword1());
            fragmentArrayList.add(new FragmentForgotPassword2());
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
