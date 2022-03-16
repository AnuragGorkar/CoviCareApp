package com.example.covicareapp.ui.fragments.addedGroups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.helpers.VitalsSQLiteHelper;
import com.example.covicareapp.ui.adapters.LocalUsersAdapter;

import java.util.ArrayList;

public class AddedGroupLocalUsersFragment extends Fragment {
    public LocalUsersAdapter localUsersAdapter;
    VitalsSQLiteHelper vitalsSQLiteHelper;
    View rootView;
    //Variables
    String groupId, groupDescription, groupCreated, groupOnlineUsers, groupOfflineUsers;
    ArrayList<String> groupOnlineUsersList, groupOfflineUsersList;

    // UI Variables
    ImageView errorImage;
    TextView errorText;
    RecyclerView recyclerView;

    public static AddedGroupLocalUsersFragment newInstance(@NonNull String groupId, String groupDescription, String groupCreated, String groupOnlineUsers, String groupOfflineUsers, ArrayList<String> groupOnlineUsersList, ArrayList<String> groupOfflineUsersList) {
        AddedGroupLocalUsersFragment fragment = new AddedGroupLocalUsersFragment();
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
        rootView = inflater.inflate(R.layout.fragment_added_group_local_users, container, false);

        Bundle args = getArguments();
        groupId = args.getString("groupId");
        groupDescription = args.getString("groupDescription");
        groupCreated = args.getString("groupDateCreated");
        groupOnlineUsers = args.getString("groupOnlineUsers");
        groupOfflineUsers = args.getString("groupOfflineUsers");
        groupOnlineUsersList = (ArrayList<String>) args.getSerializable("groupOnlineUsersList");
        groupOfflineUsersList = (ArrayList<String>) args.getSerializable("groupOfflineUsersList");

        errorImage = rootView.findViewById(R.id.error_image);
        errorText = rootView.findViewById(R.id.error_text);

        errorText.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        vitalsSQLiteHelper = new VitalsSQLiteHelper(getActivity());

        initRecyclerView();

        return rootView;
    }

    public void initRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        localUsersAdapter = new LocalUsersAdapter(getActivity(), vitalsSQLiteHelper.getCursorForRecyclerViewForGroup(groupId));

        recyclerView.setAdapter(localUsersAdapter);
    }
}