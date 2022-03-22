package com.example.covicareapp.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.covicareapp.R;
import com.example.covicareapp.ui.activities.addedGroups.GroupAddedInfoActivity;
import com.example.covicareapp.ui.activities.qrscan.OnlineVitalsScanQrActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class OnlineUserVitalsInfoActivity extends AppCompatActivity {

    //Variables
    String countryCode, countryName;
    String groupId, groupName, groupDateCreated, groupDescription, groupCreated, groupOnlineUsers, groupOfflineUsers;
    ArrayList<String> groupOnlineUsersList, groupOfflineUsersList;
    String fullName, gender, raspiUId, email, phoneNumber, userId, dateOfBirth;

    // UI Variables
    TextView textView;
    FloatingActionButton addVitalsFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_user_vitals_info);

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

        textView.setText(fullName + " " + email + " " + userId + " " + groupId + " " + raspiUId);

        addVitalsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission(view);
            }
        });


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