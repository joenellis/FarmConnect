package com.buah.farmconnect.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.buah.farmconnect.adapter.AdapterViewProductImages;
import com.buah.farmconnect.R;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.session.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewProduct extends AppCompatActivity {


    ArrayList<String> mImages;
    View mBottomSheet;
    Toolbar mToolbar;
    TextView mPrice;
    Button mMapButton;
    Button mDescButton;
    TextView mCall;
    TextView mLocation;
    TextView mFarmerName;
    TextView mProductName;
    TextView mDescription;
    ImageView mProductImage;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    BottomSheetBehavior mBottomSheetBehavior;
    String productId;
    private String audio;
    private String video;
    private String contact;
    VideoView videoview;
    ProgressDialog pDialog;
    FloatingActionButton mEditFab;
    private boolean isUploader ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        initializeComponents();
        bottomSheetHack();

        mToolbar.setTitle("Product Name");
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Product");

        Intent intent = getIntent();
        productId= intent.getStringExtra("ID");

        mRecyclerView.setLayoutManager(layoutManager);

        /////test to call per product selected
        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.productdetails(productId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.body() != null) {
                    if (!response.body().getError()) {

                        mImages = new ArrayList<>();
                        mImages.add(response.body().getObjectProductdetail().getImage1());
                        mImages.add(response.body().getObjectProductdetail().getImage2());
                        mImages.add(response.body().getObjectProductdetail().getImage3());

                        AdapterViewProductImages adapter = new AdapterViewProductImages(getApplicationContext(), mImages);
                        mRecyclerView.setAdapter(adapter);

                        String productname = response.body().getObjectProductdetail().getProductname();
                        String image = response.body().getObjectProductdetail().getImage();
                        String description = response.body().getObjectProductdetail().getDescription();
                        String farmername = response.body().getObjectProductdetail().getFullname();
                        String price = response.body().getObjectProductdetail().getPrice();
                        String location = response.body().getObjectProductdetail().getLocation();
                        audio = response.body().getObjectProductdetail().getAudio();
                        video = response.body().getObjectProductdetail().getVideo();
                        contact = response.body().getObjectProductdetail().getContact();

                        isUploader = farmername == SharedPrefManager.getInstance(ActivityViewProduct.this).getobjectUser().getFullname();
                        if (isUploader) {
                            mEditFab.setVisibility(View.VISIBLE);
                            mEditFab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }

                        mProductName.setText(productname);
                        Picasso.with(getApplicationContext()).load(image).into(mProductImage);
                        mDescription.setText(description);
                        mFarmerName.setText(farmername);
                        mPrice.setText(price);
                        mLocation.setText(location);
                        mCall.setText(contact);

                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_product_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_deleteProduct);

        if(isUploader) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_deleteProduct:
                deleteProduct();
                break;
        }

        return true;
    }

    private void deleteProduct() {
    }

    private void bottomSheetHack() {
        mBottomSheet.post(new Runnable() {
            @Override
            public void run() {
                mBottomSheetBehavior.setPeekHeight(0);
            }
        });
    }

    private void initializeComponents() {
        mToolbar = findViewById(R.id.viewProduct_toolbar);
        mRecyclerView = findViewById(R.id.viewProduct_recyclerViewForImages);
        mDescription = findViewById(R.id.viewProduct_txtDescriptionText);
        mDescButton = findViewById(R.id.viewProduct_btnMoreDescription);
        mProductImage = findViewById(R.id.viewProduct_imgProductImage);
        mProductName = findViewById(R.id.viewProduct_txtProductName);
        mBottomSheet = findViewById(R.id.viewProduct_bottomSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mMapButton = findViewById(R.id.viewProduct_btnViewOnMap);
        mFarmerName = findViewById(R.id.viewProduct_txtFarmName);
        mCall = findViewById(R.id.viewProduct_txtCall);
        mLocation = findViewById(R.id.viewProduct_txtLocation);
        mPrice = findViewById(R.id.viewProduct_txtPrice);
        videoview = findViewById(R.id.VideoView);
        mEditFab = findViewById(R.id.viewProduct_fabEdit);

        layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
    }

    public void onDescButtonClick(View view) {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void onMapButtonClick(View view) {
        Snackbar.make(
                findViewById(R.id.viewProduct_layRoot),
                "Opening Maps",
                Snackbar.LENGTH_LONG
        ).show();
    }

    public void onCallButtonClick(View view) {

    }

    public void onPlayAudioClick(View view) {

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(audio), "audio/*");
        startActivity(intent);

    }

    public void onPlayVideoClick(View view) {
        Intent myIntent = new Intent(this, VideoViewActivity.class);
        myIntent.putExtra("vUrl",video);
        this.startActivity(myIntent);

    }
}
