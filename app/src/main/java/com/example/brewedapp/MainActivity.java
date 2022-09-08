package com.example.brewedapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GoogleSignIn";
    Button login, signUp;
    TextView continue_with_google;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 007;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //firebase realtime database
        mDatabase = FirebaseDatabase.getInstance().getReference();


        login = findViewById(R.id.login_button_main);
        signUp = findViewById(R.id.signup_button_main);
        continue_with_google = findViewById(R.id.continue_with_google);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        continue_with_google.setOnClickListener(view -> signInWithGoogle());

        login.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        });

        signUp.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
        });
    }

    //sign in with google account
    public void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        //store user info in SharedPreferences
                        FirebaseUser user = mAuth.getCurrentUser();
                        String name = user.getDisplayName() != null ? user.getDisplayName() : "No Name";
                        String email = user.getEmail();
                        String uid = user.getUid();

                        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE); //access the SharedPreferences file UserInfo
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("userName", name);
                        editor.putString("userEmail", email);
                        editor.putString("userID", uid);
                        editor.apply();

                        //add an unknown address
                        //add the etAddress to the real time database under users/{uid}/address:userAddress
                        mDatabase.child("users").child(uid).child("address").setValue("unknown")
                                .addOnSuccessListener(aVoid -> {
                                    // Write was successful!
                                    Log.d(TAG, "Address write success.");
                                })
                                .addOnFailureListener(e -> {
                                    // Write failed
                                    Log.w(TAG, "Address write failed.");
                                });

                        //navigate to DisplayProductsActivity and prevent user from going back
                        Intent i = new Intent(MainActivity.this, DisplayProductsActivity.class);
                        startActivity(i);
                        // user cannot go back to this screen until he is logged in
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        //notify user that the process failed
                        Toast.makeText(MainActivity.this, "Process failed, try again later.", Toast.LENGTH_LONG).show();

                    }
                });
    }
}