package com.buah.farmconnect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buah.farmconnect.R;

import java.util.ArrayList;

public class AdapterViewProductImages extends RecyclerView.Adapter<AdapterViewProductImages.ProductImageHolder> {

    private Context mContext;
    private ArrayList mImage;

    public AdapterViewProductImages(Context mContext, ArrayList mImage) {
        this.mContext = mContext;
        this.mImage = mImage;
    }

    @Override
    public ProductImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.holder_, null);
        return new ProductImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductImageHolder holder, int position) {

        holder.productImage.setImageResource(Integer.parseInt(mImage.get(position).toString()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    class ProductImageHolder extends RecyclerView.ViewHolder {

        ImageView productImage;

        ProductImageHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.holder_imageView);
        }
    }
}
