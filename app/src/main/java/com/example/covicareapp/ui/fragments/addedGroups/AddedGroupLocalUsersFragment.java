package com.example.covicareapp.ui.fragments.addedGroups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;

import java.util.ArrayList;

public class AddedGroupLocalUsersFragment extends Fragment {

    // UI Variables
    TextView textView;

    public static AddedGroupLocalUsersFragment newInstance(@NonNull String groupId, String groupDescription, String groupCreated, String groupOnlineUsers, String groupOfflineUsers, ArrayList<String> groupOnlineUsersList, ArrayList<String> groupOfflineUsersList) {
        AddedGroupLocalUsersFragment fragment = new AddedGroupLocalUsersFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_added_group_local_users, container, false);

        textView = rootView.findViewById(R.id.text);

        Bundle args = getArguments();
        String groupId = args.getString("groupId");

        textView.setText(groupId);

        Log.i("Local", groupId);

        return rootView;
    }
}