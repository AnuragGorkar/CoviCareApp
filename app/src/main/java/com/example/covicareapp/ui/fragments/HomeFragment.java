package com.example.covicareapp.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;
import com.example.covicareapp.ui.activities.ScanQrActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class HomeFragment extends Fragment {

    //    Variables
    boolean isAllEFabVisible;

    //    UI Variables
    FloatingActionButton oneTimeRecordVitals, continuousRecordVitals, ctScanImage;
    ExtendedFloatingActionButton recordVitals;
    MaterialTextView oneTimeRecordText, continuousMonitorText, ctScanText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        oneTimeRecordVitals = rootView.findViewById(R.id.one_time_monitoring_fab);
        continuousRecordVitals = rootView.findViewById(R.id.continuous_monitoring_fab);
        recordVitals = rootView.findViewById(R.id.record_vitals_efab);
        oneTimeRecordText = rootView.findViewById(R.id.one_time_record_tv);
        continuousMonitorText = rootView.findViewById(R.id.continuous_monitoring_tv);
        ctScanImage = rootView.findViewById(R.id.scan_ct_fab);
        ctScanText = rootView.findViewById(R.id.scanct_tv);

        oneTimeRecordVitals.setVisibility(View.GONE);
        continuousRecordVitals.setVisibility((View.GONE));
        ctScanImage.setVisibility((View.GONE));
        oneTimeRecordText.setVisibility(View.GONE);
        continuousMonitorText.setVisibility((View.GONE));
        ctScanText.setVisibility((View.GONE));

        isAllEFabVisible = false;

        recordVitals.shrink();

        Intent intent = getActivity().getIntent();
        String vitals_data = intent.getStringExtra("Vitals Data");


        recordVitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllEFabVisible) {
                    oneTimeRecordVitals.show();
                    continuousRecordVitals.show();
                    ctScanImage.show();
                    oneTimeRecordText.setVisibility(View.VISIBLE);
                    continuousMonitorText.setVisibility(View.VISIBLE);
                    ctScanText.setVisibility((View.VISIBLE));

                    recordVitals.extend();

                    isAllEFabVisible = true;
                } else {
                    oneTimeRecordVitals.hide();
                    continuousRecordVitals.hide();
                    ctScanImage.hide();
                    oneTimeRecordText.setVisibility(View.GONE);
                    continuousMonitorText.setVisibility(View.GONE);
                    ctScanText.setVisibility((View.GONE));

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
                requestCameraPermission(view);
            }
        });

        ctScanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return rootView;
    }


    public void requestCameraPermission(View view) {
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ScanQrActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                showSnackbar(view, "Please allow permission for QR code Camera scan", "", "Error");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                showSnackbar(view, "Please allow permission for QR code Camera scan", "", "Error");
            }
        }).check();
    }


    protected void showSnackbar(View view, String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(view, messageStr, Snackbar.LENGTH_LONG);

        // the duration is in terms of milliseconds
        snackbar.setDuration(3600);
        // set the background tint color for the snackbar
        switch (resultState) {
            case "Success":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.success_800));
                break;
            case "Warning":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.warning_900));
                break;
            case "Error":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.error_400));
                break;
            default:
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.information_800));
        }
        // set the action button text color of the snackbar however this is optional
        snackbar.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_50));
        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.white_50));

        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // as all the snackbar wont have the action button
        snackbar.show();

    }
}