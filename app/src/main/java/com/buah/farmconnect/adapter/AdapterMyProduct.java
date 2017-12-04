package com.buah.farmconnect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buah.farmconnect.object.ObjectProduct;
import com.buah.farmconnect.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterMyProduct extends RecyclerView.Adapter<AdapterMyProduct.ProductHolder> {

    private Context mContext;
    private List<ObjectProduct> mProducts;

    public AdapterMyProduct(Context mContext, List<ObjectProduct> mProducts){
        this.mContext = mContext;
        this.mProducts = mProducts;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.holder_my_product, null);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {

        ObjectProduct product = mProducts.get(position);

        holder.text.setText(product.getProductname());
        holder.text1.setText(product.getPrice());
        holder.text2.setText(product.getLocation());
        Picasso.with(this.mContext).load(product.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{

        TextView text;
        TextView text1;
        TextView text2;

        ImageView imageView;

        public ProductHolder(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.txtMyProductName);
            text1 = itemView.findViewById(R.id.txtMyProductQuantity);
            text2 = itemView.findViewById(R.id.txtMyProductPrice);

            imageView = itemView.findViewById(R.id.imgMyproduct);
        }
    }
}
