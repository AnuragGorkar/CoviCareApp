package com.example.covicareapp.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public class AddNewUserActivity extends AppCompatActivity {

    // Variables
    String groupId, groupName, groupDateCreated, groupDescription, groupOfflineUsers, groupOnlineUsers, email, password;
    ArrayList<String> groupOnlineUsersList = new ArrayList<String>();
    ArrayList<String> groupOfflineUsersList = new ArrayList<String>();

    // UI Variables
    Dialog dialog;
    MaterialToolbar materialToolbar;
    MaterialButton add_online_users, add_local_users;
    ProgressBar progressBar;
    ImageButton closeDialogueButton;
    TextInputLayout userNameTextInput, userPasswordTextInput;
    MaterialButton addNewUserButton, scanQRToAddNewUserButton;
    TextView groupNameTextView;

    //    Firebase Variable
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    CollectionReference userCollectionReference, allGroupsCollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        // UI Hooks
        materialToolbar = findViewById(R.id.toolbar);
        add_online_users = findViewById(R.id.add_online_users);
        add_local_users = findViewById(R.id.add_local_users);
        groupNameTextView = findViewById(R.id.group_name);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        groupDateCreated = intent.getStringExtra("groupDateCreated");
        groupDescription = intent.getStringExtra("groupDescription");
        groupOnlineUsers = String.valueOf(intent.getStringExtra("groupOnlineUsers"));
        groupOfflineUsers = intent.getStringExtra("groupOfflineUsers");
        groupOnlineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOnlineUsersList");

        groupNameTextView.setText(groupName);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewUserActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        add_online_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddOnlineUserDialogue();
            }
        });

        add_local_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewUserActivity.this, AddLocalUsersInfoActivity.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupName);
                intent.putExtra("groupDateCreated", groupDateCreated);
                intent.putExtra("groupDescription", groupDescription);
                intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
                intent.putExtra("groupOnlineUsers", groupOnlineUsers);
                intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    public void showAddOnlineUserDialogue() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_add_new_user_login_dialogue_box);

        // UI Hooks
        closeDialogueButton = dialog.findViewById(R.id.close_dialogue);
        userNameTextInput = dialog.findViewById(R.id.add_email);
        userPasswordTextInput = dialog.findViewById(R.id.add_password);
        addNewUserButton = dialog.findViewById(R.id.add_new_online_user);
        progressBar = dialog.findViewById(R.id.progress_indicator);
        scanQRToAddNewUserButton = dialog.findViewById(R.id.scan_qr_add_user);

        progressBar.setVisibility(View.GONE);

        addNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Firebase Applications", FirebaseApp.getApps(getApplicationContext()).toString());
                if (validateInput()) {
                    addOnlineUser(view);
                } else {
                }
            }
        });

        closeDialogueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.70);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_toolbar_background);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AddNewUserActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean validateEmail() {
        String emailVal = userNameTextInput.getEditText().getText().toString().trim();
        if (emailVal.isEmpty()) {
            userNameTextInput.setError("Email cannot be empty");
            return false;
        } else {
            userNameTextInput.setError(null);
            userNameTextInput.setErrorEnabled(false);
            String emailRegexPattern = "^(.+)@(.+)$";
            Pattern p = Pattern.compile(emailRegexPattern);
            if (!p.matcher(emailVal).matches()) {
                userNameTextInput.setError("Email format is invalid");
                return false;
            } else {
                userNameTextInput.setError(null);
                userNameTextInput.setErrorEnabled(false);
                email = emailVal;
                return true;
            }
        }
    }

    private boolean validatePassword() {
        String passwordVal = userPasswordTextInput.getEditText().getText().toString().trim();
        if (passwordVal.isEmpty()) {
            userPasswordTextInput.setError("Password cannot be empty");
            return false;
        } else {
            userPasswordTextInput.setError(null);
            userPasswordTextInput.setErrorEnabled(false);
            if (passwordVal.length() < 6) {
                userPasswordTextInput.setError("Minimum length of password is 6");
                return false;
            } else {
                userPasswordTextInput.setError(null);
                userPasswordTextInput.setErrorEnabled(false);
                String passwordRegexPattern = "(^" +
//                        "(?=.*[0-9])" +  // at least 1 digit
//                        "(?=.*[a-z])" + // at least 1 small letter
//                        "(?=.*[A-Z])" + // at least 1 capital letter
                        "(?=.*[a-zA-Z])" + // any letter
                        "(?=.*[@#$%^&+=])" + // at least one special character
                        "(?=\\S+$)" + // no white spaces
                        ".{6,}" + // at least 6 characters
                        "$)";
                Pattern p = Pattern.compile(passwordRegexPattern);
                if (!p.matcher(passwordVal).matches()) {
                    userPasswordTextInput.setError("Password format is invalid");
                    return false;
                } else {
                    userPasswordTextInput.setError(null);
                    userPasswordTextInput.setErrorEnabled(false);
                    password = passwordVal;
                    return true;
                }
            }
        }
    }

    private boolean validateInput() {
        return !(!validatePassword() | !validateEmail());
    }

    public void addOnlineUser(View view) {
        setDialogueDismissable(false);
        FirebaseAuth currentFirebaseAuth = FirebaseAuth.getInstance();
        String currentEmail = currentFirebaseAuth.getCurrentUser().getEmail();
        if (currentEmail.equals(email)) {
            setDialogueDismissable(true);
            showSnackbar("Error! " + "You cannot add yourself to any group", "", "Error", view);
        } else {
            //    Firebase Variable
            FirebaseApp.initializeApp(this /* Context */, FirebaseApp.getInstance().getOptions(), "addNewUser");
            FirebaseApp addNewUser = FirebaseApp.getInstance("addNewUser");

            firebaseAuth = FirebaseAuth.getInstance(addNewUser);

            firebaseUser = firebaseAuth.getCurrentUser();

            progressBar.setVisibility(View.VISIBLE);
            addNewUserButton.setVisibility(View.GONE);

            if (groupOnlineUsersList.contains(email)) {
                addNewUser.delete();
                setDialogueDismissable(true);
                progressBar.setVisibility(View.GONE);
                addNewUserButton.setVisibility(View.VISIBLE);
                showSnackbar("Warning! " + "User already added in group. Try to add another user.", "", "Warning", view);
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        addNewUser.delete();
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser.isEmailVerified()) {
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
                                                        progressBar.setVisibility(View.GONE);
                                                        addNewUserButton.setVisibility(View.VISIBLE);
                                                        addNewUserButton.setBackgroundColor(getColor(R.color.success_400));
                                                        addNewUserButton.setText("New User Added");

                                                        allGroupsCollectionReference.document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                                    Map<String, Object> data = documentSnapshot.getData();

                                                                    final Handler handler = new Handler();
                                                                    handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Intent intent = new Intent(AddNewUserActivity.this, GroupAddedInfoActivity.class);
                                                                            intent.putExtra("groupId", groupId);
                                                                            intent.putExtra("groupName", groupName);
                                                                            intent.putExtra("groupDateCreated", groupDateCreated);
                                                                            intent.putExtra("groupDescription", groupDescription);
                                                                            intent.putExtra("groupOnlineUsersList", (ArrayList<String>) data.get("groupUsers"));
                                                                            intent.putExtra("groupOnlineUsers", String.valueOf(((ArrayList<String>) data.get("groupUsers")).size()));
                                                                            intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                                                                            intent.putExtra("groupOfflineUsersList", groupOnlineUsersList);
                                                                            startActivity(intent);
                                                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                        }
                                                                    }, 200);

                                                                } else {
                                                                    setDialogueDismissable(true);
                                                                    progressBar.setVisibility(View.GONE);
                                                                    addNewUserButton.setVisibility(View.VISIBLE);
                                                                    Log.e("Error", task.getException().getMessage());
                                                                }

                                                            }
                                                        });
                                                    } else {
                                                        setDialogueDismissable(true);
                                                        progressBar.setVisibility(View.GONE);
                                                        addNewUserButton.setVisibility(View.VISIBLE);
                                                        Log.e("Error", task.getException().getMessage());
                                                        // Handle Error
                                                    }

                                                }
                                            });
                                        } else {
                                            setDialogueDismissable(true);
                                            progressBar.setVisibility(View.GONE);
                                            addNewUserButton.setVisibility(View.VISIBLE);
                                            Log.e("Error", task.getException().getMessage());
                                            // Handle Error
                                        }
                                    }
                                });
                            }
                        } else {
                            setDialogueDismissable(true);
                            progressBar.setVisibility(View.GONE);
                            addNewUserButton.setVisibility(View.VISIBLE);
                            showSnackbar("Error! " + task.getException().getMessage(), "", "Error", view);
                        }
                    }
                });
            }


        }


    }

    public void setDialogueDismissable(boolean dismiss) {
        dialog.setCancelable(dismiss);
        dialog.setCanceledOnTouchOutside(dismiss);
        closeDialogueButton.setClickable(dismiss);
        scanQRToAddNewUserButton.setClickable(dismiss);
        userNameTextInput.setEnabled(dismiss);
        userPasswordTextInput.setEnabled(dismiss);
    }

    protected void showSnackbar(String messageStr, String actionStr, String resultState, View view) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar;
        if (view != null)
            snackbar = Snackbar.make(view, messageStr, Snackbar.LENGTH_LONG);
        else
            snackbar = Snackbar.make(findViewById(R.id.add_new_user_activity), messageStr, Snackbar.LENGTH_LONG);

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