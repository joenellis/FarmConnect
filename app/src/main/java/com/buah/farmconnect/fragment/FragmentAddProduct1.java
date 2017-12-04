package com.buah.farmconnect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.Upload;

public class FragmentAddProduct1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_1, container, false);


        TextInputEditText productName = view.findViewById(R.id.addProduct_txtProductName);
        TextInputEditText description = view.findViewById(R.id.addProduct_txtDescription);

        Upload.setProductName(productName.getText().toString());
        Upload.setDescription(description.getText().toString());

        return view;
    }
}
