package com.example.covicareapp.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.covicareapp.R;
import com.example.covicareapp.databinding.ActivityOnlineUserVitalsInfoBinding;
import com.example.covicareapp.helpers.Constants;
import com.example.covicareapp.models.OnlineUserVitalsModel;
import com.example.covicareapp.ui.activities.addedGroups.GroupAddedInfoActivity;
import com.example.covicareapp.ui.activities.qrscan.OnlineVitalsScanQrActivity;
import com.example.covicareapp.ui.adapters.ViewPagerFragmentAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class OnlineUserVitalsInfoActivity extends AppCompatActivity {

    private static final String TAG = "OnlineUserVitalsInfoActivity";
    //Variables
    String countryCode, countryName;
    String groupId, groupName, groupDateCreated, groupDescription, groupCreated, groupOnlineUsers, groupOfflineUsers;
    ArrayList<String> groupOnlineUsersList, groupOfflineUsersList;
    String fullName, gender, raspiUId, email, phoneNumber, userId, dateOfBirth;
    DecimalFormat df = new DecimalFormat("#.##");

    LineChart chart;

    // UI Variables
    TextView textView;
    FloatingActionButton addVitalsFab;
    int currentVital = Constants.TEMPERATURE_ID;
    int leftCardVital = Constants.PULSE_ID;
    int rightCardVital = Constants.SPO2_ID;
    private ArrayList<OnlineUserVitalsModel> vitalsData;

    private ActivityOnlineUserVitalsInfoBinding binding;
    ViewPager2 viewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOnlineUserVitalsInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (groupName == null)
            Log.i("groupName at Local Users", "Group Name is Null");
        if (groupDateCreated == null)
            Log.i("groupDateCreated at Local Users", "Group Date Created is Null");

        Intent intent = getIntent();
        dateOfBirth = intent.getStringExtra("dateOfBirth");
        countryCode = intent.getStringExtra("countryCode");
        countryName = intent.getStringExtra("countryName");
        fullName = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        phoneNumber = intent.getStringExtra("phoneNumber");
        userId = intent.getStringExtra("userId");
        raspiUId = intent.getStringExtra("raspiUId");
        gender = intent.getStringExtra("gender");

        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        groupDateCreated = intent.getStringExtra("groupDateCreated");
        groupDescription = intent.getStringExtra("groupDescription");
        groupOnlineUsers = String.valueOf(intent.getStringExtra("groupOnlineUsers"));
        groupOnlineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOnlineUsersList");
        groupOfflineUsers = intent.getStringExtra("groupOfflineUsers");
        groupOfflineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOfflineUsersList");

        // UI Hooks
        textView = findViewById(R.id.textView);
        addVitalsFab = findViewById(R.id.add_vitals_fab);

//        textView.setText(fullName + " " + email + " " + userId + " " + groupId + " " + raspiUId);


        Log.e(TAG, "onCreate: userId : " + userId);

        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
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

            TextView tv = (TextView) LayoutInflater.from(OnlineUserVitalsInfoActivity.this)
                    .inflate(R.layout.custom_tab, null);

            Objects.requireNonNull(binding.tabLayout.getTabAt(i)).setCustomView(tv);
        }

        addVitalsFab.setOnClickListener(this::requestCameraPermission);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OnlineUserVitalsInfoActivity.this, GroupAddedInfoActivity.class);
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
    }

    public void requestCameraPermission(View view) {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent(OnlineUserVitalsInfoActivity.this, OnlineVitalsScanQrActivity.class);
                intent.putExtra("countryCode", countryCode);
                intent.putExtra("countryName", countryName);
                intent.putExtra("dateOfBirth", dateOfBirth);
                intent.putExtra("email", email);
                intent.putExtra("fullName", fullName);
                intent.putExtra("gender", gender);
                intent.putExtra("raspiUId", raspiUId);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("userId", userId);

                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupName);
                intent.putExtra("groupDateCreated", groupDateCreated);
                intent.putExtra("groupDescription", groupDescription);
                intent.putExtra("groupOnlineUsersList", groupOnlineUsersList);
                intent.putExtra("groupOnlineUsers", groupOnlineUsers);
                intent.putExtra("groupOfflineUsers", groupOfflineUsers);
                intent.putExtra("groupOfflineUsersList", groupOfflineUsersList);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.success_800));
                break;
            case "Warning":
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.warning_900));
                break;
            case "Error":
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.error_400));
                break;
            default:
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.information_800));
        }
        // set the action button text color of the snackbar however this is optional
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white_50));
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white_50));

        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // as all the snackbar wont have the action button
        snackbar.show();

    }
}