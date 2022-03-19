package com.example.covicareapp.ui.fragments.addedGroups;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.helpers.VitalsSQLiteHelper;
import com.example.covicareapp.ui.activities.addedGroups.AddLocalUsersInfoActivity;
import com.example.covicareapp.ui.activities.addedGroups.SelectExsitingLocalUserActivity;
import com.example.covicareapp.ui.adapters.LocalUsersAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddedGroupLocalUsersFragment extends Fragment implements LocalUsersAdapter.OnLocalUserClickListener {
    public LocalUsersAdapter localUsersAdapter;
    VitalsSQLiteHelper vitalsSQLiteHelper;
    View rootView;
    //Variables
    String groupId, groupName, groupDateCreated, groupDescription, groupCreated, groupOnlineUsers, groupOfflineUsers;
    ArrayList<String> groupOnlineUsersList, groupOfflineUsersList;

    // UI Variables
    Dialog dialog;
    ImageButton closeDialogueButton;
    ImageView errorImage;
    TextView errorText;
    RecyclerView recyclerView;
    FloatingActionButton addLocalUserFab;
    MaterialButton addNewLocalUser, addExistingLocalUser;

    public static AddedGroupLocalUsersFragment newInstance(@NonNull String groupId, String groupName, String groupDateCreated, String groupDescription, String groupCreated, String groupOnlineUsers, String groupOfflineUsers, ArrayList<String> groupOnlineUsersList, ArrayList<String> groupOfflineUsersList) {
        AddedGroupLocalUsersFragment fragment = new AddedGroupLocalUsersFragment();
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
        rootView = inflater.inflate(R.layout.fragment_added_group_local_users, container, false);

        Bundle args = getArguments();
        groupId = args.getString("groupId");
        groupName = args.getString("groupName");
        groupDateCreated = args.getString("groupDateCreated");
        groupDescription = args.getString("groupDescription");
        groupCreated = args.getString("groupDateCreated");
        groupOnlineUsers = args.getString("groupOnlineUsers");
        groupOfflineUsers = args.getString("groupOfflineUsers");
        groupOnlineUsersList = (ArrayList<String>) args.getSerializable("groupOnlineUsersList");
        groupOfflineUsersList = (ArrayList<String>) args.getSerializable("groupOfflineUsersList");

        addLocalUserFab = rootView.findViewById(R.id.add_user_fab);
        errorImage = rootView.findViewById(R.id.error_image);
        errorText = rootView.findViewById(R.id.error_text);

        errorText.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        vitalsSQLiteHelper = new VitalsSQLiteHelper(getActivity());

        addLocalUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddLocalUserDialogue();
            }
        });

        initRecyclerView();

        return rootView;
    }

    public void initRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Cursor cursor = vitalsSQLiteHelper.getCursorForRecyclerViewForGroup(groupId);

        localUsersAdapter = new LocalUsersAdapter(getActivity(), cursor, "AddedGroupLocalUsersFragment");
        localUsersAdapter.setGroupId(groupId);
        recyclerView.setAdapter(localUsersAdapter);

        if (cursor.getCount() == 0) {
            errorText.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLocalUserClick(int position) {
        Log.i("Position", String.valueOf(position));

    }


    public void showAddLocalUserDialogue() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_add_new_local_user_dialogue_box);

        // UI Hooks
        closeDialogueButton = dialog.findViewById(R.id.close_dialogue);
        addNewLocalUser = dialog.findViewById(R.id.add_new_local_user);
        addExistingLocalUser = dialog.findViewById(R.id.add_existing_local_user);

        addNewLocalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left);
                Intent intent = new Intent(getActivity(), AddLocalUsersInfoActivity.class);
                intent.putExtra("backTo", "AddedGroupLocalUsersFragment");
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
        });

        addExistingLocalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left);
                Intent intent = new Intent(getActivity(), SelectExsitingLocalUserActivity.class);
                intent.putExtra("backTo", "AddedGroupLocalUsersFragment");
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

}