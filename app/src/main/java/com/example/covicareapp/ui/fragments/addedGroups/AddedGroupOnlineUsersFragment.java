package com.example.covicareapp.ui.fragments.addedGroups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.models.UserModel;
import com.example.covicareapp.ui.adapters.UserAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class AddedGroupOnlineUsersFragment extends Fragment {
    View rootView;

    //Variables
    String groupId, groupDescription, groupCreated, groupOnlineUsers, groupOfflineUsers;
    ArrayList<String> groupOnlineUsersList, groupOfflineUsersList;

    // UI Variables
    ProgressBar progressBar;
    ImageView errorImage;
    TextView errorText;
    RecyclerView recyclerView;

    // Firebase Variables
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    CollectionReference userCollectionReference, allGroupsCollectionReference;

    UserAdapter userAdapter;

    public static AddedGroupOnlineUsersFragment newInstance(@NonNull String groupId, String groupDescription, String groupCreated, String groupOnlineUsers, String groupOfflineUsers, ArrayList<String> groupOnlineUsersList, ArrayList<String> groupOfflineUsersList) {
        AddedGroupOnlineUsersFragment fragment = new AddedGroupOnlineUsersFragment();
        Bundle args = new Bundle();
        args.putString("groupId", groupId);
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

    public void initRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (groupOnlineUsersList.isEmpty()) {
            errorText.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        } else {
            errorText.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);

            Query query = userCollectionReference.whereIn("email", groupOnlineUsersList);

            Log.i("ONLINE USERS", groupOnlineUsersList.toString());

            FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();

            userAdapter = new UserAdapter(options);

            recyclerView.setAdapter(userAdapter);

            userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {

                }
            });
        }
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
}