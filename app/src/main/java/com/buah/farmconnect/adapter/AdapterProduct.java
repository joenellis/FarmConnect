package com.buah.farmconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buah.farmconnect.object.ObjectProduct;
import com.buah.farmconnect.R;
import com.buah.farmconnect.activity.ActivityViewProduct;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ProductHolder> {

    private Context mContext;
    private List<ObjectProduct> products;

    public AdapterProduct(Context mContext, List<ObjectProduct> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.holder_product_item, null);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {

        final ObjectProduct product = products.get(position);

        holder.productName.setText(product.getProductname());
        holder.productPrice.setText(product.getPrice());
        Glide.with(this.mContext).load(product.getImage()).into(holder.productImage);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ActivityViewProduct.class);
                intent.putExtra("ID", product.getProduct_id());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        ImageView productImage;


        ProductHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.txtItem);
            productPrice = itemView.findViewById(R.id.txtPrice);
            productQuantity = itemView.findViewById(R.id.txtQuantity);
            productImage = itemView.findViewById(R.id.imgItem);
        }
    }
}
