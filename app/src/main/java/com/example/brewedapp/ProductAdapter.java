package com.example.brewedapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    //save context
    private Context mContext;

    //contain the list of products
    private List<Product> mProducts;

    //constructor
    public ProductAdapter(Context mContext, List<Product> mProducts) {
        this.mContext = mContext;
        this.mProducts = mProducts;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_layout, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        //reference to current item
        Product productCurrent = mProducts.get(position);

        Glide.with(mContext)
                .load(productCurrent.getP_imageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.product_image);

        String price = '$'+ String.valueOf(productCurrent.getP_price());
        holder.product_price.setText(price);

        holder.product_title.setText(productCurrent.getP_title());

        holder.product_image.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ProductInfoActivity.class);
            intent.putExtra("product", productCurrent);
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView product_image;
        public TextView  product_price;
        public TextView product_title;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            product_price = itemView.findViewById(R.id.product_price);
            product_title = itemView.findViewById(R.id.product_title);
        }
    }
}
