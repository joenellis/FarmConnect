package com.buah.farmconnect.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.AddProduct;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.fragment.FragmentDialogLocation;
import com.buah.farmconnect.view.CustomViewPager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditProduct extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private String productName, productImage, productImage1, productImage2, productImage3, productDescription, productPrice, productLocation, audio, video, categoryid;
    private String productId;
    // LogCat tag
    private static final String TAG = ActivityAddProduct.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec

    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    // UI elements
    private TextView lblLocation;

    private Button btnShowLocation;
    private double latitude, longitude;

    Toolbar mToolbar;

    public static boolean isRequestingLocation = false;
    public static String location;

    private String address;

    private String city;
    private String country;
    private String mImagePath1;
    private String mImagePath2;
    private String mImagePath3;
    private String mImagePath4;
    private String mVideoFilePath;
    private String mAudioFilePath;

    TextInputEditText mProductName;
    TextInputEditText mProductDescrition;
    TextInputEditText mProductPrice;
    Spinner mCategory;
    ImageButton mImageButton1;
    ImageButton mImageButton2;
    ImageButton mImageButton3;
    ImageButton mImageButton4;
    Button mAudioButton;
    Button mVideoButton;
    Button mCurrentLocationButton;
    Button mChooseLocationButton;


    boolean isRecordingAudio = false;

    private int GALLERY = 1;
    private int CAMERA = 2;
    private int VIDEO = 3;
    private int buttonId;
    private MediaRecorder mMediaRecorder;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 29;
    private int REQUEST_VIDEO_CAPTURE = 4;
    private boolean isclicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        init();

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Intent intent = getIntent();
        productId = intent.getStringExtra("ID");

        getProduct(productId);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories_name,
                android.R.layout.simple_list_item_1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(adapter);

        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int spinner_pos = mCategory.getSelectedItemPosition();
                String[] id_values = getResources().getStringArray(R.array.categories_id);
                int id = Integer.valueOf(id_values[spinner_pos]);

                if (id != 0) {
                    categoryid = String.valueOf(id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        mProductName = findViewById(R.id.editProduct_txtProductName);
        mProductDescrition = findViewById(R.id.editProduct_txtProductDescription);
        mProductPrice = findViewById(R.id.editProduct_txtPrice);
        mCategory = findViewById(R.id.editProduct_categories);
        mImageButton1 = findViewById(R.id.editProduct_btnAddImage1);
        mImageButton2 = findViewById(R.id.editProduct_btnAddImage2);
        mImageButton3 = findViewById(R.id.editProduct_btnAddImage3);
        mImageButton4 = findViewById(R.id.editProduct_btnAddImage4);
        mAudioButton = findViewById(R.id.editProduct_btnRecordAudio);
        mVideoButton = findViewById(R.id.editProduct_btnUploadVideo);
        mCurrentLocationButton = findViewById(R.id.editProduct_btnCurrentLocation);
        mChooseLocationButton = findViewById(R.id.editProduct_btnChooseLocation);
    }

    public void onAddImageClick(View view) {
        buttonId = view.getId();
        showPictureDialog(view.getId());
    }

    private void showPictureDialog(@IdRes final int buttonId) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery(buttonId);
                                break;
                            case 1:
                                takePhotoFromCamera(buttonId);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallery(int buttonId) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra("Button", buttonId);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera(@IdRes int buttonId) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("Button", buttonId);
        startActivityForResult(intent, CAMERA);
    }

    public void OnRecordAudioClick(View view) {
        Button record = (Button) view;
        if (!isRecordingAudio) {
            recordAudio();
            record.setText("Stop Recording Audio");
            isRecordingAudio = true;
        } else {
            stopRecordingAudio();
            record.setText("Start Recording Audio");
            AddProduct.setAudio(audio);
            isRecordingAudio = false;
        }
    }

    public void recordAudio() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            requestMicPermission();

        } else {

            startRecordingAudio();

        }
    }

    private void requestMicPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            Snackbar.make(findViewById(R.id.addProduct5_rootLayout),
                    "This is to let you record.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(ActivityEditProduct.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                        }
                    })
                    .show();

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

            // MY_PERMISSIONS_REQUEST_RECORD_AUDIO is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    public void OnRecordVideoClick(View view) {

        buttonId = view.getId();
        showVideoDialog(view.getId());

    }

    private void showVideoDialog(@IdRes final int buttonId) {

        AlertDialog.Builder videoDialog = new AlertDialog.Builder(this);
        videoDialog.setTitle("Select Action");

        String[] videoDialogItems = {
                "Select video from gallery",
                "Capture video from camera"
        };

        videoDialog.setItems(videoDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                chooseVideoFromGallery(buttonId);
                                break;
                            case 1:
                                takeVideoFromCamera(buttonId);
                                break;
                        }
                    }
                });

        videoDialog.show();

    }

    private void takeVideoFromCamera(int buttonId) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void chooseVideoFromGallery(int buttonId) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra("Button", buttonId);
        startActivityForResult(intent, VIDEO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    startRecordingAudio();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void startRecordingAudio() {

        mAudioFilePath = getExternalCacheDir().getPath();
        mAudioFilePath += "/audio_description.mp3";
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setOutputFile(mAudioFilePath);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {

            mMediaRecorder.prepare();

        } catch (IOException e) {

            Log.e("Media Recorder", e.getMessage());

        }

        mMediaRecorder.start();

    }

    public void stopRecordingAudio() {

        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;

        Snackbar.make(
                findViewById(R.id.editProduct_rootLayout),
                "Audio Recording Saved!",
                Snackbar.LENGTH_LONG
        ).show();

    }

    public void onChooseLocationClick(View view) {

        final android.app.FragmentManager fm = getFragmentManager();
        final FragmentDialogLocation p = new FragmentDialogLocation();
        p.show(fm, "Select Location");
        ActivityEditProduct.isRequestingLocation = true;
        isclicked = true;

    }

    private void getProduct(String productId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.productdetails(productId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        //onLoginBackground();

                        productName = response.body().getObjectProductdetail().getProductname();
                        productImage = response.body().getObjectProductdetail().getImage();
                        productImage1 = response.body().getObjectProductdetail().getImage1();
                        productImage2 = response.body().getObjectProductdetail().getImage2();
                        productImage3 = response.body().getObjectProductdetail().getImage3();
                        productDescription = response.body().getObjectProductdetail().getDescription();
                        productPrice = response.body().getObjectProductdetail().getPrice();
                        productLocation = response.body().getObjectProductdetail().getLocation();
                        audio = response.body().getObjectProductdetail().getAudio();
                        video = response.body().getObjectProductdetail().getVideo();
                        categoryid = response.body().getObjectProductdetail().getCategory_id();


                        mProductName.setText(productName);
                        mProductDescrition.setText(productDescription);
                        mProductPrice.setText(productPrice);

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra("Button", buttonId);

        try {
            if (requestCode == GALLERY) {
                if (id == R.id.editProduct_btnAddImage1) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query((selectedImage), filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    productImage = cursor.getString(columnIndex);

                    //Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.editProduct_btnAddImage2) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    productImage1 = cursor.getString(columnIndex);

                    //Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.editProduct_btnAddImage3) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    productImage2 = cursor.getString(columnIndex);

                    //Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.editProduct_btnAddImage4) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    productImage3 = cursor.getString(columnIndex);

                    //Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else {
                    Toast.makeText(this, "No id found", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == CAMERA) {
                Uri selectedImage = null;

                if (id == R.id.editProduct_btnAddImage1) {

                    selectedImage = data.getData();
                    mImagePath1 = selectedImage.getPath();

                    //Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.editProduct_btnAddImage2) {

                    selectedImage = data.getData();
                    mImagePath2 = selectedImage.getPath();

                    //Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.editProduct_btnAddImage3) {

                    selectedImage = data.getData();
                    mImagePath3 = selectedImage.getPath();

                   // Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.editProduct_btnAddImage4) {

                    selectedImage = data.getData();
                    mImagePath4 = selectedImage.getPath();

                    //Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);
                } else {
                    Toast.makeText(this, "No id found", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == VIDEO) {
                // Get the Video from data
                Uri selectedVideo = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                video = cursor.getString(columnIndex);
                cursor.close();

            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {

                Uri videoUri = intent.getData();
                video = videoUri.getPath();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


    }

    void setImageString(@IdRes int id) {
        TextView imageText;
        switch (id) {
            case R.id.addProduct_btnAddImage1:
                imageText = findViewById(R.id.addProduct_txtImageName1);
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct_btnAddImage2:
                imageText = findViewById(R.id.addProduct_txtImageName2);
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct_btnAddImage3:
                imageText = findViewById(R.id.addProduct_txtImageName3);
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct_btnAddImage4:
                imageText = findViewById(R.id.addProduct_txtImageName4);
                imageText.setText("Image Added!");
                break;

        }
    }

    ////////////Menu Items///////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_account_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_saveEditAccount:
                update();
                break;
        }

        return true;
    }

    //////////////////////GPS GOOGLE API CURRENT LOCATION
    public void onCurrentLocationClick(View view) {
        isclicked = true;
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
            return;
        }
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

            ActivityEditProduct.location = address;


            Snackbar.make(
                    findViewById(R.id.editProduct_rootLayout),
                    address,
                    Snackbar.LENGTH_LONG
            ).show();

        } else {

//            Snackbar.make(
//                    findViewById(R.id.editProduct_rootLayout),
//                    "Couldn't get the location. Make sure location is enabled on the device",
//                    Snackbar.LENGTH_INDEFINITE
//            ).show();
//           Toast.makeText(getApplicationContext(), "(Couldn't get the location. Make sure location is enabled on the device)", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
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

    private void update() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String pname = String.valueOf(mProductName.getText()).trim();
        String pdescription = String.valueOf(mProductDescrition.getText()).trim();
        String pprice = String.valueOf(mProductPrice.getText()).trim();
        if(isclicked){
            productLocation = ActivityEditProduct.location;
        }

        //Map is used to multipart the file using okhttp3.RequestBody
        File imageFile1 = new File(productImage);
        File imageFile2 = new File(productImage1);
        File imageFile3 = new File(productImage2);
        File imageFile4 = new File(productImage3);



        RequestBody product_id = RequestBody.create(MediaType.parse("multipart/form-data"), productId);
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), categoryid);
        RequestBody productname = RequestBody.create(MediaType.parse("multipart/form-data"), pname);
        RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), pprice);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), pdescription);
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


        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call;

        if (video != null && audio != null) {

            File videoFile = new File(video);
            File audioFile = new File(audio);

            RequestBody requestBody5 = RequestBody.create(MediaType.parse("*/*"), videoFile);
            RequestBody requestBody6 = RequestBody.create(MediaType.parse("*/*"), audioFile);

            MultipartBody.Part fileToUpload5 = MultipartBody.Part.createFormData("file5", videoFile.getName(), requestBody5);
            MultipartBody.Part fileToUpload6 = MultipartBody.Part.createFormData("file6", audioFile.getName(), requestBody6);

            call = service.updateProduct(product_id, category_id, productname, price, description, location, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, fileToUpload5, fileToUpload6);

        } else if (video != null) {

            File videoFile = new File(video);
            RequestBody requestBody5 = RequestBody.create(MediaType.parse("*/*"), videoFile);
            MultipartBody.Part fileToUpload5 = MultipartBody.Part.createFormData("file5", videoFile.getName(), requestBody5);
            call = service.updateProduct(product_id, category_id, productname, price, description, location, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, fileToUpload5);

        } else if (audio != null) {

            File audioFile = new File(audio);
            RequestBody requestBody6 = RequestBody.create(MediaType.parse("*/*"), audioFile);
            MultipartBody.Part fileToUpload6 = MultipartBody.Part.createFormData("file6", audioFile.getName(), requestBody6);
            call = service.updateProduct(product_id, category_id, productname, price, description, location, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, fileToUpload6);

        } else {

            call = service.updateProduct(product_id, category_id, productname, price, description, location, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4);

        }
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

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
    }

}