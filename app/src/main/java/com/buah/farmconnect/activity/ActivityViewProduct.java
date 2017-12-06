package com.buah.farmconnect.activity;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buah.farmconnect.adapter.AdapterViewProductImages;
import com.buah.farmconnect.R;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.squareup.picasso.Picasso;

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
                        audio = response.body().getObjectProductdetail().getAudio();
                        video = response.body().getObjectProductdetail().getVideo();


                        AdapterViewProductImages adapter = new AdapterViewProductImages(getApplicationContext(), mImages);
                        ;
                        mRecyclerView.setAdapter(adapter);

                        String productname = response.body().getObjectProductdetail().getProductname();
                        String image = response.body().getObjectProductdetail().getImage();
                        String description = response.body().getObjectProductdetail().getDescription();
                        String farmername = response.body().getObjectProductdetail().getFullname();
                        String price = response.body().getObjectProductdetail().getPrice();
                        String location = response.body().getObjectProductdetail().getLocation();

                        mProductName.setText(productname);
                        Picasso.with(getApplicationContext()).load(image).into(mProductImage);
                        mDescription.setText(description);
                        mFarmerName.setText(farmername);
                        mPrice.setText(price);
                        mLocation.setText(location);

                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        ;
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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
    }

    public void onPlayVideoClick(View view) {
    }
}
