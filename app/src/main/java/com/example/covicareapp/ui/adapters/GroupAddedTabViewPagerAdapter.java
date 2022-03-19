package com.example.covicareapp.ui.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupInfoFragment;
import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupLocalUsersFragment;
import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupOnlineUsersFragment;

import java.util.ArrayList;

public class GroupAddedTabViewPagerAdapter extends FragmentStateAdapter {

    private final String[] tabTitle = new String[]{"Info", "Online Users", "Local Users"};
    String groupIdVal, groupNameVal, groupDescriptionVal, groupDateCreatedVal, groupOnlineUsersVal, groupOfflineUsersVal;
    ArrayList<String> groupOnlineUsersListVal = new ArrayList<String>();
    ArrayList<String> groupOfflineUsersListVal = new ArrayList<String>();
    Bundle bundle;

    public GroupAddedTabViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String groupId, String groupName, String groupDateCreated, String groupDescription, String groupOnlineUsers, String groupOfflineUsers, ArrayList<String> groupOnlineUsersList, ArrayList<String> groupOfflineUsersList) {
        super(fragmentActivity);
        groupIdVal = groupId;
        groupNameVal = groupName;
        groupDateCreatedVal = groupDateCreated;
        groupDescriptionVal = groupDescription;
        groupOnlineUsersVal = groupOnlineUsers;
        groupOfflineUsersVal = groupOfflineUsers;
        groupOnlineUsersListVal = groupOnlineUsersList;
        groupOfflineUsersListVal = groupOfflineUsersList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                bundle = new Bundle();
                bundle.putString("groupId", groupIdVal);
                bundle.putString("groupName", groupNameVal);
                bundle.putString("groupDateCreated", groupDateCreatedVal);
                bundle.putString("groupDescription", groupDescriptionVal);
                bundle.putString("groupOnlineUsers", groupOnlineUsersVal);
                bundle.putString("groupOfflineUsers", groupOfflineUsersVal);
                bundle.putSerializable("groupOnlineUsersList", groupOnlineUsersListVal);
                bundle.putSerializable("groupOfflineUsersList", groupOfflineUsersListVal);
                AddedGroupOnlineUsersFragment addedGroupOnlineUsersFragment = new AddedGroupOnlineUsersFragment();
                addedGroupOnlineUsersFragment.setArguments(bundle);
                return addedGroupOnlineUsersFragment;
            case 2:
                bundle = new Bundle();
                bundle.putString("groupId", groupIdVal);
                bundle.putString("groupName", groupNameVal);
                bundle.putString("groupDateCreated", groupDateCreatedVal);
                bundle.putString("groupDescription", groupDescriptionVal);
                bundle.putString("groupOnlineUsers", groupOnlineUsersVal);
                bundle.putString("groupOfflineUsers", groupOfflineUsersVal);
                bundle.putSerializable("groupOnlineUsersList", groupOnlineUsersListVal);
                bundle.putSerializable("groupOfflineUsersList", groupOfflineUsersListVal);
                AddedGroupLocalUsersFragment addedGroupLocalUsersFragment = new AddedGroupLocalUsersFragment();
                addedGroupLocalUsersFragment.setArguments(bundle);
                return addedGroupLocalUsersFragment;
            default:
                bundle = new Bundle();
                bundle.putString("groupId", groupIdVal);
                bundle.putString("groupName", groupNameVal);
                bundle.putString("groupDateCreated", groupDateCreatedVal);
                bundle.putString("groupDescription", groupDescriptionVal);
                bundle.putString("groupOnlineUsers", groupOnlineUsersVal);
                bundle.putString("groupOfflineUsers", groupOfflineUsersVal);
                bundle.putSerializable("groupOnlineUsersList", groupOnlineUsersListVal);
                bundle.putSerializable("groupOfflineUsersList", groupOfflineUsersListVal);
                AddedGroupInfoFragment addedGroupInfoFragment = new AddedGroupInfoFragment();
                addedGroupInfoFragment.setArguments(bundle);
                return addedGroupInfoFragment;
        }
    }

    @Override
    public int getItemCount() {
        return tabTitle.length;
    }
}
