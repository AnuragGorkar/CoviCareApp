package com.example.covicareapp.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.covicareapp.R;
import com.example.covicareapp.models.AddedGroupsModel;
import com.example.covicareapp.ui.adapters.AddedGroupsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SelectGroupActivity extends AppCompatActivity {

    // Variables
    String groupName, groupDescription;

    // UI Variables
    RecyclerView recyclerView;
    FloatingActionButton addNewGroupFab;
    LottieAnimationView loadingLottieAnimation;
    ProgressBar progressBar;
    ImageButton closeDialogueButton;
    TextInputLayout groupNameTextInput, groupDescriptionTextInput;
    MaterialButton addNewGroupButton;
    TextView loadingTextView;
    MaterialToolbar materialToolbar;
    ImageView errorImage;
    TextView errorText;

    // Firebase Variables
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    CollectionReference userCollectionReference, allGroupsCollectionReference;

    // User Data
    HashMap<String, Object> userData;
    ArrayList<String> groupsCreatedIds;

    AddedGroupsAdapter addedGroupsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        Intent intent = getIntent();

        //UI Hooks
        recyclerView = findViewById(R.id.recycler_view);
        addNewGroupFab = findViewById(R.id.add_group_fab);
        loadingLottieAnimation = findViewById(R.id.loading_lottie);
        loadingTextView = findViewById(R.id.loading_text);
        materialToolbar = findViewById(R.id.toolbar);
        errorImage = findViewById(R.id.error_image);
        errorText = findViewById(R.id.error_text);

        errorText.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userCollectionReference = firebaseFirestore.collection("users");
        allGroupsCollectionReference = firebaseFirestore.collection("allGroups");

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectGroupActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                if (addedGroupsAdapter != null)
                    addedGroupsAdapter.stopListening();
            }
        });

        addNewGroupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProfileDialogue();
            }
        });

        getFirebaseUserData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SelectGroupActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        if (addedGroupsAdapter != null)
            addedGroupsAdapter.stopListening();
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (groupsCreatedIds.isEmpty()) {
            errorText.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        } else {
            errorText.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);

            Query query = allGroupsCollectionReference.whereIn("groupId", groupsCreatedIds);

            FirestoreRecyclerOptions<AddedGroupsModel> options = new FirestoreRecyclerOptions.Builder<AddedGroupsModel>().setQuery(query, AddedGroupsModel.class).build();

            addedGroupsAdapter = new AddedGroupsAdapter(options);

            recyclerView.setAdapter(addedGroupsAdapter);

            addedGroupsAdapter.startListening();

            addedGroupsAdapter.setOnItemClickListener(new AddedGroupsAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Map<String, Object> data = documentSnapshot.getData();

                    Intent intent = new Intent(SelectGroupActivity.this, AddNewUserActivity.class);
                    intent.putExtra("groupId", data.get("groupId").toString());
                    intent.putExtra("groupName", data.get("groupName").toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMMM yyyy");
                    Date date = ((Timestamp) data.get("dateCreated")).toDate();
                    intent.putExtra("groupDateCreated", simpleDateFormat.format(date));
                    intent.putExtra("groupDescription", data.get("groupInfo").toString());
                    intent.putExtra("groupOnlineUsers", String.valueOf(((ArrayList<String>) data.get("groupUsers")).size()));
                    intent.putExtra("groupOfflineUsers", "0");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }
    }

    public void getFirebaseUserData() {
        loadingTextView.setVisibility(View.VISIBLE);
        loadingLottieAnimation.setVisibility(View.VISIBLE);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = firebaseFirestore.collection("users");

        userCollectionReference.document(firebaseUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        userData = (HashMap<String, Object>) documentSnapshot.getData();

                        userCollectionReference.document(userData.get("email").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        groupsCreatedIds = (ArrayList<String>) documentSnapshot.getData().get("groupsCreated");
                                        groupsCreatedIds.remove(userData.get("email").toString());

                                        loadingTextView.setVisibility(View.GONE);
                                        loadingLottieAnimation.setVisibility(View.GONE);

                                        initRecyclerView();
                                    } else {
                                        // No Profile Created

                                    }
                                } else {
                                    // Error Fetching Profiles
                                }

                            }
                        });

                    } else {
                        // No User
                    }

                } else {
                    // No User
                }
            }
        });
    }

    public void showAddProfileDialogue() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_add_group_dialogue_box);

        // UI Hooks
        closeDialogueButton = dialog.findViewById(R.id.close_dialogue);
        groupNameTextInput = dialog.findViewById(R.id.group_name);
        groupDescriptionTextInput = dialog.findViewById(R.id.group_desc);
        addNewGroupButton = dialog.findViewById(R.id.add_group_btn);
        progressBar = dialog.findViewById(R.id.progress_indicator);

        progressBar.setVisibility(View.GONE);

        addNewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateGroupFields()) {
                    addNewGroup(dialog);
                } else {
                    showSnackbar("Input valid data for new group", "", "Error");
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
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_toolbar_background);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    private boolean validateGroupFields() {
        return !(!validateGroupDescription() | !validateGroupName());
    }

    public void addNewGroup(Dialog dialog) {
        Map<String, Object> groupData = new HashMap<String, Object>();
        groupData.put("dateCreated", Timestamp.now());
        groupData.put("createdBy", userData.get("email"));
        groupData.put("groupName", groupName);
        groupData.put("groupInfo", groupDescription);
        groupData.put("groupUsers", new ArrayList<String>());

        progressBar.setVisibility(View.VISIBLE);
        addNewGroupButton.setVisibility(View.GONE);

        DocumentReference newDocRef = allGroupsCollectionReference.document();
        groupData.put("groupId", newDocRef.getId());
        newDocRef.set(groupData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    userCollectionReference.document(userData.get("email").toString()).update("groupsCreated", FieldValue.arrayUnion(newDocRef.getId())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            addNewGroupButton.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                showSnackbar("Group created successfully ", "", "Success");
                                if (addedGroupsAdapter != null)
                                    addedGroupsAdapter.stopListening();
                                groupsCreatedIds.add(newDocRef.getId());
                                initRecyclerView();
                                dialog.dismiss();
                            } else {
                                showSnackbar("Error! " + task.getException().toString(), "", "Error");
                            }
                        }
                    });

                } else {
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    addNewGroupButton.setVisibility(View.VISIBLE);
                    showSnackbar("Error! " + task.getException().toString(), "", "Error");
                }
            }
        });
    }

    private boolean validateGroupName() {
        String groupNameVal = groupNameTextInput.getEditText().getText().toString().trim();
        if (groupNameVal.isEmpty()) {
            groupNameTextInput.setError("Group Name cannot be empty");
            return false;
        } else if (groupNameVal.length() < 5) {
            groupNameTextInput.setError("Group Name should be more than 5 characters");
            return false;
        } else if (groupNameVal.length() > 35) {
            groupNameTextInput.setError("Group Name should be less than 35 characters");
            return false;
        } else {
            groupNameTextInput.setError(null);
            groupNameTextInput.setErrorEnabled(false);
            groupName = groupNameVal;
            return true;
        }
    }

    private boolean validateGroupDescription() {
        String groupDescVal = groupDescriptionTextInput.getEditText().getText().toString().trim();
        if (groupDescVal.isEmpty()) {
            groupDescriptionTextInput.setError("Group description cannot be empty");
            return false;
        } else if (groupDescVal.length() < 10) {
            groupDescriptionTextInput.setError("Group description should be more than 10 characters");
            return false;
        } else if (groupDescVal.length() > 100) {
            groupDescriptionTextInput.setError("Group description should be less than 100 characters");
            return false;
        } else {
            groupDescriptionTextInput.setError(null);
            groupDescriptionTextInput.setErrorEnabled(false);
            groupDescription = groupDescVal;
            return true;
        }
    }

    protected void showSnackbar(String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(findViewById(R.id.recycler_view).getRootView(), messageStr, Snackbar.LENGTH_LONG);

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