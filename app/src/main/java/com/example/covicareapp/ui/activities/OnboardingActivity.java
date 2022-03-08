package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.example.covicareapp.ui.activities.auth.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OnboardingActivity extends AppCompatActivity {

    //    Firebase Variable
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        //        Firebase Auth Instance
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                verifyUser(firebaseUser);
            }
        };

        mAuthListener.onAuthStateChanged(firebaseAuth);
        Log.i("Here", "Before Check");
        verifyUser(firebaseUser);
    }

    private void verifyUser(FirebaseUser firebaseUser) {
        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("users").document(firebaseUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }
        Log.i("Here", "Not exists");
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

}