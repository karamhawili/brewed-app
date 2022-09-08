package com.example.brewedapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    BottomNavigationView navigation_menu_adp;

    Button logout_btn_acc;

    //shared preferences for username and logout
    SharedPreferences sharedPref;

    //username textview
    TextView username_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        logout_btn_acc = findViewById(R.id.logout_btn_acc);
        navigation_menu_adp = findViewById(R.id.navigation_menu_adp);
        username_title = findViewById(R.id.username_title);

        sharedPref = AccountActivity.this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE); //access the SharedPreferences file UserInfo

        //set text to username
        username_title.setText(sharedPref.getString("userName", "User"));

        navigation_menu_adp.setSelectedItemId(R.id.accountActivity);
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

        //logout
        logout_btn_acc.setOnClickListener(view -> {

            //get current user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                //get user full name
                String name = user.getDisplayName();
                //logout
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AccountActivity.this, name + " successfully logged out", Toast.LENGTH_LONG).show();

                //clear SharedPreferences
                SharedPreferences sharedPref = AccountActivity.this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE); //access the SharedPreferences file UserInfo
                sharedPref.edit().clear().apply();

                //go to main activity
                Intent i = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(i);
                // user cannot go back to this screen until he is logged in
                finishAffinity();
            }
        });
    }
}