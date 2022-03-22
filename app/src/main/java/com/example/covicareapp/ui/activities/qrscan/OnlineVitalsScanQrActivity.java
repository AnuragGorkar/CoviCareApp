package com.example.covicareapp.ui.activities.qrscan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.covicareapp.R;
import com.example.covicareapp.helpers.VitalsSQLiteHelper;
import com.example.covicareapp.logic.EncryptDecryptData;
import com.example.covicareapp.models.OnlineUserVitalsModel;
import com.example.covicareapp.ui.activities.OnlineUserVitalsInfoActivity;
import com.google.firebase.Timestamp;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Map;

public class OnlineVitalsScanQrActivity extends AppCompatActivity {

    //Variables
    String groupId, groupName, groupDateCreated, groupDescription, groupCreated, groupOnlineUsers, groupOfflineUsers;
    ArrayList<String> groupOnlineUsersList, groupOfflineUsersList;
    String fullName, gender, raspiUId, email, phoneNumber, userId, dateOfBirth, countryCode, countryName;

    //    UI Variables
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    TextView scannedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_scan_qr);

        Context context = this;

        Intent intent = getIntent();
        dateOfBirth = intent.getStringExtra("dateOfBirth");
        fullName = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        phoneNumber = intent.getStringExtra("phoneNumber");
        userId = intent.getStringExtra("userId");
        raspiUId = intent.getStringExtra("raspiUId");
        gender = intent.getStringExtra("gender");
        countryCode = intent.getStringExtra("countryCode");
        countryName = intent.getStringExtra("countryName");

        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        groupDateCreated = intent.getStringExtra("groupDateCreated");
        groupDescription = intent.getStringExtra("groupDescription");
        groupOnlineUsers = String.valueOf(intent.getStringExtra("groupOnlineUsers"));
        groupOnlineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOnlineUsersList");
        groupOfflineUsers = intent.getStringExtra("groupOfflineUsers");
        groupOfflineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOfflineUsersList");

        //        UI Hooks
        codeScannerView = findViewById(R.id.scanner_view);
        scannedCode = findViewById(R.id.scanned_value);

        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getText().contains("Salt") && result.getText().contains("Data") && result.getText().contains("Password")) {
                            scannedCode.setTextColor(getColor(R.color.success_400));
                            scannedCode.setText("Adding User Vitals Record");

                            EncryptDecryptData encryptDecryptData = new EncryptDecryptData();

                            Map<String, Object> vitals = encryptDecryptData.decryptVitals(result.getText());

                            Log.i("Vitals  ", vitals.toString());

                            Long time = Timestamp.now().getSeconds();

                            OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiUId, (String) vitals.get("raspiId"), groupId, Double.valueOf((String) vitals.get("hb")), Double.valueOf((String) vitals.get("o2")), Double.valueOf((String) vitals.get("temp")), Integer.valueOf((String) vitals.get("cough_value")), time, "Analysis Result");

                            VitalsSQLiteHelper vitalsSQLiteHelper = new VitalsSQLiteHelper(context);

                            vitalsSQLiteHelper.addOneVitalsEntryOnline(onlineUserVitalsModel);

                            Intent intent = new Intent(OnlineVitalsScanQrActivity.this, OnlineUserVitalsInfoActivity.class);
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
                            finish();
                        } else {
                            scannedCode.setTextColor(getColor(R.color.error_900));
                            scannedCode.setText("Scan Valid QR Please");
                        }
                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(OnlineVitalsScanQrActivity.this, OnlineUserVitalsInfoActivity.class);
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
        finish();
    }
}