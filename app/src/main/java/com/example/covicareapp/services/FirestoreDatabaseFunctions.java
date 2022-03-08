package com.example.covicareapp.services;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.covicareapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreDatabaseFunctions {

    public Map<String, Object> addNewUser(UserModel userModel, FirebaseFirestore firebaseFirestore, MaterialButton button, ProgressBar progressBar) {
        Map<String, Object> resultMap = new HashMap<>();
        CollectionReference userCollectionReference = firebaseFirestore.collection("users");

        userCollectionReference.document(userModel.getEmail()).set(userModel.getUserData()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    resultMap.put("Result", true);
                    resultMap.put("Message", "The user is successfully added to CoviCare");
                } else {
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    resultMap.put("Result", false);
                    resultMap.put("Message", "Error! " + task.getException().getMessage());
                }
            }
        });

        return resultMap;
    }

}
