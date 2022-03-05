package com.example.covicareapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;


public class HomeFragment extends Fragment {

    //    Variables
    boolean isAllEFabVisible;

    //    UI Variables
    FloatingActionButton oneTimeRecordVitals, continuousRecordVitals;
    ExtendedFloatingActionButton recordVitals;
    MaterialTextView oneTimeRecordText, continuousMonitorText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        oneTimeRecordVitals = rootView.findViewById(R.id.one_time_monitoring_fab);
        continuousRecordVitals = rootView.findViewById(R.id.continuous_monitoring_fab);
        recordVitals = rootView.findViewById(R.id.record_vitals_efab);
        oneTimeRecordText = rootView.findViewById(R.id.one_time_record_tv);
        continuousMonitorText = rootView.findViewById(R.id.continuous_monitoring_tv);

        oneTimeRecordVitals.setVisibility(View.GONE);
        continuousRecordVitals.setVisibility((View.GONE));
        oneTimeRecordText.setVisibility(View.GONE);
        continuousMonitorText.setVisibility((View.GONE));

        isAllEFabVisible = false;

        recordVitals.shrink();

        recordVitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllEFabVisible) {
                    oneTimeRecordVitals.show();
                    continuousRecordVitals.show();
                    oneTimeRecordText.setVisibility(View.VISIBLE);
                    continuousMonitorText.setVisibility(View.VISIBLE);

                    recordVitals.extend();

                    isAllEFabVisible = true;
                } else {
                    oneTimeRecordVitals.hide();
                    continuousRecordVitals.hide();
                    oneTimeRecordText.setVisibility(View.GONE);
                    continuousMonitorText.setVisibility(View.GONE);

                    recordVitals.shrink();

                    isAllEFabVisible = false;
                }
            }
        });

        continuousRecordVitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        oneTimeRecordVitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return rootView;
    }
}