package com.example.covicareapp.ui.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.covicareapp.helpers.Constants;
import com.example.covicareapp.ui.fragments.VitalsDisplayFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    //    private ArrayList<ViewPagerItem> viewPagerItemArrayList;
    private String userId;


    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public ViewPagerFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }


    public void setViewPagerItemArrayList(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = new VitalsDisplayFragment();
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                args.putInt(Constants.PAGER_TYPE, Constants.WEEK);
                break;
            case 1:
                args.putInt(Constants.PAGER_TYPE, Constants.MONTH);
                break;
            case 2:
                args.putInt(Constants.PAGER_TYPE, Constants.YEAR);
                break;
        }


        args.putString(Constants.USER_ID, userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
