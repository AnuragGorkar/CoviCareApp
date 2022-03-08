package com.example.covicareapp.ui.fragments.addedGroups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;

public class AddedGroupOnlineUsersFragment extends Fragment {

    public static AddedGroupOnlineUsersFragment newInstance(@NonNull String groupId) {
        AddedGroupOnlineUsersFragment fragment = new AddedGroupOnlineUsersFragment();
        Bundle args = new Bundle();
        args.putString("groupId", groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_added_group_online_users, container, false);

        Bundle args = getArguments();
        String groupId = args.getString("groupId");

        Log.i("Online", groupId);

        return rootView;
    }
}