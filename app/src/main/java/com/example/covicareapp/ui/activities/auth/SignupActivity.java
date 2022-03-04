package com.example.covicareapp.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.covicareapp.R;
import com.example.covicareapp.ui.fragments.auth.ContactDetailsFragment;
import com.example.covicareapp.ui.fragments.auth.PersonalDetailsFragment;
import com.example.covicareapp.ui.fragments.auth.SigninDetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity
        implements  SigninDetailsFragment.SignInDataInterface,
                    PersonalDetailsFragment.PersonalDataInterface,
                    ContactDetailsFragment.ContactDataInterface{

    //    Signin Data Variables
    String fullNameVal = "", emailVal = "", passwordVal  = "", genderVal, dateOfBirthVal, countryCodeVal, countryNameVal, phoneNumberVal;

    // Get FrameLayout for fragments
    FrameLayout fragmentContainer;
    // Get Fragment Manager
    FragmentManager fragmentManager = getSupportFragmentManager();

    // Define Current Fragment Name
    String current_fragment = "Signin Details";

    // Firebase Variable
    FirebaseAuth firebaseAuth;

    //UI Variables
    MaterialButton returnLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //        Get Current Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();

        //        Fragment References
        SigninDetailsFragment signinDetailsFragment = SigninDetailsFragment.newInstance();
        PersonalDetailsFragment personalDetailsFragment = (PersonalDetailsFragment) fragmentManager.findFragmentById(R.id.personal_details_fragment);
        ContactDetailsFragment contactDetailsFragment = (ContactDetailsFragment) fragmentManager.findFragmentById(R.id.contact_details_fragment);

        fragmentContainer = findViewById(R.id.fragment_container);
        returnLoginButton = findViewById(R.id.return_login_btn);

        returnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (current_fragment){
            case "Signin Details":{
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
            break;
            case "Personal Details":{
                switchFragment("Signin Details", SigninDetailsFragment.class);
            }
            break;
            case "Contact Details":{
                switchFragment("Personal Details", PersonalDetailsFragment.class);
            }
        }
    }

    protected void switchFragment( String fragmentName, Class<? extends androidx.fragment.app.Fragment> fragmentClass){
        current_fragment = fragmentName;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.fragment_container, fragmentClass, null)
                .setReorderingAllowed(true)
                .addToBackStack(null) // name can be null
                .commit();
    }

    @Override
    public void sendSignInData(String fullName, String email, String password) {
        fullNameVal = fullName;
        emailVal = email;
        passwordVal = password;

        switchFragment("Personal Details", PersonalDetailsFragment.class);
    }

    @Override
    public void sendPersonalData(String gender, String dateOfBirth) {
        genderVal = gender;
        dateOfBirthVal = dateOfBirth;
        switchFragment("Contact Details", ContactDetailsFragment.class);
    }

    @Override
    public void sendContactData(String countryName, String countryCode, String phoneNumber) {
        countryNameVal = countryName;
        countryCodeVal = countryCode;
        phoneNumberVal = phoneNumber;

        createUser(emailVal, passwordVal);
    }

    public void createUser(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignupActivity.this, VerifyEmailActivity.class);
                    intent.putExtra("fullName", fullNameVal);
                    intent.putExtra("email", emailVal);
                    intent.putExtra("password", passwordVal);
                    intent.putExtra("gender", genderVal);
                    intent.putExtra("dateOfBirth", dateOfBirthVal);
                    intent.putExtra("phoneNumber", phoneNumberVal);
                    intent.putExtra("countryName", countryNameVal);
                    intent.putExtra("countryCode", countryCodeVal);

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else{
                    showSnackbar(task.getException().getMessage().trim(), "", "Error");
                }
            }
        });
    }

    protected void showSnackbar(String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(findViewById(R.id.signup_activity).getRootView(), messageStr, Snackbar.LENGTH_LONG);

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