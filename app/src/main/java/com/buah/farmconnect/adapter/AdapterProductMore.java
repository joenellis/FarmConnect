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

import java.util.ArrayList;
import java.util.List;

public class AdapterProductMore extends RecyclerView.Adapter<AdapterProductMore.ProductHolder> {

    private Context mContext;
    private List<ObjectProduct> products;
    private ArrayList mImage;

    public AdapterProductMore(Context mContext, List<ObjectProduct> products, ArrayList image) {
        this.mContext = mContext;
        this.products = products;
        this.mImage = image;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.holder_product_item_more, null);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {

        ObjectProduct product = products.get(position);

        holder.productName.setText(product.getProductname());
        holder.productImage.setImageResource(Integer.parseInt(mImage.get(position).toString()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ActivityViewProduct.class);
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
        ImageView productImage;

        ProductHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.moreTxt);
            productImage = itemView.findViewById(R.id.moreImg);
        }
    }
}
