package com.example.covicareapp.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.example.covicareapp.models.OnlineUserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerifyEmailActivity extends AppCompatActivity {
    // Variables
    String raspiUid, userId, fullName, email, password, gender, dateOfBirth, phoneNumber, countryCode, countryName;

    // Firebase Variables
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    CollectionReference userCollectionReference, allGroupsCollectionReference;

    //    UI Variables
    ProgressBar progressBar;
    MaterialTextView sentEmailTextView;
    MaterialButton loginButton;

    // function to generate a random string of length n
    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        Intent intent = getIntent();
        fullName = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        gender = intent.getStringExtra("gender");
        dateOfBirth = intent.getStringExtra("dateOfBirth") + " 00:00:00";
        phoneNumber = intent.getStringExtra("phoneNumber");
        countryName = intent.getStringExtra("countryName");
        countryCode = intent.getStringExtra("countryCode");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        sentEmailTextView = findViewById(R.id.sent_email_tv);
        loginButton = findViewById(R.id.go_to_login_btn);
        progressBar = findViewById(R.id.progress_indicator);

        progressBar.setVisibility(View.GONE);

        sentEmailTextView.setText("Verification e-mail sent to\n" + email);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseUser.isEmailVerified()) {
                                userId = firebaseUser.getUid();
                                raspiUid = "O" + getAlphaNumericString(5);

                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                userCollectionReference = firebaseFirestore.collection("users");
                                allGroupsCollectionReference = firebaseFirestore.collection("allGroups");

                                Log.i("User DOB Format at signup :", dateOfBirth);

                                OnlineUserModel userHelperClass = new OnlineUserModel(raspiUid, fullName, email, userId, gender, new Timestamp(java.sql.Timestamp.valueOf(dateOfBirth)), phoneNumber, countryCode, countryName, new ArrayList<String>(), new ArrayList<String>());

                                userCollectionReference.document(userHelperClass.getEmail()).set(userHelperClass.getUserData()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> groupData = new HashMap<String, Object>();
                                            groupData.put("createdBy", email);
                                            groupData.put("groupName", email + "'s Group");
                                            groupData.put("groupInfo", "This is the group for " + email + "'s data");
                                            groupData.put("dateCreated", Timestamp.now());

                                            ArrayList<String> usersList = new ArrayList<String>();
                                            usersList.add(email);

                                            groupData.put("groupUsers", usersList);
                                            DocumentReference newDocRef = allGroupsCollectionReference.document(email);
                                            groupData.put("groupId", newDocRef.getId());
                                            newDocRef.set(groupData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Map<String, Object> userGroupsListData = new HashMap<String, Object>();
                                                        ArrayList<String> userGroupsList = new ArrayList<String>();
                                                        userGroupsList.add(newDocRef.getId());
                                                        userGroupsListData.put("groupsCreated", userGroupsList);

                                                        userCollectionReference.document(email).update(userGroupsListData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                loginButton.setVisibility(View.VISIBLE);
                                                                progressBar.setVisibility(View.GONE);
                                                                if (task.isSuccessful()) {
                                                                    Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
                                                                    intent.putExtra("email", email);
                                                                    intent.putExtra("password", password);
                                                                    startActivity(intent);
                                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                } else {
                                                                    showSnackbar("Error " + task.getException().getMessage(), "", "Error");
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        loginButton.setVisibility(View.VISIBLE);
                                                        progressBar.setVisibility(View.GONE);

                                                        showSnackbar("Error " + task.getException().getMessage(), "", "Error");
                                                    }
                                                }
                                            });

                                        } else {
                                            loginButton.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);

                                            showSnackbar("Error " + task.getException().getMessage(), "", "Error");
                                        }
                                    }
                                });
                            } else {
                                loginButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                                showSnackbar("Please click link sent to " + email, "", "Warning");
                            }
                        } else {
                            loginButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            showSnackbar("Error! " + task.getException().getMessage().trim(), "", "Error");
                        }
                    }
                });
            }
        });

        sendVerificationEmail(firebaseUser);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Verify Action First
        Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void sendVerificationEmail(FirebaseUser firebaseUser) {
        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                showSnackbar("Verification Email Sent Successfully", "", "Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackbar("Error! " + e.getMessage(), "", "Success");
            }
        });
    }

    protected void showSnackbar(String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(findViewById(R.id.verify_email_activity).getRootView(), messageStr, Snackbar.LENGTH_LONG);

        // the duration is in terms of milliseconds
        snackbar.setDuration(3600);
        // set the background tint color for the snackbar
        switch (resultState) {
            case "Success":
                snackbar.setBackgroundTint(getColor(R.color.success_800));
                break;
            case "Warning":
                snackbar.setBackgroundTint(getColor(R.color.warning_900));
                break;
            case "Error":
                snackbar.setBackgroundTint(getColor(R.color.error_400));
                break;
            default:
                snackbar.setBackgroundTint(getColor(R.color.information_800));
        }
        // set the action button text color of the snackbar however this is optional
        snackbar.setTextColor(getColor(R.color.white_50));
        snackbar.setActionTextColor(getColor(R.color.white_50));

        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // as all the snackbar wont have the action button
        snackbar.show();

    }
}