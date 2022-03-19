package com.example.covicareapp.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.regex.Pattern;

public class ReenterDataActivity extends AppCompatActivity {
    //    Variables
    String email, password, fullName, gender, countryCode, dateOfBirth, countryName, phoneNumber;
    int day, month, year;

    //    UI Variables
    ProgressBar progressBar;
    TextInputLayout fullNameTextInput, phoneNumberTextInput;
    RadioGroup gender_radio_group;
    RadioButton selected_gender;
    DatePicker datePickerDob;
    com.hbb20.CountryCodePicker country_code;
    MaterialButton signUpButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reenter_data);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        //        UI Hooks
        fullNameTextInput = findViewById(R.id.signup_user_full_name);
        phoneNumberTextInput = findViewById(R.id.signup_user_phone_number);
        country_code = findViewById(R.id.country_code_picker);
        gender_radio_group = findViewById(R.id.radio_group_gender);
        datePickerDob = findViewById(R.id.dob_date_picker);
        progressBar = findViewById(R.id.progress_indicator);
        gender_radio_group = findViewById(R.id.radio_group_gender);
        signUpButton = findViewById(R.id.signup_btn);

        progressBar.setVisibility(View.GONE);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Get gender
                selected_gender = findViewById(gender_radio_group.getCheckedRadioButtonId());
                gender = selected_gender.getText().toString().trim();

                //                Get Date of Birth
                day = datePickerDob.getDayOfMonth();
                month = datePickerDob.getMonth();
                year = datePickerDob.getYear();

                dateOfBirth = getDateofBirthFormattted(day, month, year);

                countryName = country_code.getSelectedCountryName();
                countryCode = country_code.getSelectedCountryCode();

                if(validateInput()){

                    signUpButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    Intent intent = new Intent(ReenterDataActivity.this, VerifyEmailActivity.class);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("gender", gender);
                    intent.putExtra("dateOfBirth", dateOfBirth);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("countryName", countryName);
                    intent.putExtra("countryCode", countryCode);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ReenterDataActivity.this, VerifyEmailActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean validateInput() {
        return !(!validateAge() | !validateFullName() | !validatePhoneNumber());
    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = currentYear - datePickerDob.getYear();

        if(userAge < 14){
            showSnackbar("Your age must be more than 13", "", "Warning");
            return false;
        }else{
            return true;
        }
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

    private boolean validatePhoneNumber(){
        String phoneNumberVal = phoneNumberTextInput.getEditText().getText().toString().trim();
        if(phoneNumberVal.isEmpty()){
            phoneNumberTextInput.setError("Phone number cannot be empty");
            return false;
        }else{
            phoneNumberTextInput.setError(null);
            phoneNumberTextInput.setErrorEnabled(false);
            String phoneNumberRegexPattern = "(0/91)?[7-9][0-9]{9}";
            Pattern p = Pattern.compile(phoneNumberRegexPattern);
            if (!p.matcher(phoneNumberVal).matches()) {
                phoneNumberTextInput.setError("Phone number format is invalid");
                return false;
            } else {
                phoneNumberTextInput.setError(null);
                phoneNumberTextInput.setErrorEnabled(false);
                phoneNumber = phoneNumberVal;
                return true;
            }
        }
    }

    String getDateofBirthFormattted(int day, int month, int year){
        String dayStr, monthStr;
        month = month + 1;
        if(day < 10)
            dayStr = "0" + day;
        else
            dayStr = "" + day;
        if(month < 10)
            monthStr = "0" + month;
        else
            monthStr = "" + month;

        return dayStr + "/" + monthStr + "/" + year;
    }

    protected void showSnackbar(String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(findViewById(R.id.login_activity).getRootView(), messageStr, Snackbar.LENGTH_LONG);

        // the duration is in terms of milliseconds
        snackbar.setDuration(3600);
        // set the background tint color for the snackbar
        switch (resultState){
            case "Success":
                snackbar.setBackgroundTint(getColor(R.color.success_800));
                break;
            case "Warning":
                snackbar.setBackgroundTint(getColor(R.color.warning_900));
                break;
            case "Error":
                snackbar.setBackgroundTint(getColor(R.color.error_400));
                break;
            default:
                snackbar.setBackgroundTint(getColor(R.color.information_800));
        }
        // set the action button text color of the snackbar however this is optional
        snackbar.setTextColor(getColor(R.color.white_50));
        snackbar.setActionTextColor(getColor(R.color.white_50));

        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // as all the snackbar wont have the action button
        snackbar.show();

    }
}