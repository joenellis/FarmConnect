package com.buah.farmconnect.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
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
import com.buah.farmconnect.fragment.FragmentAddProduct1;
import com.buah.farmconnect.fragment.FragmentAddProduct2;
import com.buah.farmconnect.fragment.FragmentAddProduct3;
import com.buah.farmconnect.fragment.FragmentAddProduct4;
import com.buah.farmconnect.fragment.FragmentAddProduct5;
import com.buah.farmconnect.fragment.FragmentDialogLocation;
import com.buah.farmconnect.session.SharedPrefManager;
import com.buah.farmconnect.view.CustomViewPager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

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

        mPager = findViewById(R.id.addProduct_viewPager);
        mPager.setPagingEnabled(false);
        mPagerAdapter = new AddProductPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        button = findViewById(R.id.addProduct_btnRecordAudio);

    }


    //Next Click Things
    public void onAddProductNextClick1(View view) {

        productName = findViewById(R.id.addProduct_txtProductName);
        productDescription = findViewById(R.id.addProduct_txtDescription);

        if (TextUtils.isEmpty(productName.getText().toString())) {

            productName.setError("Enter Product Name");

        } else if (TextUtils.isEmpty(productDescription.getText().toString())) {

            productDescription.setError("Enter Product Description");

        } else {

            AddProduct.setProductName(productName.getText().toString().trim());
            AddProduct.setDescription(productDescription.getText().toString().trim());
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);

        }

    }

    public void onAddProductNextClick2(View view) {
        productPrice = findViewById(R.id.addProduct_txtPrice);
        productCategory = findViewById(R.id.addProduct_categories);

        int spinner_pos = productCategory.getSelectedItemPosition();
        String[] id_values = getResources().getStringArray(R.array.categories_id);
        int id = Integer.valueOf(id_values[spinner_pos]);

        if (TextUtils.isEmpty(productPrice.getText().toString())) {
            productPrice.setError("Enter Price of Product!");
        } else if (id == 0) {
            Snackbar.make(
                    findViewById(R.id.addProduct2_rootLayout),
                    "Please Select Category",
                    Snackbar.LENGTH_LONG
            ).show();
        } else {
            AddProduct.setPrice(productPrice.getText().toString().trim());
            AddProduct.setCategory_id(String.valueOf(id));
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    public void onAddProductNextClick3(View view) {
        if (ActivityAddProduct.location.equals("")) {
            Snackbar.make(
                    findViewById(R.id.addProduct2_rootLayout),
                    "Please Select a Location",
                    Snackbar.LENGTH_LONG
            ).show();
        } else {
            AddProduct.setLocation(ActivityAddProduct.location);
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    public void onAddProductNextClick4(View view) {

        TextView imageText1 = findViewById(R.id.addProduct4_txtImageName1);
        TextView imageText2 = findViewById(R.id.addProduct4_txtImageName2);
        TextView imageText3 = findViewById(R.id.addProduct4_txtImageName3);
        TextView imageText4 = findViewById(R.id.addProduct4_txtImageName4);

        if (imageText1.getText().length() <= 0) {

            Snackbar.make(
                    findViewById(R.id.addProduct4_rootLayout),
                    "Please Select Four Images",
                    Snackbar.LENGTH_LONG
            ).show();

        } else if (imageText2.getText().length() <= 0) {

            Snackbar.make(
                    findViewById(R.id.addProduct4_rootLayout),
                    "Please Select Four Images",
                    Snackbar.LENGTH_LONG
            ).show();

        } else if (imageText3.getText().length() <= 0) {

            Snackbar.make(
                    findViewById(R.id.addProduct4_rootLayout),
                    "Please Select Four Images",
                    Snackbar.LENGTH_LONG
            ).show();

        } else if (imageText4.getText().length() <= 0) {

            Snackbar.make(
                    findViewById(R.id.addProduct4_rootLayout),
                    "Please Select Four Images",
                    Snackbar.LENGTH_LONG
            ).show();

        } else {

            mPager.setCurrentItem(mPager.getCurrentItem() + 1);

        }


    }

    public void onChooseLocationClick(View view) {

        final android.app.FragmentManager fm = getFragmentManager();
        final FragmentDialogLocation p = new FragmentDialogLocation();
        p.show(fm, "Select Location");
        ActivityEditProduct.isRequestingLocation = false;
    }


    //Image Things
    public void onAddImageClick(View view) {
        buttonId = view.getId();
        showPictureDialog(view.getId());
    }

    private void showPictureDialog(@IdRes final int btnId) {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");

        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"
        };

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                choosePhotoFromGallery(btnId);
                                break;
                            case 1:
                                takePhotoFromCamera(btnId);
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

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;

            try {

                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.buah.farmconnect.fileprovider",
                        photoFile);

                takePictureIntent.putExtra("Button", buttonId);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, CAMERA);


                setImageString(buttonId, photoURI.getPath());
            }

        }

    }

    void setImageString(@IdRes int id, String fileLocation) {
        TextView imageText;
        switch (id) {
            case R.id.addProduct4_btnAddImage1:
                imageText = findViewById(R.id.addProduct4_txtImageName1);
                mImagePath1 = fileLocation;
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct4_btnAddImage2:
                imageText = findViewById(R.id.addProduct4_txtImageName2);
                mImagePath2 = fileLocation;
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct4_btnAddImage3:
                imageText = findViewById(R.id.addProduct4_txtImageName3);
                mImagePath3 = fileLocation;
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct4_btnAddImage4:
                imageText = findViewById(R.id.addProduct4_txtImageName4);
                mImagePath4 = fileLocation;
                imageText.setText("Image Added!");
                break;

        }
    }

    void setImageString(@IdRes int id) {
        TextView imageText;
        switch (id) {
            case R.id.addProduct4_btnAddImage1:
                imageText = findViewById(R.id.addProduct4_txtImageName1);
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct4_btnAddImage2:
                imageText = findViewById(R.id.addProduct4_txtImageName2);
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct4_btnAddImage3:
                imageText = findViewById(R.id.addProduct4_txtImageName3);
                imageText.setText("Image Added!");
                break;
            case R.id.addProduct4_btnAddImage4:
                imageText = findViewById(R.id.addProduct4_txtImageName4);
                imageText.setText("Image Added!");
                break;

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //Record Audio Things
    public void OnRecordAudioClick(View view) {
        Button record = (Button) view;
        if (!isRecordingAudio) {
            recordAudio();
            record.setText("Stop Recording Audio");
            isRecordingAudio = true;
        } else {
            stopRecordingAudio();
            record.setText("Start Recording Audio");
            AddProduct.setAudio(mAudioFilePath);
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
                            ActivityCompat.requestPermissions(ActivityAddProduct.this,
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
                findViewById(R.id.addProduct5_rootLayout),
                "Audio Recording Saved!",
                Snackbar.LENGTH_LONG
        ).show();

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


    //Result Things
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra("Button", buttonId);

        try {
            if (requestCode == GALLERY) {
                if (id == R.id.addProduct4_btnAddImage1) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query((selectedImage), filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mImagePath1 = cursor.getString(columnIndex);

                    Snackbar.make(findViewById(R.id.addProduct3_rootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.addProduct4_btnAddImage2) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mImagePath2 = cursor.getString(columnIndex);

                    Snackbar.make(findViewById(R.id.addProduct3_rootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.addProduct4_btnAddImage3) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mImagePath3 = cursor.getString(columnIndex);

                    Snackbar.make(findViewById(R.id.addProduct3_rootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.addProduct4_btnAddImage4) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mImagePath4 = cursor.getString(columnIndex);

                    Snackbar.make(findViewById(R.id.addProduct3_rootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else {
                    Toast.makeText(this, "No id found", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == CAMERA) {

//
//                if (id == R.id.addProduct4_btnAddImage1) {
//
//                    selectedImage = (Uri) data.getExtras().get("data");
//                    mImagePath1 = getRealPathFromURI(this, selectedImage);
//
//                    Snackbar.make(findViewById(R.id.addProduct3_rootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
//                    setImageString(buttonId);
//
//                } else if (id == R.id.addProduct4_btnAddImage2) {
//
//                    File file = new File(getExternalCacheDir(),
//                            String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    selectedImage = Uri.fromFile(file);
//
//
//                    mImagePath2 = selectedImage.getPath();
//                    Snackbar.make(findViewById(R.id.addProduct3_rootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
//                    setImageString(buttonId);
//
//                } else if (id == R.id.addProduct4_btnAddImage3) {
//
//                    File file = new File(getExternalCacheDir(),
//                            String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    selectedImage = Uri.fromFile(file);
//
//
//                    mImagePath3 = selectedImage.getPath();
//
//                    Snackbar.make(findViewById(R.id.addProduct3_rootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
//                    setImageString(buttonId);
//
//                } else if (id == R.id.addProduct4_btnAddImage4) {
//
//                    File file = new File(getExternalCacheDir(),
//                            String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    selectedImage = Uri.fromFile(file);
//
//
//                    mImagePath4 = selectedImage.getPath();
//
//                    Snackbar.make(findViewById(R.id.addProduct3_rootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
//                    setImageString(buttonId);
//                } else {
//                    Toast.makeText(this, "No id found", Toast.LENGTH_SHORT).show();
//                }
            } else if (requestCode == VIDEO_PICK) {
                // Get the Video from data
                Uri selectedVideo = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mVideoFilePath = cursor.getString(columnIndex);
                cursor.close();

            } else if (requestCode == VIDEO_CAPTURE) {


                if (resultCode == RESULT_OK) {

//                    video_uri = data.getData();
//                    mVideoView.setVideoURI(video_uri);
                    String videoRealPath = getRealPathFromURI(this, data.getData());


                    mVideoFilePath = videoRealPath;

                } else if (resultCode == RESULT_CANCELED) {

                    Toast.makeText(this, "Video recording cancelled.",
                            Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, "Failed to record video",
                            Toast.LENGTH_LONG).show();

                }


            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

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


            Snackbar.make(
                    findViewById(R.id.addProduct3_rootLayout),
                    address,
                    Snackbar.LENGTH_LONG
            ).show();

        } else {

            Snackbar.make(
                    findViewById(R.id.addProduct3_rootLayout),
                    "Couldn't get the location. Make sure location is enabled on the device",
                    Snackbar.LENGTH_INDEFINITE
            ).show();
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

    private class AddProductPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        AddProductPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentArrayList.add(new FragmentAddProduct1());
            fragmentArrayList.add(new FragmentAddProduct2());
            fragmentArrayList.add(new FragmentAddProduct3());
            fragmentArrayList.add(new FragmentAddProduct4());
            fragmentArrayList.add(new FragmentAddProduct5());
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
