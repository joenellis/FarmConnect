package com.buah.farmconnect.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buah.farmconnect.adapter.AdapterProduct;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMore extends Fragment {

    GridLayoutManager layoutManager;
    RecyclerView mRecyclerViewMore;

    int spanCount;

    public FragmentMore() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        String args = getArguments().getString("Category");

        spanCount = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)? 3 : 2;

        layoutManager = new GridLayoutManager(getContext(), spanCount);
        mRecyclerViewMore = rootView.findViewById(R.id.recyclerViewMore);
        mRecyclerViewMore.setLayoutManager(layoutManager);
        mRecyclerViewMore.setHasFixedSize(true);


        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.productcaegory(args);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.body() != null) {
                    if (!response.body().getError()) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                        AdapterProduct adapter = new AdapterProduct(getContext(), response.body().getObjectProducts());
                        mRecyclerViewMore.setAdapter(adapter);

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}
