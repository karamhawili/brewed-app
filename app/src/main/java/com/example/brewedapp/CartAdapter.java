package com.example.brewedapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemViewHolder> {

    //save context
    private Context mContext;

    //contain the list of products
    private List<ProductSqlite> mItems;

    //constructor
    public CartAdapter(Context mContext, List<ProductSqlite> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }


    @NonNull
    @Override
    public CartAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_item_layout, parent, false);
        return new CartAdapter.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ItemViewHolder holder, int position) {
        //reference to current item
        ProductSqlite productCurrent = mItems.get(position);

        //load image
        Glide.with(mContext)
                .load(productCurrent.getP_imageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ca_image_iv);

        //TextViews
        holder.ca_title.setText(productCurrent.getP_title());
        holder.ca_origin.setText(productCurrent.getP_origin());
        holder.ca_quantity.setText("x" + String.valueOf(productCurrent.getP_quantity()));

        String price = '$'+ String.valueOf(productCurrent.getP_price());
        holder.ca_price.setText(price);


        //listeners
        holder.ca_decrease_btn.setOnClickListener(view -> {
            //database
            DatabaseHandler dbh = new DatabaseHandler(mContext);
            dbh.updateItemQuantity(productCurrent.getP_title(), "decrease");
        });

        holder.ca_increase_btn.setOnClickListener(view -> {
            DatabaseHandler dbh = new DatabaseHandler(mContext);
            dbh.updateItemQuantity(productCurrent.getP_title(), "increase");
        });

        holder.ca_delete_btn.setOnClickListener(view -> {
            DatabaseHandler dbh = new DatabaseHandler(mContext);
            dbh.removeItemFromCart(productCurrent);
        });
        //end of listeners

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView ca_image_iv;
        public TextView ca_title;
        public TextView ca_origin;
        public TextView ca_price;
        public TextView ca_quantity;

        //buttons
        public ImageView ca_decrease_btn;
        public ImageView ca_increase_btn;
        public ImageView ca_delete_btn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ca_image_iv = itemView.findViewById(R.id.ca_image_iv);
            ca_title = itemView.findViewById(R.id.ca_title);
            ca_origin = itemView.findViewById(R.id.ca_origin);
            ca_price = itemView.findViewById(R.id.ca_price);
            ca_quantity = itemView.findViewById(R.id.ca_quantity);
            ca_decrease_btn = itemView.findViewById(R.id.ca_decrease_btn);
            ca_increase_btn = itemView.findViewById(R.id.ca_increase_btn);
            ca_delete_btn = itemView.findViewById(R.id.ca_delete_btn);

            ca_decrease_btn.setVisibility(View.INVISIBLE);
            ca_increase_btn.setVisibility(View.INVISIBLE);

        }
    }
}
