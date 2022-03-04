package com.example.covicareapp.ui.fragments.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

public class SigninDetailsFragment extends Fragment {
    //    Data Variables
    String fullName, email, password;

    //    UI Variables
    ProgressBar progressBar;
    TextInputLayout fullNameTextInput, emailTextInput, passwordTextInput, confirmPasswordTextInput;
    MaterialButton goToPersonalDetailsButton;

    SignInDataInterface sigInDataInterface;

    // Firebase Variable
    FirebaseAuth firebaseAuth;

    public SigninDetailsFragment() {
        // Required empty public constructor
    }

    public static SigninDetailsFragment newInstance() {
        SigninDetailsFragment fragment = new SigninDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //        Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin_details, container, false);

        //        UI Hooks
        fullNameTextInput = view.findViewById(R.id.signup_user_full_name);
        emailTextInput = view.findViewById(R.id.signup_user_email);
        passwordTextInput = view.findViewById(R.id.signup_user_password);
        confirmPasswordTextInput = view.findViewById(R.id.signup_user_confirm_password);
        goToPersonalDetailsButton = view.findViewById(R.id.go_to_personal_details_btn);
        progressBar = view.findViewById(R.id.progress_indicator);

        progressBar.setVisibility(View.GONE);

        //        Get Current Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();

        goToPersonalDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateSigninDetails()){
                    progressBar.setVisibility(View.VISIBLE);
                    goToPersonalDetailsButton.setVisibility(View.GONE);
                    firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                goToPersonalDetailsButton.setVisibility(View.VISIBLE);

                                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                if(isNewUser)
                                    sigInDataInterface.sendSignInData(fullName, email, password);
                                else{
                                    emailTextInput.setError("E-mail already registered.");
                                    showSnackbar(view, "Error! User e-mail already registered.  Use another e-mail id.", "", "Error");
                                }
                            }else{
                                progressBar.setVisibility(View.GONE);
                                goToPersonalDetailsButton.setVisibility(View.VISIBLE);

                                showSnackbar(view, "Error! " + task.getException().toString(), "", "Error");
                            }
                        }
                    });


                }
            }
        });

        return view;
    }
    public interface SignInDataInterface{
        void sendSignInData(String fullName, String email, String password);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity  = (Activity) context;

        try {
            sigInDataInterface = (SignInDataInterface) activity;
        }catch (RuntimeException e){
            throw new  RuntimeException(activity.toString() + "Implement Method");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sigInDataInterface = null;
    }

    //        Validate Functions
    public boolean validateSigninDetails(){
        return !(!validateFullName() | !validatePassword() | !validateEmail() | !validateConfirmPassword());
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

    private boolean validateConfirmPassword() {
        String confirmPasswordVal = confirmPasswordTextInput.getEditText().getText().toString().trim();
        String passwordVal = passwordTextInput.getEditText().getText().toString().trim();
        if (confirmPasswordVal.isEmpty()) {
            confirmPasswordTextInput.setError("Confirm Password cannot be empty");
            return false;
        } else {
            confirmPasswordTextInput.setError(null);
            confirmPasswordTextInput.setErrorEnabled(false);
            Log.i(passwordVal, confirmPasswordVal);
            if (!passwordVal.equals(confirmPasswordVal)) {
                confirmPasswordTextInput.setError("Password does not match");
                return false;
            } else {
                confirmPasswordTextInput.setError(null);
                confirmPasswordTextInput.setErrorEnabled(false);
                password = passwordVal;
                return true;
            }
        }
    }

    protected void showSnackbar(View view, String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(view, messageStr, Snackbar.LENGTH_LONG);

        // the duration is in terms of milliseconds
        snackbar.setDuration(3600);
        // set the background tint color for the snackbar
        switch (resultState){
            case "Success":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.success_800));
                break;
            case "Warning":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.warning_900));
                break;
            case "Error":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.error_400));
                break;
            default:
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.information_800));
        }
        // set the action button text color of the snackbar however this is optional
        snackbar.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_50));
        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.white_50));

        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // as all the snackbar wont have the action button
        snackbar.show();

    }
}