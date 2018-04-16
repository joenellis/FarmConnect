package com.buah.farmconnect.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.AddProduct;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.fragment.FragmentDialogLocation;
import com.buah.farmconnect.session.SharedPrefManager;
import com.buah.farmconnect.view.CustomViewPager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddProduct extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // LogCat tag
    private static final String TAG = ActivityAddProduct.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    private static final String IMAGE_DIRECTORY = "/farmconnectDCIM";

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 25;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private File f;

    private Uri fileUri;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    Uri selectedImage;
    // UI elements
    private double latitude, longitude;

    public static String location = "";

    Toolbar mToolbar;
    CustomViewPager mPager;
    PagerAdapter mPagerAdapter;

    TextInputEditText productName;
    TextInputEditText productDescription;
    TextInputEditText productPrice;
    Spinner productCategory;

    private String address;
    private String city;
    private String country;

    private String mImagePath1;
    private String mImagePath2;
    private String mImagePath3;
    private String mImagePath4;
    private String mVideoFilePath;
    private String mAudioFilePath;

    boolean isRecordingAudio = false;

    private int GALLERY = 1;
    private int CAMERA = 2;
    private int VIDEO_PICK = 3;
    private int buttonId;
    private Button button;
    private MediaRecorder mMediaRecorder;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 29;
    private final int VIDEO_CAPTURE = 101;
    private Uri mVideoFileUri;
    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Add Product");
        setSupportActionBar(mToolbar);


        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }


    //Image Things
    public void onAddImageClick(View view) {
        buttonId = view.getId();
        //showPictureDialog(view.getId());
    }



    private void choosePhotoFromGallery(int buttonId) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra("Button", buttonId);

        startActivityForResult(galleryIntent, GALLERY);

    }


    public void onAddProductClick(View view) {

        String id = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getUser_id();
        String category_id = AddProduct.getCategory_id();
        String productName = AddProduct.getProductName();
        String productPrice = AddProduct.getPrice();
        String productDescription = AddProduct.getDescription();
        String productLocation = AddProduct.getLocation();


        //Map is used to multipart the file using okhttp3.RequestBody
        File imageFile1 = new File(mImagePath1);
        File imageFile2 = new File(mImagePath2);
        File imageFile3 = new File(mImagePath3);
        File imageFile4 = new File(mImagePath4);


        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        RequestBody categoryid = RequestBody.create(MediaType.parse("multipart/form-data"), category_id);
        RequestBody productname = RequestBody.create(MediaType.parse("multipart/form-data"), productName);
        RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), productPrice);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), productDescription);
        RequestBody location = RequestBody.create(MediaType.parse("multipart/form-data"), productLocation);

        //Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), imageFile1);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), imageFile2);
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("*/*"), imageFile3);
        RequestBody requestBody4 = RequestBody.create(MediaType.parse("*/*"), imageFile4);

        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", imageFile1.getName(), requestBody1);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file2", imageFile2.getName(), requestBody2);
        MultipartBody.Part fileToUpload3 = MultipartBody.Part.createFormData("file3", imageFile3.getName(), requestBody3);
        MultipartBody.Part fileToUpload4 = MultipartBody.Part.createFormData("file4", imageFile4.getName(), requestBody4);


        final ProgressDialog progressDialog = new ProgressDialog(ActivityAddProduct.this);
        progressDialog.setMessage("Adding Product. Please wait...");
        progressDialog.show();

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call;

        if (mVideoFilePath != null && mAudioFilePath != null) {

            File videoFile = new File(mVideoFilePath);
            File audioFile = new File(mAudioFilePath);

            RequestBody requestBody5 = RequestBody.create(MediaType.parse("*/*"), videoFile);
            RequestBody requestBody6 = RequestBody.create(MediaType.parse("*/*"), audioFile);

            MultipartBody.Part fileToUpload5 = MultipartBody.Part.createFormData("file5", videoFile.getName(), requestBody5);
            MultipartBody.Part fileToUpload6 = MultipartBody.Part.createFormData("file6", audioFile.getName(), requestBody6);

            call = service.uploadMulFile(userid, categoryid, productname, price, description, location, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, fileToUpload5, fileToUpload6);

        } else if (mVideoFilePath != null) {

            File videoFile = new File(mVideoFilePath);
            RequestBody requestBody5 = RequestBody.create(MediaType.parse("*/*"), videoFile);
            MultipartBody.Part fileToUpload5 = MultipartBody.Part.createFormData("file5", videoFile.getName(), requestBody5);
            call = service.uploadMulFile(userid, categoryid, productname, price, description, location, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, fileToUpload5);

        } else if (mAudioFilePath != null) {

            File audioFile = new File(mAudioFilePath);
            RequestBody requestBody6 = RequestBody.create(MediaType.parse("*/*"), audioFile);
            MultipartBody.Part fileToUpload6 = MultipartBody.Part.createFormData("file6", audioFile.getName(), requestBody6);
            call = service.uploadMulFile(userid, categoryid, productname, price, description, location, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, fileToUpload6);

        } else {

            call = service.uploadMulFile(userid, categoryid, productname, price, description, location, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4);

        }

        call.enqueue(new Callback<Result>() {

            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progressDialog.dismiss();

                if (response.body() != null) {
                    if (!response.body().getError()) {

                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityAddProduct.this, ActivityHome.class);
                        startActivity(intent);
                    }
                }


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
            }

        });
    }


    //Record Video Things
    public void OnRecordVideoClick(View view) {

        buttonId = view.getId();
        showVideoDialog();

    }

    private void showVideoDialog() {

        AlertDialog.Builder videoDialog = new AlertDialog.Builder(this);
        videoDialog.setTitle("Select Action");

        String[] videoDialogItems = {
                "Select video from gallery",
                "Record video from camera"
        };

        videoDialog.setItems(videoDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                chooseVideoFromGallery();
                                break;
                            case 1:
                                recordVideoFromCamera();
                                break;
                        }

                    }
                });

        videoDialog.show();
    }

    private void recordVideoFromCamera() {

        File mediaFile = new
                File(getCacheDir().getPath()
                + "/myvideo.mp4");
        mVideoFileUri = Uri.fromFile(mediaFile);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoFileUri);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    private void chooseVideoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(intent, VIDEO_PICK);

    }



    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    //Location Things
    public void onCurrentLocationClick(View view) {
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            if ((buildGoogleApiClient())) {
                onStartConn();

                if ((onStartConn())) {
                    checkPlayServices();
                }
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized boolean buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        return true;
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        requestLocPermission();
        onStartLoc();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        onStopCon();

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();


            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            country = addresses.get(0).getCountryName();

            ActivityAddProduct.location = address;
            AddProduct.setLocation(location);


        } else {

//           Toast.makeText(getApplicationContext(), "(Couldn't get the location. Make sure location is enabled on the device)", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    private boolean requestLocPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_RECORD_AUDIO is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
        return true;
    }

    public boolean onStartLoc() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        return true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        SQLException result = null;
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    public boolean onStartConn() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    public void onStopCon() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
