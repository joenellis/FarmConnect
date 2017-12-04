package com.buah.farmconnect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.buah.farmconnect.R;

public class FragmentAddProduct3 extends Fragment implements AdapterView.OnItemSelectedListener{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_3, container, false);

        Spinner cities = view.findViewById(R.id.addProduct_cites);
        Spinner regions = view.findViewById(R.id.addProduct_regions);
        Button location = view.findViewById(R.id.addProduct_btnCurrentLocation);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.cities,
                android.R.layout.simple_list_item_1
        );
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cities.setAdapter(cityAdapter);
        cities.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.regions,
                android.R.layout.simple_list_item_1
        );
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regions.setAdapter(regionAdapter);
        regions.setOnItemSelectedListener(this);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

         parent.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
