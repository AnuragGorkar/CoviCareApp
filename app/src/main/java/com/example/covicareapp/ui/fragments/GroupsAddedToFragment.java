package com.example.covicareapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupsAddedToFragment extends Fragment {

    //UI Variables
    RecyclerView recyclerView;

    // User Data
    HashMap<String, Object> userDataVal;
    ArrayList<String> groupsAddedToIdsVal;


    public static GroupsAddedToFragment newInstance(@NonNull HashMap<String, Object> userData, @NonNull ArrayList<String> groupsAddedToIds) {
        GroupsAddedToFragment fragment = new GroupsAddedToFragment();

        Bundle args = new Bundle();
        args.putSerializable("userData", userData);
        args.putSerializable("groupsAddedToIds", groupsAddedToIds);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groups_added_to, container, false);
        Bundle args = getArguments();

        userDataVal = (HashMap<String, Object>) args.getSerializable("userData");
        groupsAddedToIdsVal = (ArrayList<String>) args.getSerializable("groupsAddedToIds");

        initRecyclerView();

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (addedGroupsAdapter != null)
//            addedGroupsAdapter.stopListening();
    }

    public void initRecyclerView() {
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
}