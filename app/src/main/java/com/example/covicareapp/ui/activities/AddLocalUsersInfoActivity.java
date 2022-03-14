package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class AddLocalUsersInfoActivity extends AppCompatActivity {

    // UI Variables
    ProgressBar progressBar;
    MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_local_users_info);

        // UI Hooks
        materialToolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progress_indicator);

        progressBar.setVisibility(View.GONE);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddLocalUsersInfoActivity.this, AddNewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddLocalUsersInfoActivity.this, AddNewUserActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}