package com.example.covicareapp.ui.fragments.addedGroups;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddedGroupInfoFragment extends Fragment {

    // UI Variables
    TextView groupDescriptionTextView, groupCreatedTextView, totalUsersTextView, localUsersTextView, onlineUsersTextView;

    // Firebase Variables
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    CollectionReference userCollectionReference, allGroupsCollectionReference;

    public static AddedGroupInfoFragment newInstance(@NonNull String groupId, String groupDescription, String groupCreated, String groupOnlineUsers, String groupOfflineUsers, ArrayList<String> groupOnlineUsersList, ArrayList<String> groupOfflineUsersList) {
        AddedGroupInfoFragment fragment = new AddedGroupInfoFragment();
        Bundle args = new Bundle();
        args.putString("groupId", groupId);
        args.putString("groupDescription", groupDescription);
        args.putString("groupCreated", groupCreated);
        args.putString("groupOnlineUsers", groupOnlineUsers);
        args.putString("groupOfflineUsers", groupOfflineUsers);
        args.putSerializable("groupOnlineUsersList", groupOnlineUsersList);
        args.putSerializable("groupOfflineUsers", groupOfflineUsersList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_added_group_info, container, false);
        Bundle args = getArguments();
        String groupId = args.getString("groupId");
        String groupDescription = args.getString("groupDescription");
        String groupCreated = args.getString("groupDateCreated");
        String groupOnlineUsers = args.getString("groupOnlineUsers");
        String groupOfflineUsers = args.getString("groupOfflineUsers");

        groupDescriptionTextView = rootView.findViewById(R.id.group_description);
        groupCreatedTextView = rootView.findViewById(R.id.date_created);
        totalUsersTextView = rootView.findViewById(R.id.total_users_number);
        localUsersTextView = rootView.findViewById(R.id.local_users_number);
        onlineUsersTextView = rootView.findViewById(R.id.online_users_number);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userCollectionReference = firebaseFirestore.collection("users");
        allGroupsCollectionReference = firebaseFirestore.collection("allGroups");

        groupDescriptionTextView.setText(groupDescription);
        groupCreatedTextView.setText(groupCreated);
        localUsersTextView.setText(groupOfflineUsers);
        onlineUsersTextView.setText(groupOnlineUsers);

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, 2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                totalUsersTextView.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setDuration(1000);
        animator.start();

        return rootView;
    }
}