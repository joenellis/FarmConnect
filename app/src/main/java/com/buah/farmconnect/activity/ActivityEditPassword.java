package com.buah.farmconnect.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.buah.farmconnect.R;
import com.buah.farmconnect.view.CustomViewPager;

public class ActivityEditPassword extends AppCompatActivity {

    Toolbar mToolbar;
    TextInputEditText oldPassword;
    TextInputEditText newPassword;
    TextInputEditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        init();

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    public void init() {

        mToolbar = findViewById(R.id.toolbar);
        oldPassword = findViewById(R.id.editPassword_txtOldPassword);
        newPassword = findViewById(R.id.editPassword_txtNewPassword);
        confirmPassword = findViewById(R.id.editPassword_txtConfirmPassword);

    }

    private void updatePassword(){

        if (TextUtils.isEmpty(oldPassword.getText().toString())) {

            oldPassword.setError("Enter Your Old Password");

        } else if (TextUtils.isEmpty(newPassword.getText().toString())) {

            newPassword.setError("Enter Your New Password");

        } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {

            confirmPassword.setError("Enter Your Confirm Password");

        }else {

            String txtOldPassword = oldPassword.getText().toString().trim();
            String txtNewPassword = newPassword.getText().toString().trim();
            String txtConfirmOldPassword = confirmPassword.getText().toString().trim();


        }
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
}
