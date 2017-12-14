package com.buah.farmconnect.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.buah.farmconnect.R;
import com.buah.farmconnect.adapter.AdapterViewProductImages;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.session.SharedPrefManager;
import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;

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
    private boolean isUploader;
    private String productName;
    private String productImage;
    private String productDescription;
    private String farmerName;
    private String productPrice;
    private String productLocation;

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
        productId = intent.getStringExtra("ID");

        mRecyclerView.setLayoutManager(layoutManager);

        final ProgressDialog progressDialog = new ProgressDialog(ActivityViewProduct.this);
        progressDialog.setMessage("Loading Product Details...");
        progressDialog.show();

        /////test to call per product selected
        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.productdetails(productId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progressDialog.dismiss();

                if (response.body() != null) {
                    if (!response.body().getError()) {

                        mImages = new ArrayList<>();
                        mImages.add(response.body().getObjectProductdetail().getImage());
                        mImages.add(response.body().getObjectProductdetail().getImage1());
                        mImages.add(response.body().getObjectProductdetail().getImage2());
                        mImages.add(response.body().getObjectProductdetail().getImage3());

                        AdapterViewProductImages adapter = new AdapterViewProductImages(getApplicationContext(), mImages);
                        mRecyclerView.setAdapter(adapter);

                        productName = response.body().getObjectProductdetail().getProductname();
                        productImage = response.body().getObjectProductdetail().getImage();
                        productDescription = response.body().getObjectProductdetail().getDescription();
                        farmerName = response.body().getObjectProductdetail().getFullname();
                        productPrice = response.body().getObjectProductdetail().getPrice();
                        productLocation = response.body().getObjectProductdetail().getLocation();
                        audio = response.body().getObjectProductdetail().getAudio();
                        video = response.body().getObjectProductdetail().getVideo();
                        contact = response.body().getObjectProductdetail().getContact();

                        mToolbar.setTitle(productName);
                        mProductName.setText(productName);
                        Glide.with(getApplicationContext()).load(productImage).into(mProductImage);
                        mDescription.setText(productDescription);
                        mFarmerName.setText(farmerName);
                        mPrice.setText(productPrice);
                        mLocation.setText(productLocation);
                        mCall.setText(contact);

                        if (audio == null) {
                            ViewGroup lay = findViewById(R.id.viewProduct_layAudioDescription);
                            lay.setVisibility(View.GONE);
                        }
                        if (video == null) {
                            ViewGroup lay = findViewById(R.id.viewProduct_layVideoDescription);
                            lay.setVisibility(View.GONE);
                        }
                        if (video == null && audio == null) {
                            Button button = findViewById(R.id.viewProduct_btnMoreDescription);
                            button.setVisibility(View.GONE);
                        }

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


        String bn = "geo:0,0?q=";

        Uri gmmIntentUri = Uri.parse(bn + productLocation);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Snackbar.make(
                    findViewById(R.id.viewProduct_layRoot),
                    "You have no suitable app",
                    Snackbar.LENGTH_LONG
            ).show();
        }


    }

    public void onAddToWishListClick(View view) {
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {

            addToWishList();

            Intent intent = new Intent(this, ActivityHome.class);
            intent.putExtra("Screen", "WishList");
            startActivity(intent);

        } else {

            Snackbar.make(
                    findViewById(R.id.rootMyWishList),
                    "Please Sign In To Complete This Action",
                    Snackbar.LENGTH_LONG
            ).show();

        }
    }

    private void addToWishList() {

    }

    public void onCallButtonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact, null));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onPlayAudioClick(View view) {

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(audio), "audio/*");
        startActivity(intent);

    }

    public void onPlayVideoClick(View view) {
        Intent myIntent = new Intent(this, VideoViewActivity.class);
        myIntent.putExtra("vUrl", video);
        this.startActivity(myIntent);

    }
}
