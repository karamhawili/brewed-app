package com.example.brewedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.number.Precision;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;

public class ProductInfoActivity extends AppCompatActivity {

    TextView title_tv, weight_tv, origin_tv, roast_level_tv, notes_tv, acidity_level_tv, grind_tv, price_tv;
    Button add_to_cart_btn, back_btn_pia;
    ImageView image_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        title_tv = findViewById(R.id.title_tv);
        weight_tv = findViewById(R.id.weight_tv);
        origin_tv = findViewById(R.id.origin_tv);
        roast_level_tv = findViewById(R.id.roast_level_tv);
        notes_tv = findViewById(R.id.notes_tv);
        acidity_level_tv = findViewById(R.id.acidity_level_tv);
        grind_tv = findViewById(R.id.grind_tv);
        price_tv = findViewById(R.id.price_tv);
        add_to_cart_btn = findViewById(R.id.add_to_cart_btn);
        image_iv = findViewById(R.id.image_iv);
        back_btn_pia = findViewById(R.id.back_btn_pia);


        //get product object
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");

        add_to_cart_btn.setOnClickListener(view -> {
            DatabaseHandler dbh = new DatabaseHandler(ProductInfoActivity.this);
            if (dbh.hasRow(product)){
                dbh.updateItemQuantity(product.getP_title(), "increase");
                Toast.makeText(ProductInfoActivity.this, "Item added, value incremented", Toast.LENGTH_SHORT).show();
            } else {
                dbh.addItemToCart(product);
                Toast.makeText(ProductInfoActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show(); //will be changed later on
            }
        });

        back_btn_pia.setOnClickListener(view -> finish());

        //set product info and load image
        setProductInfo(product);

    }


    public void setProductInfo(Product p) {

        title_tv.setText(p.getP_title());
        weight_tv.setText(p.getP_weight());
        origin_tv.setText(p.getP_origin());
        roast_level_tv.setText(p.getP_roastLevel());
        notes_tv.setText(p.getP_notes());
        acidity_level_tv.setText(p.getP_acidityLevel());
        grind_tv.setText(p.getP_grind());
        String price = '$'+ String.valueOf(p.getP_price());
        price_tv.setText(price);

        //load image
        Glide.with(this)
                .load(p.getP_imageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image_iv);
    }
}