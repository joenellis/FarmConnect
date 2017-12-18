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
//import com.squareup.picasso.Picasso;

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


    public List<ObjectProduct> filter(List<ObjectProduct> products, String query) {
        query = query.toLowerCase();

        ArrayList<ObjectProduct> filteredCompanyList = new ArrayList<>();

        for (ObjectProduct item : products) {
            final String productName = item.getProductname().toLowerCase();
            final String productPrice = item.getPrice().toLowerCase();
            if (productName.contains(query) || productPrice.contains(query)) {
                filteredCompanyList.add(item);
            }
        }
        return filteredCompanyList;
    }

    public void animateTo(List<ObjectProduct> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<ObjectProduct> newModels) {
        for (int i = products.size() - 1; i >= 0; i--) {
            final ObjectProduct model = products.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ObjectProduct> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ObjectProduct model = newModels.get(i);
            if (!products.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ObjectProduct> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ObjectProduct model = newModels.get(toPosition);
            final int fromPosition = products.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public ObjectProduct removeItem(int position) {
        final ObjectProduct model = products.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ObjectProduct model) {
        products.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ObjectProduct model = products.remove(fromPosition);
        products.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
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
