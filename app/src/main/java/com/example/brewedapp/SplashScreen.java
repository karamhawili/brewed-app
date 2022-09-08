package com.example.brewedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.FutureTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        new Handler().postDelayed(() -> {

            //if user is signed in, navigate to DisplayProductsActivity
            Intent i;
            if (user != null) {
                i = new Intent(SplashScreen.this, DisplayProductsActivity.class);
            } else {
                //else navigate to MainActivity
                i = new Intent(SplashScreen.this, MainActivity.class);
            }
            startActivity(i);
            //clear back button stack (user cannot go back to this screen if he is logged in)
            finish();
        }, 3000); //time in milliseconds

    }
}
