package com.buah.farmconnect.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buah.farmconnect.adapter.AdapterMyProduct;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.R;
import com.buah.farmconnect.session.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMyProduct extends Fragment {


    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_products, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewMyProducts);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        String userid = SharedPrefManager.getInstance(getActivity()).getobjectUser().getUser_id();




        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.mproducts(userid);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.body() != null) {
                if (!response.body().getError()) {
                    Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_LONG).show() ;

                    AdapterMyProduct adapter = new AdapterMyProduct(getContext(),response.body().getObjectProducts());
                    recyclerView.setAdapter(adapter);

                } else {
           //         Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_LONG).show();
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
