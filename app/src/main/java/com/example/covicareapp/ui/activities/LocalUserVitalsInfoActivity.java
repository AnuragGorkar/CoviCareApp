package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;

public class LocalUserVitalsInfoActivity extends AppCompatActivity {

    // Variables
    long localId;
    String localLUID;

    // UI Variables
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_user_vitals_info);

        Intent intent = getIntent();
        localId = intent.getLongExtra("local_ID", 0);
        localLUID = intent.getStringExtra("local_luid");

        // UI Hooks
        textView = findViewById(R.id.textView);

        textView.setText("Display Local Users Vitals Info from SQLite Here\n" + localLUID);
    }
}