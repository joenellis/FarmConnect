package com.buah.farmconnect.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.buah.farmconnect.fragment.BlankFragment;
import com.buah.farmconnect.fragment.FragmentAddProduct1;
import com.buah.farmconnect.fragment.FragmentAddProduct2;
import com.buah.farmconnect.fragment.FragmentAddProduct3;
import com.buah.farmconnect.fragment.FragmentAddProduct4;
import com.buah.farmconnect.fragment.FragmentAddProduct5;
import com.buah.farmconnect.view.CustomViewPager;

import java.io.IOException;
import java.util.ArrayList;

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

    String mediaPath;
    String mediaPath1;
    String mediaPath2;
    String mediaPath3;

    boolean isRecordingAudio = false;

    private int GALLERY = 1;
    private int CAMERA = 2;
    private int buttonId;
    private Button button;
    private MediaRecorder mMediaRecorder;
    private String mAudioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Add Product");
        setSupportActionBar(mToolbar);

        mAudioFilePath = getExternalCacheDir().getAbsolutePath();
        mAudioFilePath += "/audio_description.mp3";

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
        final BlankFragment p = new BlankFragment();
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
        }
    }

    public void onAddProductClick(View view) {
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

    public void recordAudio(){
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setOutputFile(mAudioFilePath);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mMediaRecorder.prepare();
        } catch (IOException e){
            Log.e("Media Recorder", e.getMessage());
        }

        mMediaRecorder.start();
    }


    public void stopRecordingAudio(){
       mMediaRecorder.stop();
       mMediaRecorder.release();
       mMediaRecorder = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra("Button", buttonId);


        if (requestCode == GALLERY) {
            if (id == R.id.addProduct_btnAddImage1) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query((selectedImage), filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage2) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath1 = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage3) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath2 = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage4) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath3 = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else {
                Toast.makeText(this, "No id found", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA) {

            if (id == R.id.addProduct_btnAddImage1) {

                Uri selectedImage = data.getData();
                mediaPath = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage2) {

                Uri selectedImage = data.getData();
                mediaPath1 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage3) {

                Uri selectedImage = data.getData();
                mediaPath2 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            } else if (id == R.id.addProduct_btnAddImage4) {

                Uri selectedImage = data.getData();
                mediaPath3 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);
            }
        }
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
