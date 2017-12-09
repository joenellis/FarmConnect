package com.buah.farmconnect.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddProduct extends AppCompatActivity {

    public static String location = "";

    Toolbar mToolbar;
    CustomViewPager mPager;
    PagerAdapter mPagerAdapter;

    TextInputEditText productName;
    TextInputEditText productDescription;
    TextInputEditText productPrice;
    Spinner productCategory;
    Spinner productLocationRegion;
    Spinner productLocationCity;

    private String mImagePath1;
    private String mImagePath2;
    private String mImagePath3;
    private String mImagePath4;
    private String mVideoFilePath;
    private String mAudioFilePath;

    boolean isRecordingAudio = false;

    private int GALLERY = 1;
    private int CAMERA = 2;
    private int VIDEO = 3;
    private int buttonId;
    private Button button;
    private MediaRecorder mMediaRecorder;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 29;

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

    public void onChooseLocationClick(View view) {

        final android.app.FragmentManager fm = getFragmentManager();
        final FragmentDialogLocation p = new FragmentDialogLocation();
        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                p.show(fm, "Select Location");
            }
        });
    }

    public void onAddProductNextClick4(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
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
            AddProduct.setAudio(mAudioFilePath);
            isRecordingAudio = false;
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
        progressDialog.setMessage("Logging In...");
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

                if (!response.body().getError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityAddProduct.this, ActivityHome.class);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
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
                if (id == R.id.addProduct_btnAddImage1) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query((selectedImage), filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mImagePath1 = cursor.getString(columnIndex);

                    Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.addProduct_btnAddImage2) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mImagePath2 = cursor.getString(columnIndex);

                    Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                    setImageString(buttonId);

                } else if (id == R.id.addProduct_btnAddImage3) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mImagePath3 = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage4) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mImagePath4 = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else {
                Toast.makeText(this, "No id found", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA) {
            Uri selectedImage = null;

            if (id == R.id.addProduct_btnAddImage1) {

                selectedImage = data.getData();
                mImagePath1 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage2) {

                selectedImage = data.getData();
                mImagePath2 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage3) {

                selectedImage = data.getData();
                mImagePath3 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage4) {

                selectedImage = data.getData();
                mImagePath4 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);
            }else {
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
                mVideoFilePath = cursor.getString(columnIndex);
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


    }

    public void OnRecordVideoClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, VIDEO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
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
                findViewById(R.id.addProduct5_rootLayout),
                "Audio Recording Saved!",
                Snackbar.LENGTH_LONG
        ).show();

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
