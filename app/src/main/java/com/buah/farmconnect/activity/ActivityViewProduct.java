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

import com.buah.farmconnect.adapter.AdapterViewProductImages;
import com.buah.farmconnect.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityViewProduct extends AppCompatActivity {

    View mBottomSheet;
    Toolbar mToolbar;
    TextView mPrice;
    Button mMapButton;
    Button mDescButton;
    TextView mQuantity;
    TextView mLocation;
    TextView mFarmerName;
    TextView mProductName;
    TextView mDescription;
    ImageView mProductImage;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    BottomSheetBehavior mBottomSheetBehavior;
    AdapterViewProductImages adapterViewProductImages;
    String productId;

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
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            productId = bundle.getString("ID");
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapterViewProductImages);
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
        mQuantity = findViewById(R.id.viewProduct_txtQuantity);
        mLocation = findViewById(R.id.viewProduct_txtLocation);
        mPrice = findViewById(R.id.viewProduct_txtPrice);
        adapterViewProductImages = new AdapterViewProductImages(
                this,
                new ArrayList<>(Arrays.asList(
                        R.drawable.tomato,
                        R.drawable.cabbage,
                        R.drawable.banana,
                        R.drawable.chicken
                ))
        );
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
}
