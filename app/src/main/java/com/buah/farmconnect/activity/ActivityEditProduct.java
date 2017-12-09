package com.buah.farmconnect.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.buah.farmconnect.R;

public class ActivityEditProduct extends AppCompatActivity {

    private Spinner category;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Intent intent = getIntent();
        productId = intent.getStringExtra("ID");

        category = findViewById(R.id.editProduct_categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories_name,
                android.R.layout.simple_list_item_1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
    }

    public void onAddImageClick(View view) {
    }

    public void OnRecordAudioClick(View view) {
    }

    public void OnRecordVideoClick(View view) {
    }

    public void onCurrentLocationClick(View view) {
    }

    public void onChooseLocationClick(View view) {
    }

}
//    int spinner_pos = category.getSelectedItemPosition();
//    String[] id_values = getResources().getStringArray(R.array.categories_id);
//    int id = Integer.valueOf(id_values[spinner_pos]);