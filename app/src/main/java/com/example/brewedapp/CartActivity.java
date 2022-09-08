package com.example.brewedapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    //database
    DatabaseHandler dbh;

    RecyclerView recycler_view_ca;
    CartAdapter mAdapter;

    //sharedPreferences - uid
    SharedPreferences sharedPref;

    //database - address + place order
    private DatabaseReference mDatabase;

    //address EditText
    TextInputLayout layout_address_ca;
    EditText etAddress_ca;

    TextView subtotal_tv, delivery_charge_tv, total_tv;

    BottomNavigationView navigation_menu_adp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recycler_view_ca = findViewById(R.id.recycler_view_ca);
        layout_address_ca = findViewById(R.id.layout_address_ca);
        etAddress_ca = findViewById(R.id.etAddress_ca);
        subtotal_tv = findViewById(R.id.subtotal_tv);
        delivery_charge_tv = findViewById(R.id.delivery_charge_tv);
        total_tv = findViewById(R.id.total_tv);

        navigation_menu_adp = findViewById(R.id.navigation_menu_adp);
        navigation_menu_adp.setSelectedItemId(R.id.cartActivity);
        navigation_menu_adp.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.displayProductsActivity:
                    startActivity(new Intent(getApplicationContext(), DisplayProductsActivity.class));
                    return true;
                case R.id.cartActivity:
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    return true;
                case R.id.accountActivity:
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                    return true;
            }
            return true;
        });

        //set enabled false in case we already have the user's address (email-pass authentication)
        etAddress_ca.setEnabled(false);

        //database
        dbh = new DatabaseHandler(CartActivity.this);

        //populate recyclerview
        displayCartItems();

        sharedPref = CartActivity.this.getSharedPreferences("UserInfo",Context.MODE_PRIVATE); //access the SharedPreferences file UserInfo
        //get user ID from sharedPreferences file
        String UID = sharedPref.getString("userID", "No ID");

        //get address from realtime database
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(UID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address = snapshot.child("address").getValue().toString();
                if (address.equals("unknown")) {
                    etAddress_ca.setEnabled(true);
                }
                else {
                    etAddress_ca.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Failed to retrieve address", Toast.LENGTH_SHORT).show();
            }
        });

        if(etAddress_ca.getText().toString().equals("")) {
            //prompt the user to enter his own address (case - google account authentication)
            etAddress_ca.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    layout_address_ca.setError("address required*");
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(etAddress_ca.getText().toString().isEmpty()) {
                        layout_address_ca.setError("required*");
                    }
                    else {
                        layout_address_ca.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }


        //display order fees
        Float subtotal = dbh.getSubTotal();
        subtotal_tv.setText("$" + String.valueOf(subtotal));
        delivery_charge_tv.setText("$" + "10.0");
        Float total = subtotal + 10;
        total_tv.setText("$" + String.valueOf(total));

    }


    public void displayCartItems() {
        // get all items from database
        List<ProductSqlite> mItems = dbh.getAllItemsFromCart();

        recycler_view_ca.setHasFixedSize(true);
        //layout manager for the recyclerView
        recycler_view_ca.setLayoutManager(new LinearLayoutManager(this));

        // create adapter
        mAdapter = new CartAdapter(CartActivity.this, mItems);

        recycler_view_ca.setAdapter(mAdapter);

    }
}