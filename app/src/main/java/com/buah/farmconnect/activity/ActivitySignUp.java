package com.buah.farmconnect.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buah.farmconnect.R;
import com.buah.farmconnect.WebActivity;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.api.SignUp;
import com.buah.farmconnect.view.CustomViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;

    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText number;
    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText dob;
    TextInputEditText location;
    TextInputEditText code;
    TextInputEditText profession;
    TextInputEditText orgainsation;
    TextInputEditText answer;
    Spinner securityQuestion;


    private String mFirstName;
    private String mLastName;
    private String mNumber;
    private String mEmail;
    private String mPassword;
    private String mDob;
    private String mLocation;
    private String mCode;
    private String mProfession;
    private String mOrganisation;
    private String mStatus = null;
    private String mCountry;
    private String mSecurityQuestion;
    private String mAnswer;
    private Button buttonSignUp;
    private RadioGroup radioGender;
    private DatePickerDialog datePickerDialog;


    private Spinner spinner;
    private String URL="http://techiesatish.com/demo_api/spinner.php";
    private  ArrayList<String> YearGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /////bind ids
        bindViews();

        ////Spinner foryear groups
        YearGroupName=new ArrayList<>();
        loadSpinnerData(URL);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCountry=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String mCountrynull = null;
            }
        });

    }
    private void loadSpinnerData(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getInt("success")==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("Name");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String country=jsonObject1.getString("Country");
                            YearGroupName.add(country);
                        }
                    }
                    spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, YearGroupName));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public void SignUp() {

        if (allDataSet()) {
          if(checkAccessCode()){
                createAccount();
          }
        }
    }


    private boolean allDataSet() {

        mFirstName = firstName.getText().toString().trim();
        mLastName = lastName.getText().toString().trim();
        mNumber = number.getText().toString().trim();
        mEmail = email.getText().toString().trim();
        mPassword = password.getText().toString().trim();
        mDob = dob.getText().toString().trim();
        mLocation= location.getText().toString().trim();
        mCode = code.getText().toString().trim();
        mProfession = profession.getText().toString().trim();
        mOrganisation = orgainsation.getText().toString().trim();

        final RadioButton status = findViewById(radioGender.getCheckedRadioButtonId());
        if (status != null) {
           mStatus = status.getText().toString();
        }
        //mAnswer = mTextAnswer.getText().toString().trim();
        //mSecurityQuestion = mSpinSecurityQuestion.getSelectedItem().toString();

        if (TextUtils.isEmpty(mFirstName)) {

            firstName.setError("Enter First Name");
            return false;

        } else if (TextUtils.isEmpty(mLastName)) {

            lastName.setError("Enter Last Name");
            return false;

        } else if (TextUtils.isEmpty(mLocation)) {

            location.setError("Enter Location");
            return false;

        } else if (TextUtils.isEmpty(mNumber)) {

            number.setError("Enter Number");
            return false;

        } else if (TextUtils.isEmpty(mDob)) {

            dob.setError("Enter Date Of Birth");
            return false;

        } else if (TextUtils.isEmpty(mEmail)) {

            email.setError("Enter Email Address");
            return false;

        } else if (TextUtils.isEmpty(mPassword)) {

            password.setError("Enter Your Password");
            return false;

        } else if (TextUtils.isEmpty(mProfession)) {

            profession.setError("Enter Profession");
            return false;

        } else if (TextUtils.isEmpty(mOrganisation)) {

            orgainsation.setError("Enter Organisation");
            return false;

        } else if (TextUtils.isEmpty(mCode)) {

            code.setError("Enter Year Group Access Code");
            return false;

        } else if (TextUtils.isEmpty(mStatus)) {
            Toast.makeText(this, "Please Select Employment Status", Toast.LENGTH_SHORT).show();
            return false;

        } else if (TextUtils.isEmpty(mCountry)) {
        Toast.makeText(this, "Please Select Year Gruop", Toast.LENGTH_SHORT).show();
        return false;

//        } else if (mSpinSecurityQuestion.getSelectedItemPosition() == 0) {
//
//            showMessage("Select Security Question!");
//            return false;
//
//        } else if (TextUtils.isEmpty(mAnswer)) {
//
//            mTextAnswer.setError("Enter Your Answer");
//            return false;

        } else {
            return true;
        }

    }


    private void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
        firstName =findViewById(R.id.signUp_txtFirstName);
        lastName =findViewById(R.id.signUp_txtLastName);
        number =findViewById(R.id.signUp_txtNumber);
        email =findViewById(R.id.signUp_txtEmail);
        password =findViewById(R.id.signUp_txtPassword);
        dob =findViewById(R.id.signUp_txtDob);
        radioGender =findViewById(R.id.signUp_radioEmploymentStatus);
        spinner=findViewById(R.id.country_Name);
        location=findViewById(R.id.signUp_txtLocation);
        code=findViewById(R.id.signUp_txtCode);
        profession=findViewById(R.id.signUp_txtProfession);
        orgainsation=findViewById(R.id.signUp_txtOrganisation);

        buttonSignUp = findViewById(R.id.signUp_btnSignUp);
        buttonSignUp.setOnClickListener(this);

    }


    private boolean checkAccessCode() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating Access Code...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userSignup();


        call.enqueue(new Callback<Result>() {

            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull retrofit2.Response<Result> response) {
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
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }

        });
        return true;
    }

    private void createAccount() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.userSignup();


        call.enqueue(new Callback<Result>() {

            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull retrofit2.Response<Result> response) {
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
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUp_btnSignUp:
                SignUp();
                break;
        }
    }
}
