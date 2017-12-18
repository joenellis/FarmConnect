package com.buah.farmconnect.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.R;
import com.buah.farmconnect.adapter.AdapterProduct;
import com.buah.farmconnect.object.ObjectProduct;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHome extends Fragment {

    final private String KEY = "GET";

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerViewHome;

    int spanCount;
    private AdapterProduct adapter;
    private ArrayList<ObjectProduct> products;

    public FragmentHome() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        spanCount = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)? 3 : 2;

        layoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerViewHome = rootView.findViewById(R.id.recyclerViewHome);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);

        Api api = new Api();
        ApiCall service = api.getRetro().create(ApiCall.class);
        Call<Result> call = service.products(KEY);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.body() != null) {
                    if (!response.body().getError()) {
//                      Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        products = response.body().getObjectProducts();
                        adapter = new AdapterProduct(getContext(), products);
                        recyclerViewHome.setAdapter(adapter);

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.home_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final List<ObjectProduct> filteredModelList = adapter.filter(products, newText);
                adapter.animateTo(filteredModelList);
                recyclerViewHome.scrollToPosition(0);

                return true;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       // displaySelectedScreen(item.getItemId());
        return true;

    }

}
