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

public class AddedGroupLocalUsersFragment extends Fragment {

    // UI Variables
    TextView textView;

    public static AddedGroupLocalUsersFragment newInstance(@NonNull String groupId) {
        AddedGroupLocalUsersFragment fragment = new AddedGroupLocalUsersFragment();
        Bundle args = new Bundle();
        args.putString("groupId", groupId);
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