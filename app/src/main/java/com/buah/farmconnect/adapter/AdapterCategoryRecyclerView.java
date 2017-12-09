package com.buah.farmconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.buah.farmconnect.R;
import com.buah.farmconnect.activity.ActivityViewProduct;
import com.buah.farmconnect.fragment.FragmentMore;
import com.buah.farmconnect.object.ObjectProduct;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.http.OPTIONS;

public class AdapterCategoryRecyclerView extends RecyclerView.Adapter<AdapterCategoryRecyclerView.CategoryHolder> {

    private  ArrayList<ArrayList<ObjectProduct>> categories;
    private Context mContext;
    private ArrayList mImage;
    private List<ObjectProduct> products;
    private String[] category = {"Tubers","Fruits","Vegetables", "Grains", "Dairy/Fish"};


    public AdapterCategoryRecyclerView(Context mContext, List<ObjectProduct> products, ArrayList image) {
        this.mContext = mContext;
        this.mImage = image;
        this.products = products;

    }

    public AdapterCategoryRecyclerView(Context mContext, ArrayList<ArrayList<ObjectProduct>> categories) {
        this.mContext = mContext;
        this.categories = categories;
    }


    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.holder_category, null);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {


        holder.mCategory.setText(category[position]);
        String browse = "Browse our "+ category[position].toLowerCase()+ " category.";
        holder.mSubtitle.setText(browse);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.mRecyclerView.setAdapter(new AdapterProduct(mContext, categories.get(position)));
        final Bundle bundle = new Bundle();
        bundle.putString("Category", category[position]);

        final Fragment fragment = new FragmentMore();
        fragment.setArguments(bundle);

        holder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragmentActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeLayout, fragment)
                    .addToBackStack("")
                    .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        Button mMore;
        TextView mCategory;
        TextView mSubtitle;
        RecyclerView mRecyclerView;


        CategoryHolder(View itemView) {
            super(itemView);

            mMore = itemView.findViewById(R.id.holderCategory_btnMore);
            mCategory = itemView.findViewById(R.id.holderCategory_txtCategory);
            mSubtitle = itemView.findViewById(R.id.holderCategory_txtSubtitle);
            mRecyclerView = itemView.findViewById(R.id.holderCategory_recyclerView);
        }
    }
}
