package com.example.covicareapp.ui.activities.addedGroups;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covicareapp.R;
import com.example.covicareapp.helpers.VitalsSQLiteHelper;
import com.example.covicareapp.ui.adapters.LocalUsersAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class SelectExsitingLocalUserActivity extends AppCompatActivity implements LocalUsersAdapter.OnLocalUserClickListener {

    public LocalUsersAdapter localUsersAdapter;
    VitalsSQLiteHelper vitalsSQLiteHelper;

    // Variables
    String backActivity;
    String groupId, groupName, groupDateCreated, groupDescription, groupOfflineUsers, groupOnlineUsers, email, password;
    ArrayList<String> groupOnlineUsersList = new ArrayList<String>();
    ArrayList<String> groupOfflineUsersList = new ArrayList<String>();

    // UI Variables
    ImageView errorImage;
    TextView errorText;
    MaterialToolbar materialToolbar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exsiting_local_user);

        Intent intent = getIntent();
        backActivity = intent.getStringExtra("backTo");
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        groupDateCreated = intent.getStringExtra("groupDateCreated");
        groupDescription = intent.getStringExtra("groupDescription");
        groupOnlineUsers = String.valueOf(intent.getStringExtra("groupOnlineUsers"));
        groupOfflineUsers = intent.getStringExtra("groupOfflineUsers");
        groupOnlineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOnlineUsersList");

        // UI Hooks
        recyclerView = findViewById(R.id.recycler_view);
        materialToolbar = findViewById(R.id.toolbar);
        errorImage = findViewById(R.id.error_image);
        errorText = findViewById(R.id.error_text);

        errorText.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (backActivity != null && backActivity.equals("AddedGroupLocalUsersFragment")) {
                    intent = new Intent(SelectExsitingLocalUserActivity.this, GroupAddedInfoActivity.class);

                } else {
                    intent = new Intent(SelectExsitingLocalUserActivity.this, AddNewUserActivity.class);

                }
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupName);
                intent.putExtra("groupDateCreated", groupDateCreated);
                intent.putExtra("groupDescription", groupDescription);
                intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
                intent.putExtra("groupOnlineUsers", groupOnlineUsers);
                intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        vitalsSQLiteHelper = new VitalsSQLiteHelper(SelectExsitingLocalUserActivity.this);

        initRecyclerView();
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectExsitingLocalUserActivity.this));

        Cursor cursor = vitalsSQLiteHelper.getCursorForRecyclerViewForNotInGroup(groupId);

        localUsersAdapter = new LocalUsersAdapter(SelectExsitingLocalUserActivity.this, cursor, "SelectExsitingLocalUserActivity");
        localUsersAdapter.setGroupInfo(groupId, groupName, groupDateCreated, groupDescription, groupOfflineUsers, groupOnlineUsers, groupOnlineUsersList, groupOfflineUsersList);
        recyclerView.setAdapter(localUsersAdapter);

        if (cursor.getCount() == 0) {
            errorText.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        if (backActivity != null && backActivity.equals("AddedGroupLocalUsersFragment")) {
            intent = new Intent(SelectExsitingLocalUserActivity.this, GroupAddedInfoActivity.class);

        } else {
            intent = new Intent(SelectExsitingLocalUserActivity.this, AddNewUserActivity.class);

        }
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupDateCreated", groupDateCreated);
        intent.putExtra("groupDescription", groupDescription);
        intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
        intent.putExtra("groupOnlineUsers", groupOnlineUsers);
        intent.putExtra("groupOfflineUsers", groupOfflineUsers);
        intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onLocalUserClick(int position) {
        Log.i("Position", String.valueOf(position));

    }
}