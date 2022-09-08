package com.example.brewedapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button back_btn_login, login_btn;
    TextView signup_btn_login;

    TextInputLayout layoutEmail, layoutPassword;
    EditText etEmail, etPassword;

    //for the custom toast
    RelativeLayout custom_toast_layout_login;
    TextView login_toast_message;

    //login with firebase
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        back_btn_login = findViewById(R.id.back_btn_login);
        signup_btn_login = findViewById(R.id.signup_btn_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        login_btn = findViewById(R.id.login_btn);


        //custom toast binding and visibility
        custom_toast_layout_login = findViewById(R.id.custom_toast_layout_login);
        login_toast_message = findViewById(R.id.login_toast_message);
        custom_toast_layout_login.setVisibility(View.INVISIBLE);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //check etEmail condition
                if(etEmail.getText().toString().isEmpty()) {
                    layoutEmail.setError("required*");
                }
                else {
                    layoutEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //check etPassword condition
                if(etPassword.getText().toString().isEmpty()) {
                    layoutPassword.setError("required*");
                }
                else {
                    layoutPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        back_btn_login.setOnClickListener(view -> finish());

        signup_btn_login.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
        });

        login_btn.setOnClickListener(view -> logIn());

    }



    //login using email and password
    public void logIn() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(!email.equals("") && !password.equals("") ) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, go to DisplayProductsActivity
                            Log.d(TAG, "signInWithEmail:success");

                            //store user info in SharedPreferences
                            FirebaseUser user = mAuth.getCurrentUser();
                            String name = user.getDisplayName() != null ? user.getDisplayName() : "No Name";
                            String thisEmail = user.getEmail();
                            String uid = user.getUid();

                            SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE); //access the SharedPreferences file UserInfo
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userName", name);
                            editor.putString("userEmail", thisEmail);
                            editor.putString("userID", uid);
                            editor.apply();

                            Intent i = new Intent(LoginActivity.this, DisplayProductsActivity.class);
                            startActivity(i);
                            //prevent user from going back
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            //warning: password and email are incorrect
                            //Toast.makeText(LoginActivity.this, "Authentication failed. Invalid Username or Password.",
                            //Toast.LENGTH_SHORT).show();
                            showCustomToastError();
                        }
                    });
        }
        else {
            //Toast.makeText(this, "Please fill out the fields", Toast.LENGTH_LONG).show();
            showCustomToastWarning();
        }
    }




    //custom toast - warning
    public void showCustomToastWarning () {
        //show toast
        custom_toast_layout_login.setVisibility(View.VISIBLE);
        //set text
        login_toast_message.setText("Please fill out the fields");
    }

    //custom toast error
    public void showCustomToastError () {
        //show toast
        custom_toast_layout_login.setVisibility(View.VISIBLE);
        //set text
        login_toast_message.setText("Invalid Username or Password");
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