package com.example.covicareapp.ui.fragments.addedGroups;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.models.OnlineUserModel;
import com.example.covicareapp.ui.activities.addedGroups.AddNewUserActivity;
import com.example.covicareapp.ui.activities.qrscan.AddOnlineUserScanQrActivity;
import com.example.covicareapp.ui.adapters.OnlineUserAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class AddedGroupOnlineUsersFragment extends Fragment {
    View rootView;

    //Variables
    String email, password;
    String groupId, groupName, groupDateCreated, groupDescription, groupCreated, groupOnlineUsers, groupOfflineUsers;
    ArrayList<String> groupOnlineUsersList, groupOfflineUsersList;

    // UI Variables
    Dialog dialog;
    ProgressBar progressBar;
    ImageView errorImage;
    ImageButton closeDialogueButton;
    TextView errorText;
    TextInputLayout userNameTextInput, userPasswordTextInput;
    RecyclerView recyclerView;
    MaterialButton addNewUserButton, scanQRToAddNewUserButton;
    FloatingActionButton addNewUserFab;

    // Firebase Variables
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    CollectionReference userCollectionReference, allGroupsCollectionReference;

    OnlineUserAdapter userAdapter;

    public static AddedGroupOnlineUsersFragment newInstance(@NonNull String groupId, String groupName, String groupDateCreated, String groupDescription, String groupCreated, String groupOnlineUsers, String groupOfflineUsers, ArrayList<String> groupOnlineUsersList, ArrayList<String> groupOfflineUsersList) {
        AddedGroupOnlineUsersFragment fragment = new AddedGroupOnlineUsersFragment();
        Bundle args = new Bundle();
        args.putString("groupId", groupId);
        args.putString("groupName", groupName);
        args.putString("groupDateCreated", groupDateCreated);
        args.putString("groupDescription", groupDescription);
        args.putString("groupCreated", groupCreated);
        args.putString("groupOnlineUsers", groupOnlineUsers);
        args.putString("groupOfflineUsers", groupOfflineUsers);
        args.putSerializable("groupOnlineUsersList", groupOnlineUsersList);
        args.putSerializable("groupOfflineUsersList", groupOfflineUsersList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_added_group_online_users, container, false);

        Bundle args = getArguments();
        groupId = args.getString("groupId");
        groupDescription = args.getString("groupDescription");
        groupCreated = args.getString("groupDateCreated");
        groupOnlineUsers = args.getString("groupOnlineUsers");
        groupOfflineUsers = args.getString("groupOfflineUsers");
        groupOnlineUsersList = (ArrayList<String>) args.getSerializable("groupOnlineUsersList");
        groupOfflineUsersList = (ArrayList<String>) args.getSerializable("groupOfflineUsersList");

        progressBar = rootView.findViewById(R.id.progress_indicator);
        errorImage = rootView.findViewById(R.id.error_image);
        errorText = rootView.findViewById(R.id.error_text);
        addNewUserFab = rootView.findViewById(R.id.add_user_fab);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        errorText.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userCollectionReference = firebaseFirestore.collection("users");
        allGroupsCollectionReference = firebaseFirestore.collection("allGroups");

        addNewUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddOnlineUserDialogue();
            }
        });

        initRecyclerView();

        return rootView;
    }

    public void initRecyclerView() {

        if (groupOnlineUsersList.isEmpty()) {
            errorText.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        } else {
            errorText.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);

            Query query = userCollectionReference.whereIn("email", groupOnlineUsersList);

            Log.i("ONLINE USERS", groupOnlineUsersList.toString());

            FirestoreRecyclerOptions<OnlineUserModel> options = new FirestoreRecyclerOptions.Builder<OnlineUserModel>().setQuery(query, OnlineUserModel.class).build();

            userAdapter = new OnlineUserAdapter(options);

            recyclerView.setAdapter(userAdapter);

            userAdapter.setOnItemClickListener(new OnlineUserAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Map<String, Object> data = documentSnapshot.getData();

                    Intent intent = new Intent(getActivity(), AddNewUserActivity.class);
                    intent.putExtra("groupId", data.get("groupId").toString());
                    intent.putExtra("groupName", data.get("groupName").toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMMM yyyy");
                    Date date = ((Timestamp) data.get("dateCreated")).toDate();
                    intent.putExtra("groupDateCreated", simpleDateFormat.format(date));
                    intent.putExtra("groupDescription", data.get("groupInfo").toString());
                    intent.putExtra("groupOnlineUsersList", (ArrayList<String>) data.get("groupUsers"));
                    intent.putExtra("groupOnlineUsers", String.valueOf(((ArrayList<String>) data.get("groupUsers")).size()));
                    intent.putExtra("groupOfflineUsersList", new ArrayList<String>());
                    intent.putExtra("groupOfflineUsers", "0");
                    startActivity(intent);
                }
            });


        }
    }

    public void requestCameraPermission(View view) {
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left);
                Intent intent = new Intent(getActivity(), AddOnlineUserScanQrActivity.class);
                intent.putExtra("backTo", "GroupAddedInfoActivity");
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupName);
                intent.putExtra("groupDateCreated", groupDateCreated);
                intent.putExtra("groupDescription", groupDescription);
                intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
                intent.putExtra("groupOnlineUsers", groupOnlineUsers);
                intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
                startActivity(intent, options.toBundle());
                getActivity().finish();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                showSnackbar("Please allow permission for QR code Camera scan", "", "Error", view);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                showSnackbar("Please allow permission for QR code Camera scan", "", "Error", view);
            }
        }).check();
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
            FirebaseApp.initializeApp(getActivity() /* Context */, FirebaseApp.getInstance().getOptions(), "addNewUser");
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
                                                        allGroupsCollectionReference.document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    addNewUserButton.setVisibility(View.VISIBLE);
                                                                    addNewUserButton.setBackgroundColor(getActivity().getColor(R.color.success_400));
                                                                    addNewUserButton.setText("New User Added");

                                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                                    Map<String, Object> data = documentSnapshot.getData();

                                                                    final Handler handler = new Handler();
                                                                    handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            // Dismiss Dialogue and update the recycler view
                                                                            groupOnlineUsersList.add(email);
                                                                            if (userAdapter != null)
                                                                                userAdapter.stopListening();
                                                                            initRecyclerView();


                                                                            userAdapter.startListening();
                                                                            dialog.dismiss();
                                                                            showSnackbar("New user added successfully", "", "Success", view);
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

    public void showAddOnlineUserDialogue() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_add_new_user_login_dialogue_box);

        // UI Hooks
        closeDialogueButton = dialog.findViewById(R.id.close_dialogue);
        userNameTextInput = dialog.findViewById(R.id.add_email);
        userPasswordTextInput = dialog.findViewById(R.id.add_password);
        addNewUserButton = dialog.findViewById(R.id.add_new_online_user);
        progressBar = dialog.findViewById(R.id.progress_indicator);
        scanQRToAddNewUserButton = dialog.findViewById(R.id.scan_qr_add_user);

        progressBar.setVisibility(View.GONE);

        scanQRToAddNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission(view);

            }
        });

        addNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Firebase Applications", FirebaseApp.getApps(getActivity()).toString());
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

    public void setDialogueDismissable(boolean dismiss) {
        dialog.setCancelable(dismiss);
        dialog.setCanceledOnTouchOutside(dismiss);
        closeDialogueButton.setClickable(dismiss);
        scanQRToAddNewUserButton.setClickable(dismiss);
        userNameTextInput.setEnabled(dismiss);
        userPasswordTextInput.setEnabled(dismiss);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (userAdapter != null)
            userAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (userAdapter != null)
            userAdapter.stopListening();
    }

    protected void showSnackbar(String messageStr, String actionStr, String resultState, View view) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar;
        if (view != null)
            snackbar = Snackbar.make(view, messageStr, Snackbar.LENGTH_LONG);
        else
            snackbar = Snackbar.make(getActivity().findViewById(R.id.add_new_user_activity), messageStr, Snackbar.LENGTH_LONG);

        // the duration is in terms of milliseconds
        snackbar.setDuration(3600);
        // set the background tint color for the snackbar
        switch (resultState) {
            case "Success":
                snackbar.setBackgroundTint(getActivity().getColor(R.color.success_800));
                break;
            case "Warning":
                snackbar.setBackgroundTint(getActivity().getColor(R.color.warning_900));
                break;
            case "Error":
                snackbar.setBackgroundTint(getActivity().getColor(R.color.error_400));
                break;
            default:
                snackbar.setBackgroundTint(getActivity().getColor(R.color.information_800));
        }
        // set the action button text color of the snackbar however this is optional
        snackbar.setTextColor(getActivity().getColor(R.color.white_50));
        snackbar.setActionTextColor(getActivity().getColor(R.color.white_50));

        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // as all the snackbar wont have the action button
        snackbar.show();

    }
}