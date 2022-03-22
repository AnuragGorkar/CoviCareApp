package com.example.covicareapp.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
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
import com.example.covicareapp.models.GroupsAddedToModel;
import com.example.covicareapp.ui.adapters.GroupsAddedToAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupsAddedToFragment extends Fragment {

    View rootView;

    //UI Variables
    RecyclerView recyclerView;
    ImageView errorImage;
    ImageButton closeDialogueButton;
    TextView errorText;
    TextView groupName;
    ProgressBar progressBar;
    MaterialButton yesRemoveButton, noRemoveButton;

    // User Data
    HashMap<String, Object> userDataVal;
    ArrayList<String> groupsAddedToIdsVal;

    // Firebase Variables
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    CollectionReference userCollectionReference, allGroupsCollectionReference;

    GroupsAddedToAdapter groupsAddedToAdapter;


    public static GroupsAddedToFragment newInstance(@NonNull HashMap<String, Object> userData, @NonNull ArrayList<String> groupsAddedToIds) {
        GroupsAddedToFragment fragment = new GroupsAddedToFragment();

        Bundle args = new Bundle();
        args.putSerializable("userData", userData);
        args.putSerializable("groupsAddedToIds", groupsAddedToIds);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_groups_added_to, container, false);
        Bundle args = getArguments();
        userDataVal = (HashMap<String, Object>) args.getSerializable("userData");
        groupsAddedToIdsVal = (ArrayList<String>) args.getSerializable("groupsAddedToIds");

        errorImage = rootView.findViewById(R.id.error_image);
        errorText = rootView.findViewById(R.id.error_text);

        errorText.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userCollectionReference = firebaseFirestore.collection("users");
        allGroupsCollectionReference = firebaseFirestore.collection("allGroups");

        initRecyclerView();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (groupsAddedToAdapter != null)
            groupsAddedToAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (groupsAddedToAdapter != null)
            groupsAddedToAdapter.stopListening();
    }

    public void initRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (groupsAddedToIdsVal.isEmpty()) {
            errorText.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        } else {
            errorText.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);

            Query query = allGroupsCollectionReference.whereIn("groupId", groupsAddedToIdsVal);

            FirestoreRecyclerOptions<GroupsAddedToModel> options = new FirestoreRecyclerOptions.Builder<GroupsAddedToModel>().setQuery(query, GroupsAddedToModel.class).build();

            groupsAddedToAdapter = new GroupsAddedToAdapter(options);

            recyclerView.setAdapter(groupsAddedToAdapter);

//            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//                @Override
//                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                    return false;
//                }
//
//                @Override
//                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                }
//            }).attachToRecyclerView(recyclerView);

        }
    }

    protected void showSnackbar(String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.drawer_layout).getRootView(), messageStr, Snackbar.LENGTH_LONG);

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

    public void showRemoveFromGroupDialogue(int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_remove_from_group_dialogue_box);

        // UI Hooks
        closeDialogueButton = dialog.findViewById(R.id.close_dialogue);
        progressBar = dialog.findViewById(R.id.progress_indicator);
        yesRemoveButton = dialog.findViewById(R.id.yes_remove_button);
        noRemoveButton = dialog.findViewById(R.id.no_remove_button);
        groupName = dialog.findViewById(R.id.group_name_text_view);

        progressBar.setVisibility(View.GONE);

        yesRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        noRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
}