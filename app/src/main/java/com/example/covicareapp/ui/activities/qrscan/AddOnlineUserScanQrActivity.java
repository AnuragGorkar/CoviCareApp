package com.example.covicareapp.ui.activities.qrscan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.covicareapp.R;
import com.example.covicareapp.logic.EncryptDecryptData;
import com.example.covicareapp.ui.activities.addedGroups.AddNewUserActivity;
import com.example.covicareapp.ui.activities.addedGroups.GroupAddedInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Map;

public class AddOnlineUserScanQrActivity extends AppCompatActivity {

    // Variables
    String backActivity;
    String groupId, groupName, groupDateCreated, groupDescription, groupOfflineUsers, groupOnlineUsers, email, password;
    ArrayList<String> groupOnlineUsersList = new ArrayList<String>();
    ArrayList<String> groupOfflineUsersList = new ArrayList<String>();

    //    UI Variables
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    TextView scannedCode;
    ProgressBar progressBar;

    //    Firebase Variable
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    CollectionReference userCollectionReference, allGroupsCollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_online_user_scan_qr);

        //        UI Hooks
        codeScannerView = findViewById(R.id.scanner_view);
        scannedCode = findViewById(R.id.scanned_value);
        progressBar = findViewById(R.id.progress_indicator);

        progressBar.setVisibility(View.GONE);
        scannedCode.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        backActivity = intent.getStringExtra("backTo");
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        groupDateCreated = intent.getStringExtra("groupDateCreated");
        groupDescription = intent.getStringExtra("groupDescription");
        groupOnlineUsers = String.valueOf(intent.getStringExtra("groupOnlineUsers"));
        groupOfflineUsers = intent.getStringExtra("groupOfflineUsers");
        groupOnlineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOnlineUsersList");

        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        scannedCode.setText("Adding User");
                        FirebaseAuth currentFirebaseAuth = FirebaseAuth.getInstance();
                        String currentEmail = currentFirebaseAuth.getCurrentUser().getEmail();

                        EncryptDecryptData encryptDecryptData = new EncryptDecryptData();

                        Log.i("Result   ", result.getText());

                        email = encryptDecryptData.decryptEmail(result.getText());

                        if (!result.getText().contains("Data=") || !result.getText().contains("Salt=") || !result.getText().contains("Password=")) {
                            progressBar.setVisibility(View.GONE);
                            scannedCode.setTextColor(getColor(R.color.error_900));
                            scannedCode.setText("Scan valid QR code please");

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(AddOnlineUserScanQrActivity.this, AddNewUserActivity.class);
                                    intent.putExtra("groupId", groupId);
                                    intent.putExtra("groupName", groupName);
                                    intent.putExtra("groupDateCreated", groupDateCreated);
                                    intent.putExtra("groupDescription", groupDescription);
                                    intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
                                    intent.putExtra("groupOnlineUsers", groupOnlineUsers);
                                    intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                                    intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                }
                            }, 1000);

                        } else {
                            email = email.substring(7, email.length() - 1);

                            if (currentEmail.equals(email)) {
                                progressBar.setVisibility(View.GONE);
                                scannedCode.setTextColor(getColor(R.color.error_900));
                                scannedCode.setText("You cannot add yourself to any group");

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(AddOnlineUserScanQrActivity.this, AddNewUserActivity.class);
                                        intent.putExtra("groupId", groupId);
                                        intent.putExtra("groupName", groupName);
                                        intent.putExtra("groupDateCreated", groupDateCreated);
                                        intent.putExtra("groupDescription", groupDescription);
                                        intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
                                        intent.putExtra("groupOnlineUsers", groupOnlineUsers);
                                        intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                                        intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                    }
                                }, 1000);
                            } else {
                                if (groupOnlineUsersList.contains(email)) {
                                    progressBar.setVisibility(View.GONE);
                                    scannedCode.setTextColor(getColor(R.color.warning_900));
                                    scannedCode.setText("User already added in group. Add new user.");

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(AddOnlineUserScanQrActivity.this, AddNewUserActivity.class);
                                            intent.putExtra("groupId", groupId);
                                            intent.putExtra("groupName", groupName);
                                            intent.putExtra("groupDateCreated", groupDateCreated);
                                            intent.putExtra("groupDescription", groupDescription);
                                            intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
                                            intent.putExtra("groupOnlineUsers", groupOnlineUsers);
                                            intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                                            intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        }
                                    }, 1000);
                                } else {
                                    firebaseFirestore = FirebaseFirestore.getInstance();
                                    userCollectionReference = firebaseFirestore.collection("users");
                                    allGroupsCollectionReference = firebaseFirestore.collection("allGroups");

                                    userCollectionReference.document(email).update("groupsAddedTo", FieldValue.arrayUnion(groupId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                allGroupsCollectionReference.document(groupId).update("groupUsers", FieldValue.arrayUnion(email)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            allGroupsCollectionReference.document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        scannedCode.setTextColor(getColor(R.color.success_800));
                                                                        scannedCode.setText("New User Added");

                                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                                        Map<String, Object> data = documentSnapshot.getData();

                                                                        final Handler handler = new Handler();
                                                                        handler.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Intent intent = new Intent(AddOnlineUserScanQrActivity.this, GroupAddedInfoActivity.class);
                                                                                intent.putExtra("groupId", groupId);
                                                                                intent.putExtra("groupName", groupName);
                                                                                intent.putExtra("groupDateCreated", groupDateCreated);
                                                                                intent.putExtra("groupDescription", groupDescription);
                                                                                intent.putExtra("groupOnlineUsersList", (ArrayList<String>) data.get("groupUsers"));
                                                                                intent.putExtra("groupOnlineUsers", String.valueOf(((ArrayList<String>) data.get("groupUsers")).size()));
                                                                                intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                                                                                intent.putExtra("groupOfflineUsersList", groupOnlineUsersList);
                                                                                startActivity(intent);
                                                                                finish();
                                                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                            }
                                                                        }, 200);

                                                                    } else {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        scannedCode.setTextColor(getColor(R.color.error_400));
                                                                        scannedCode.setText("Error! " + task.getException().getMessage());
                                                                        Log.e("Error", task.getException().getMessage());
                                                                    }

                                                                }
                                                            });
                                                        } else {
                                                            progressBar.setVisibility(View.GONE);
                                                            scannedCode.setTextColor(getColor(R.color.error_400));
                                                            scannedCode.setText("Error! " + task.getException().getMessage());
                                                            Log.e("Error", task.getException().getMessage());
                                                            // Handle Error
                                                        }

                                                    }
                                                });
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                scannedCode.setTextColor(getColor(R.color.error_400));
                                                scannedCode.setText("Error! " + task.getException().getMessage());
                                                Log.e("Error", task.getException().getMessage());
                                                // Handle Error
                                            }
                                        }
                                    });
                                }
                            }
                        }


                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent;
        if (backActivity != null && backActivity.equals("GroupAddedInfoActivity")) {
            intent = new Intent(AddOnlineUserScanQrActivity.this, GroupAddedInfoActivity.class);
        } else {
            intent = new Intent(AddOnlineUserScanQrActivity.this, AddNewUserActivity.class);
        }
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupDateCreated", groupDateCreated);
        intent.putExtra("groupDescription", groupDescription);
        intent.putExtra("groupOnlineUsersList", groupOfflineUsersList);
        intent.putExtra("groupOnlineUsers", groupOfflineUsers);
        intent.putExtra("groupOfflineUsersList", new ArrayList<String>());
        intent.putExtra("groupOfflineUsers", "0");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}