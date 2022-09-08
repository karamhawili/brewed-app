package com.example.brewedapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DisplayProductsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("products");
    private List<Product> mProducts;

    BottomNavigationView navigation_menu_adp;

    //progress bar
    LinearProgressIndicator linear_progress_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);

        mRecyclerView = findViewById(R.id.recyclerView_adp);
        navigation_menu_adp = findViewById(R.id.navigation_menu_adp);
        navigation_menu_adp.setSelectedItemId(R.id.displayProductsActivity);
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

        linear_progress_indicator = findViewById(R.id.linear_progress_indicator);
        linear_progress_indicator.setVisibility(View.INVISIBLE);

        mRecyclerView.setHasFixedSize(true);
        //layout manager for the recyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mProducts = new ArrayList<>();

        linear_progress_indicator.setVisibility(View.VISIBLE);
        storageReference.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        // All the items under listRef.
                        Product product = new Product();
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            product.setP_imageURL(uri.toString());
                            //get metadata
                            item.getMetadata().addOnSuccessListener(storageMetadata -> {
                                product.setP_title(storageMetadata.getCustomMetadata("p_title"));
                                product.setP_origin(storageMetadata.getCustomMetadata("p_origin"));
                                product.setP_roastLevel(storageMetadata.getCustomMetadata("p_roastLevel"));
                                product.setP_notes(storageMetadata.getCustomMetadata("p_notes"));
                                product.setP_acidityLevel(storageMetadata.getCustomMetadata("p_acidityLevel"));
                                product.setP_grind(storageMetadata.getCustomMetadata("p_grind"));
                                product.setP_weight(storageMetadata.getCustomMetadata("p_weight"));
                                product.setP_price(Float.parseFloat(storageMetadata.getCustomMetadata("p_price")));
                            });
                        });
                        mProducts.add(product);
                    }

                    // create adapter
                    mAdapter = new ProductAdapter(DisplayProductsActivity.this, mProducts);

                    new Handler().postDelayed(() -> {
                        mRecyclerView.setAdapter(mAdapter);
                        linear_progress_indicator.setVisibility(View.INVISIBLE);
                    }, 4000); //time in milliseconds
                })

                .addOnFailureListener(e -> {
                    Toast.makeText(DisplayProductsActivity.this, "Make sure you have an Internet connection and try again later.", Toast.LENGTH_SHORT).show();
                    linear_progress_indicator.setVisibility(View.INVISIBLE);
                });

    }

    /**
     * Clear focus on touch outside for all EditText inputs.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}