package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.covicareapp.R;
import com.example.covicareapp.helpers.VitalsSQLiteHelper;
import com.example.covicareapp.models.LocalUserModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;

public class AddLocalUsersInfoActivity extends AppCompatActivity {

    //    Data Variables
    String groupId, groupName, groupDateCreated, groupDescription, groupOfflineUsers, groupOnlineUsers;
    String fullName, gender, dateOfBirth, localUserId;
    ArrayList<String> groupOnlineUsersList = new ArrayList<String>();
    ArrayList<String> groupOfflineUsersList = new ArrayList<String>();

    // UI Variables
    TextInputLayout fullNameTextInput;
    ProgressBar progressBar;
    MaterialToolbar materialToolbar;
    RadioGroup gender_radio_group;
    RadioButton selected_gender;
    DatePicker datePickerDob;
    MaterialButton addLocalUser;
    private int day, month, year;

    // function to generate a random string of length n
    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_local_users_info);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        groupDateCreated = intent.getStringExtra("groupDateCreated");
        groupDescription = intent.getStringExtra("groupDescription");
        groupOnlineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOnlineUsersList");
        groupOnlineUsers = intent.getStringExtra("groupOnlineUsers");
        groupOfflineUsers = intent.getStringExtra("groupOfflineUsers");
        groupOfflineUsersList = (ArrayList<String>) intent.getSerializableExtra("groupOfflineUsersList");

        // UI Hooks
        fullNameTextInput = findViewById(R.id.local_user_full_name);
        materialToolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progress_indicator);
        gender_radio_group = findViewById(R.id.radio_group_gender);
        datePickerDob = findViewById(R.id.dob_date_picker);
        addLocalUser = findViewById(R.id.add_new_local_user);

        progressBar.setVisibility(View.GONE);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddLocalUsersInfoActivity.this, AddNewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        addLocalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLocalUser.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                //                Get gender
                selected_gender = findViewById(gender_radio_group.getCheckedRadioButtonId());
                gender = selected_gender.getText().toString().trim();

                //                Get Date of Birth
                day = datePickerDob.getDayOfMonth();
                month = datePickerDob.getMonth();
                year = datePickerDob.getYear();

                dateOfBirth = getDateofBirthFormattted(day, month, year) + " 00:00:00";

                if (validateInput(view)) {
                    VitalsSQLiteHelper vitalsSQLiteHelper = new VitalsSQLiteHelper(AddLocalUsersInfoActivity.this);

                    ArrayList<String> allOfflineUsersIdsList = vitalsSQLiteHelper.getExistingLocalUsersIdList();

                    do {
                        localUserId = "L" + getAlphaNumericString(6);
                    } while (allOfflineUsersIdsList.contains(localUserId));

                    LocalUserModel offlineUserModel = new LocalUserModel(localUserId, fullName, gender, new Timestamp(java.sql.Timestamp.valueOf(dateOfBirth)));
                    Log.i("New Offline User Data: ", offlineUserModel.toString());

                    if (vitalsSQLiteHelper.addUserLocal(groupId, offlineUserModel)) {
                        addLocalUser.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        addLocalUser.setBackgroundColor(getColor(R.color.success_400));
                        addLocalUser.setText("User Added Successfully");

                        Log.i("All Added Users ID List: ", vitalsSQLiteHelper.getExistingLocalUsersIdList().toString());

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                Intent intent = new Intent(AddLocalUsersInfoActivity.this, GroupAddedInfoActivity.class);
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
                        }, 200);

                    } else {
                        addLocalUser.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        showSnackbar(view, "Error while adding new offline user", "", "Error");
                    }
                }


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

    private boolean validateInput(View view) {
        return !(!validateFullName() | !validateGender(view) | !validateAge(view));
    }

    private boolean validateFullName() {
        String fullNameVal = fullNameTextInput.getEditText().getText().toString().trim();
        if (fullNameVal.isEmpty()) {
            fullNameTextInput.setError("Full Name cannot be empty");
            return false;
        } else {
            fullNameTextInput.setError(null);
            fullNameTextInput.setErrorEnabled(false);
            fullName = fullNameVal;
            return true;
        }
    }

    private boolean validateGender(View view) {
        if (gender_radio_group.getCheckedRadioButtonId() == -1) {
            showSnackbar(view, "Please Select Gender", "", "Warning");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge(View view) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = currentYear - datePickerDob.getYear();

        if (userAge < 5) {
            showSnackbar(view, "Your age must be more than 5", "", "Warning");
            return false;
        } else {
            return true;
        }
    }

    String getDateofBirthFormattted(int day, int month, int year) {
        String dayStr, monthStr;
        month = month + 1;
        if (day < 10)
            dayStr = "0" + day;
        else
            dayStr = "" + day;
        if (month < 10)
            monthStr = "0" + month;
        else
            monthStr = "" + month;

        return year + "-" + monthStr + "-" + dayStr;
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
                snackbar.setBackgroundTint(ContextCompat.getColor(AddLocalUsersInfoActivity.this, R.color.success_800));
                break;
            case "Warning":
                snackbar.setBackgroundTint(ContextCompat.getColor(AddLocalUsersInfoActivity.this, R.color.warning_900));
                break;
            case "Error":
                snackbar.setBackgroundTint(ContextCompat.getColor(AddLocalUsersInfoActivity.this, R.color.error_400));
                break;
            default:
                snackbar.setBackgroundTint(ContextCompat.getColor(AddLocalUsersInfoActivity.this, R.color.information_800));
        }
        // set the action button text color of the snackbar however this is optional
        snackbar.setTextColor(ContextCompat.getColor(AddLocalUsersInfoActivity.this, R.color.white_50));
        snackbar.setActionTextColor(ContextCompat.getColor(AddLocalUsersInfoActivity.this, R.color.white_50));

        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // as all the snackbar wont have the action button
        snackbar.show();

    }
}