package com.buah.farmconnect.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.buah.farmconnect.R;
import com.buah.farmconnect.activity.ActivityAddProduct;
import com.buah.farmconnect.activity.ActivityEditProduct;
import com.buah.farmconnect.api.AddProduct;

public class FragmentDialogLocation extends DialogFragment {

    Button mDismissButton;
    ListView mListView;
    SearchView mSearchView;
    ArrayAdapter<String> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragmentdialog_location, null);

        //SET TITLE DIALOG TITLE
        getDialog().setTitle("Select City");

        //BUTTON,LISTVIEW,SEARCHVIEW INITIALIZATIONS
        mListView = rootView.findViewById(R.id.listView1);
        mSearchView = rootView.findViewById(R.id.searchView1);
        mDismissButton = rootView.findViewById(R.id.dismiss);

        //CREATE AND SET ADAPTER TO LISTVIEW
        mAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.cities));

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ActivityAddProduct.location = adapterView.getItemAtPosition(i).toString();
                if (ActivityEditProduct.isRequestingLocation)
                ActivityEditProduct.location = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(
                        getActivity().getApplicationContext(),
                        adapterView.getItemAtPosition(i).toString(),
                        Toast.LENGTH_LONG).show();

                dismiss();
            }
        });

        //SEARCH
        mSearchView.setQueryHint("Search Location");

        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String txt) {
                mAdapter.getFilter().filter(txt);
                return false;
            }
        });

        //BUTTON
        mDismissButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        return rootView;
    }
}