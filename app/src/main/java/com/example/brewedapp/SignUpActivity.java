package com.example.brewedapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    Button back_btn_signup, signup_btn;

    //firebase sign up
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    //database ref
    private DatabaseReference mDatabase;


    EditText etFullName, etEmail, etPassword, etConfirmPassword, etAddress;
    TextInputLayout litFullName, litEmail, litPassword, litConfirmPassword, litAddress;

    //for the custom toast
    RelativeLayout custom_toast_layout_signup;
    TextView signup_toast_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //firebase realtime database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        back_btn_signup = findViewById(R.id.back_btn_signup);
        signup_btn = findViewById(R.id.signup_btn);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etAddress = findViewById(R.id.etAddress);
        litFullName = findViewById(R.id.litFullName);
        litEmail = findViewById(R.id.litEmail);
        litPassword = findViewById(R.id.litPassword);
        litConfirmPassword =findViewById(R.id.litConfirmPassword);
        litAddress = findViewById(R.id.litAddress);

        //custom toast binding and visibility
        custom_toast_layout_signup = findViewById(R.id.custom_toast_layout_signup);
        signup_toast_message = findViewById(R.id.signup_toast_message);
        custom_toast_layout_signup.setVisibility(View.INVISIBLE);


        back_btn_signup.setOnClickListener(view -> finish());

        signup_btn.setOnClickListener(view -> signUp());


        // user full name required
        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etFullName.getText().toString().isEmpty()) {
                    litFullName.setError("required*");
                }
                else {
                    litFullName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // user email required
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etEmail.getText().toString().isEmpty()) {
                    litEmail.setError("required*");
                }
                else {
                    litEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // user password required
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etPassword.getText().toString().isEmpty()) {
                    litPassword.setError("required*");
                }
                else {
                    litPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // confirm password required and = password
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etConfirmPassword.getText().toString().isEmpty()) {
                    litConfirmPassword.setError("required*");
                }
                else {
                    litConfirmPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!(etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))) {
                    litConfirmPassword.setError("passwords don't match");
                }
                else {
                    litConfirmPassword.setError(null);
                }
            }
        });

        // address required
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etAddress.getText().toString().isEmpty()) {
                    litAddress.setError("required*");
                }
                else {
                    litAddress.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    public void signUp() {

        //get input
        String email = etEmail.getText().toString();
        String fullName = etFullName.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String address = etAddress.getText().toString();

        //continue if no field is empty
        if(!email.equals("") && !fullName.equals("") && !password.equals("")
                && !confirmPassword.equals("") && !address.equals("")){

            //if etPasswords don't match, reject sign up request and return focus to etConfirmPassword EditText
            if(!(etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))) {
                etConfirmPassword.requestFocus();
                return;
            }

            //create a new account with Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, go to DisplayProductsActivity
                            Log.d(TAG, "createUserWithEmail:success");

                            //add the full name to the user's account
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d(TAG, "User profile updated. Full Name Added.");
                                        } else {
                                            //failed to update user's name
                                            Log.w(TAG, "Failed to Update User profile.");
                                        }
                                    });

                            //store user info in SharedPreferences
                            String name = user.getDisplayName() != null ? user.getDisplayName() : "No Name";
                            String thisEmail = user.getEmail();
                            String uid = user.getUid();

                            SharedPreferences sharedPref = SignUpActivity.this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE); //access the SharedPreferences file UserInfo
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userName", name);
                            editor.putString("userEmail", thisEmail);
                            editor.putString("userID", uid);
                            editor.apply();


                            //add the etAddress to the real time database under users/{uid}/address:userAddress
                            mDatabase.child("users").child(uid).child("address").setValue(address)
                                    .addOnSuccessListener(aVoid -> {
                                        // Write was successful!
                                        Log.d(TAG, "Address write success.");
                                    })
                                    .addOnFailureListener(e -> {
                                        // Write failed
                                        Log.w(TAG, "Address write failed.");
                                    });

                            //Go to DisplayProductsActivity
                            Intent i = new Intent(SignUpActivity.this, DisplayProductsActivity.class);
                            startActivity(i);
                            //clear back button stack (user cannot go back to this screen)
                            finishAffinity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(SignUpActivity.this, "Authentication failed. Please try again in a few minutes.",
                            //Toast.LENGTH_SHORT).show();
                            showCustomToastError();
                            //updateUI(null);
                        }
                    });
        }
        else{
            //Toast.makeText(this, "Please fill out the fields", Toast.LENGTH_SHORT).show();
            showCustomToastWarning();
        }

    }



    //custom toast - warning
    public void showCustomToastWarning () {
        //show toast
        custom_toast_layout_signup.setVisibility(View.VISIBLE);
        //set text
        signup_toast_message.setText("Please fill out the fields");
    }

    //custom toast error
    public void showCustomToastError () {
        //show toast
        custom_toast_layout_signup.setVisibility(View.VISIBLE);
        //set text
        signup_toast_message.setText("Sign Up Failed");
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