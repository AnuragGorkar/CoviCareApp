package com.example.covicareapp.ui.adapters;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupInfoFragment;
import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupLocalUsersFragment;
import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupOnlineUsersFragment;

public class GroupAddedTabViewPagerAdapter extends FragmentStateAdapter {

    private final String[] tabTitle = new String[]{"Info", "Online Users", "Local Users"};
    int tabShowVal;
    String groupIdVal, groupDescriptionVal, groupDateCreatedVal, groupOnlineUsersVal, groupOfflineUsersVal;
    Bundle bundle;

    public GroupAddedTabViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String groupId, String groupDateCreated, String groupDescription, String groupOnlineUsers, String groupOfflineUsers, int tabShow) {
        super(fragmentActivity);
        tabShowVal = tabShow;
        groupIdVal = groupId;
        groupDateCreatedVal = groupDateCreated;
        groupDescriptionVal = groupDescription;
        groupOnlineUsersVal = groupOnlineUsers;
        groupOfflineUsersVal = groupOfflineUsers;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (tabShowVal != -1) {
            position = tabShowVal;
            tabShowVal = -1;
        }
        switch (position) {
            case 1:
                bundle = new Bundle();
                bundle.putString("groupId", groupIdVal);
                AddedGroupOnlineUsersFragment addedGroupOnlineUsersFragment = new AddedGroupOnlineUsersFragment();
                addedGroupOnlineUsersFragment.setArguments(bundle);
                return addedGroupOnlineUsersFragment;
            case 2:
                bundle = new Bundle();
                bundle.putString("groupId", groupIdVal);
                AddedGroupLocalUsersFragment addedGroupLocalUsersFragment = new AddedGroupLocalUsersFragment();
                addedGroupLocalUsersFragment.setArguments(bundle);
                return addedGroupLocalUsersFragment;
            default:
                bundle = new Bundle();
                bundle.putString("groupId", groupIdVal);
                bundle.putString("groupDateCreated", groupDateCreatedVal);
                Log.i("Date Added Val", groupDateCreatedVal);
                bundle.putString("groupDescription", groupDescriptionVal);
                bundle.putString("groupOnlineUsers", groupOnlineUsersVal);
                Log.i("Online Users Val", String.valueOf(groupOnlineUsersVal));
                bundle.putString("groupOfflineUsers", groupOfflineUsersVal);
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
