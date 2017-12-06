package com.buah.farmconnect.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buah.farmconnect.R;
import com.buah.farmconnect.fragment.FragmentAddProduct1;
import com.buah.farmconnect.fragment.FragmentAddProduct2;
import com.buah.farmconnect.fragment.FragmentAddProduct3;
import com.buah.farmconnect.fragment.FragmentAddProduct4;
import com.buah.farmconnect.fragment.FragmentAddProduct5;
import com.buah.farmconnect.view.CustomViewPager;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityAddProduct extends AppCompatActivity {

    CustomViewPager mPager;
    PagerAdapter mPagerAdapter;
    Toolbar mToolbar;
    String mediaPath, mediaPath1,mediaPath2,mediaPath3;
    private int GALLERY = 1;
    private int CAMERA = 2;
    private int buttonId;
    private Button button;

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

    public void onAddProductNextClick(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    public void onAddImageClick(View view) {
        buttonId = view.getId();
        showPictureDialog(view.getId());
    }

    public void onAddProductClick(View view) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra("Button", buttonId);


        if (requestCode == GALLERY) {
            if (id == R.id.addProduct_btnAddImage1 ) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query((selectedImage), filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            }else if (id == R.id.addProduct_btnAddImage2 ) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath1 = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            }else if (id == R.id.addProduct_btnAddImage3 ) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath2 = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            }else if(id == R.id.addProduct_btnAddImage4 ) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath3 = cursor.getString(columnIndex);

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            }else {
                Toast.makeText(this, "No id found", Toast.LENGTH_SHORT).show();
            }

        } else if(requestCode == CAMERA) {

            if (id == R.id.addProduct_btnAddImage1 ) {

                Uri selectedImage = data.getData();
                mediaPath = selectedImage.getPath();

             Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

        }else if(id == R.id.addProduct_btnAddImage2){

                Uri selectedImage = data.getData();
                mediaPath1 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            }else if(id == R.id.addProduct_btnAddImage3){

                Uri selectedImage = data.getData();
                mediaPath2 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);

            }else if(id == R.id.addProduct_btnAddImage4){

                Uri selectedImage = data.getData();
                mediaPath3 = selectedImage.getPath();

                Snackbar.make(findViewById(R.id.loginRootLayout), "Image Added!", Snackbar.LENGTH_LONG).show();
                setImageString(buttonId);
            }
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
