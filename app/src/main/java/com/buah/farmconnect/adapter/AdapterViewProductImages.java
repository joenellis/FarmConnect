package com.buah.farmconnect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buah.farmconnect.R;
import com.buah.farmconnect.object.ObjectProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewProductImages extends RecyclerView.Adapter<AdapterViewProductImages.ProductImageHolder> {

    private Context mContext;
    private ArrayList mImage;
    private ObjectProduct objectProductdetail;


    public AdapterViewProductImages(Context mContext, ArrayList mImage, ObjectProduct objectProductdetail) {
        this.mContext = mContext;
        this.mImage = mImage;
        this.objectProductdetail = objectProductdetail;
    }

    @Override
    public ProductImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.holder_, null);
        return new ProductImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductImageHolder holder, int position) {


        String video = objectProductdetail.getVideo();
        String audio = objectProductdetail.getAudio();
        Picasso.with(this.mContext).load(String.valueOf(mImage.get(position))).into(holder.productImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String video = objectProductdetail.getVideo();
                String audio = objectProductdetail.getAudio();
            }
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
