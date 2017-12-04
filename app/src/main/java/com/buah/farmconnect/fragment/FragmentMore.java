package com.buah.farmconnect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buah.farmconnect.object.ObjectPlay;
import com.buah.farmconnect.R;
import com.buah.farmconnect.adapter.AdapterProductMore;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentMore extends Fragment {

    ArrayList productNames = new ArrayList<>(Arrays.asList("Tomatoes", "Cabbage", "Banana ", "Chicken", "Cow", "Person 6", "Person 7", "Person 8", "Person 9", "Person 10", "Person 11", "Person 12", "Person 13", "Person 14"));
    ArrayList image = new ArrayList<>(Arrays.asList(
            R.drawable.tomato,
            R.drawable.cabbage,
            R.drawable.banana,
            R.drawable.chicken,
            R.drawable.cow,
            R.drawable.tomato,
            R.drawable.cabbage,
            R.drawable.banana,
            R.drawable.chicken,
            R.drawable.cow,
            R.drawable.tomato,
            R.drawable.cabbage,
            R.drawable.banana,
            R.drawable.chicken
    ));

    StaggeredGridLayoutManager layoutManager;
    RecyclerView mRecyclerViewMore;

    ObjectPlay products = new ObjectPlay();

    public FragmentMore() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        mRecyclerViewMore = rootView.findViewById(R.id.recyclerViewMore);
        mRecyclerViewMore.setLayoutManager(layoutManager);
        mRecyclerViewMore.setAdapter(new AdapterProductMore(getContext(), products.getObjectProducts(), image));

        return rootView;
    }
}
