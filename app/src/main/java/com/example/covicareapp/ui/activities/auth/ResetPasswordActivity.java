package com.example.covicareapp.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;

import com.example.covicareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ProgressBar;

import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {
    //      Variables
    String email;

    //      UI Variables
    ProgressBar progressBar;
    TextInputLayout emailTextInput;
    MaterialButton resetPasswordBtn, returnLoginButton;

    //    Firebase Variable
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // UI Hooks
        emailTextInput = findViewById(R.id.user_reset_email);
        resetPasswordBtn = findViewById(R.id.reset_pswd_btn);
        progressBar = findViewById(R.id.progress_indicator);
        returnLoginButton = findViewById(R.id.return_login_btn);

        progressBar.setVisibility(View.GONE);

        //        Firebase Auth Instance
        firebaseAuth = FirebaseAuth.getInstance();

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmail()){
                    resetPasswordBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                resetPasswordBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                showSnackbar("Reset password e-mail sent to " + email, "", "Success");
                            } else {
                                resetPasswordBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                showSnackbar("Error " + task.getException().getMessage().toString(), "", "Error");
                            }
                        }
                    });
                }
            }
        });

        returnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean validateEmail() {
        String emailVal = emailTextInput.getEditText().getText().toString().trim();
        if (emailVal.isEmpty()) {
            emailTextInput.setError("Email cannot be empty");
            return false;
        } else {
            emailTextInput.setError(null);
            emailTextInput.setErrorEnabled(false);
            String emailRegexPattern = "^(.+)@(.+)$";
            Pattern p = Pattern.compile(emailRegexPattern);
            if (!p.matcher(emailVal).matches()) {
                emailTextInput.setError("Email format is invalid");
                return false;
            } else {
                emailTextInput.setError(null);
                emailTextInput.setErrorEnabled(false);
                email = emailVal;
                return true;
            }
        }
    }

    protected void showSnackbar(String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(findViewById(R.id.reset_password_activity).getRootView(), messageStr, Snackbar.LENGTH_LONG);

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