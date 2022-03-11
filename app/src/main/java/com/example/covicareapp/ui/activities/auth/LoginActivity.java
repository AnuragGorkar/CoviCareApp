package com.example.covicareapp.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.example.covicareapp.ui.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    // Delay handler
    final Handler handler = new Handler();

    //    String Variables
    String email, password;

    //    UI Variables
    ProgressBar progressBar;
    Button loginButton, forgotPasswordButton, signUpButton;
    TextInputLayout emailTextInput, passwordTextInput;

    //    Firebase Variable
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // UI Hooks
        loginButton = findViewById(R.id.login_btn);
        forgotPasswordButton = findViewById(R.id.forgot_pass_btn);
        signUpButton = findViewById(R.id.go_signup_btn);
        emailTextInput = findViewById(R.id.user_email);
        progressBar = findViewById(R.id.progress_indicator);
        passwordTextInput = findViewById(R.id.user_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        progressBar.setVisibility(View.GONE);

        Intent intent = getIntent();
        try {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            emailTextInput.getEditText().setText(email);
            passwordTextInput.getEditText().setText(password);
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateInput()){
                    showSnackbar("Input Valid Data", "", "Error");
                    return;
                }else{
                    email = emailTextInput.getEditText().getText().toString().trim();
                    password = passwordTextInput.getEditText().getText().toString().trim();

                    loginButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loginButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                firebaseUser = firebaseAuth.getCurrentUser();
                                if(firebaseUser.isEmailVerified()) {
                                    loginButton.setBackgroundColor(getColor(R.color.success_400));
                                    loginButton.setText("Welcome to CoviCare");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }else{
                                    Snackbar snackbar = Snackbar.make(view, "Email-Id not verified.", Snackbar.LENGTH_LONG);
                                    snackbar.setDuration(3600);
                                    snackbar.setBackgroundTint(getColor(R.color.warning_900));
                                    snackbar.setTextColor(getColor(R.color.white_50));
                                    snackbar.setActionTextColor(getColor(R.color.white_50));

                                    snackbar.setAction("Verify Email", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(LoginActivity.this, ReenterDataActivity.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        }
                                    });
                                    // as all the snackbar wont have the action button
                                    snackbar.show();
                                }
                            }else{
                                showSnackbar("Error! "+ task.getException().getMessage(), "", "Error");
                            }
                        }
                    });
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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
                return true;
            }
        }
    }

    private boolean validatePassword() {
        String passwordVal = passwordTextInput.getEditText().getText().toString().trim();
        if (passwordVal.isEmpty()) {
            passwordTextInput.setError("Password cannot be empty");
            return false;
        } else {
            passwordTextInput.setError(null);
            passwordTextInput.setErrorEnabled(false);
            if (passwordVal.length() < 6) {
                passwordTextInput.setError("Minimum length of password is 6");
                return false;
            } else {
                passwordTextInput.setError(null);
                passwordTextInput.setErrorEnabled(false);
                String passwordRegexPattern = "(^" +
//                        "(?=.*[0-9])" +  // at least 1 digit
//                        "(?=.*[a-z])" + // at least 1 small letter
//                        "(?=.*[A-Z])" + // at least 1 capital letter
                        "(?=.*[a-zA-Z])" + // any letter
                        "(?=.*[@#$%^&+=])" + // at least one special character
                        "(?=\\S+$)" + // no white spaces
                        ".{6,}" + // at least 6 characters
                        "$)";
                Pattern p = Pattern.compile(passwordRegexPattern);
                if (!p.matcher(passwordVal).matches()) {
                    passwordTextInput.setError("Password format is invalid");
                    return false;
                } else {
                    passwordTextInput.setError(null);
                    passwordTextInput.setErrorEnabled(false);
                    return true;
                }
            }
        }
    }

    private boolean validateInput() {
        return !(!validatePassword() | !validateEmail());
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