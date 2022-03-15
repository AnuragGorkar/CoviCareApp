package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.covicareapp.R;
import com.example.covicareapp.ui.adapters.GroupAddedTabViewPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GroupAddedInfoActivity extends AppCompatActivity {
    int tabShow;
    private final int[] tabIcon = new int[]{R.drawable.ic_outline_info_24, R.drawable.ic_outline_cloud_24, R.drawable.ic_outline_sd_storage_24};
    private final String[] tabTitle = new String[]{"Info", "Online Users", "Local Users"};
    String groupId, groupName, groupDateCreated, groupDescription, groupOfflineUsers, groupOnlineUsers;

    // UI Variables
    MaterialToolbar materialToolbar;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    GroupAddedTabViewPagerAdapter groupAddedTabViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_added_info);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        groupDateCreated = intent.getStringExtra("groupDateCreated");
        groupDescription = intent.getStringExtra("groupDescription");
        groupOnlineUsers = String.valueOf(intent.getStringExtra("groupOnlineUsers"));
        groupOfflineUsers = intent.getStringExtra("groupOfflineUsers");

        // UI Hooks
        materialToolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.view_pager);
        groupAddedTabViewPagerAdapter = new GroupAddedTabViewPagerAdapter(this, groupId, groupDateCreated, groupDescription, groupOnlineUsers, groupOfflineUsers);

        materialToolbar.setTitle(groupName);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupAddedInfoActivity.this, MainActivity.class);
                intent.putExtra("Fragment", "Added Groups");
                startActivity(intent);
            }
        });

        viewPager2.setAdapter(groupAddedTabViewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> {
            tab.setIcon(tabIcon[position]);
            tab.setText(tabTitle[position]);
        })).attach();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GroupAddedInfoActivity.this, MainActivity.class);
        intent.putExtra("Fragment", "Added Groups");
        startActivity(intent);
    }
}