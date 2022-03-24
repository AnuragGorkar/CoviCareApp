package com.example.covicareapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.covicareapp.R;
import com.example.covicareapp.databinding.FragmentVitalsHistoryBinding;
import com.example.covicareapp.ui.adapters.ViewPagerFragmentAdapter;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class VitalsHistoryFragment extends Fragment {

    private FragmentVitalsHistoryBinding binding;
    ViewPager2 viewPager;


    public static VitalsHistoryFragment newInstance() {
        return new VitalsHistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVitalsHistoryBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();


//        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId  = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), getLifecycle());
        adapter.setViewPagerItemArrayList(userId);
        viewPager = binding.pager;
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);


        new TabLayoutMediator(binding.tabLayout, binding.pager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Week");
                    break;
                case 1:
                    tab.setText("Month");
                    break;
                case 2:
                    tab.setText("Year");
                    break;

            }
        }).attach();


        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {

            TextView tv = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.custom_tab, null);

            Objects.requireNonNull(binding.tabLayout.getTabAt(i)).setCustomView(tv);
        }

        return rootView;
    }


}