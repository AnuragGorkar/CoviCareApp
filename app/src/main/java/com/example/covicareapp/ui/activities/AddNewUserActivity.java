package com.example.covicareapp.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewUserActivity extends AppCompatActivity {

    // UI Variables
    Dialog dialog;
    MaterialToolbar materialToolbar;
    MaterialButton add_online_users, add_local_users;
    ProgressBar progressBar;
    ImageButton closeDialogueButton;
    TextInputLayout userNameTextInput, userPasswordTextInput;
    MaterialButton addNewUserButton, scanQRToAddNewUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        // UI Hooks
        materialToolbar = findViewById(R.id.toolbar);
        add_online_users = findViewById(R.id.add_online_users);
        add_local_users = findViewById(R.id.add_local_users);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewUserActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        add_online_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddOnlineUserDialogue();
            }
        });

        add_local_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewUserActivity.this, AddLocalUsersInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    public void showAddOnlineUserDialogue() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_add_new_user_login_dialogue_box);

        // UI Hooks
        closeDialogueButton = dialog.findViewById(R.id.close_dialogue);
        userNameTextInput = dialog.findViewById(R.id.add_email);
        userPasswordTextInput = dialog.findViewById(R.id.add_password);
        addNewUserButton = dialog.findViewById(R.id.add_new_online_user);
        progressBar = dialog.findViewById(R.id.progress_indicator);
        scanQRToAddNewUserButton = dialog.findViewById(R.id.scan_qr_add_user);

        progressBar.setVisibility(View.GONE);

        addNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (validateInputFields()) {
//                } else {
//                }
            }
        });

        closeDialogueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.70);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_toolbar_background);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AddNewUserActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}