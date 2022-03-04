package com.example.covicareapp.ui.fragments.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.util.regex.Pattern;

public class ContactDetailsFragment extends Fragment {
    //    Data Variables
    String countryCode, countryName, phoneNumber;

    //    UI Variables
    MaterialButton signupButton;
    ProgressBar progressBar;
    TextInputLayout phoneNumberTextInput;
    com.hbb20.CountryCodePicker country_code;

    ContactDataInterface contactDataInterface;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    public static ContactDetailsFragment newInstance() {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);

        //        UI Hooks
        signupButton = view.findViewById(R.id.signup_btn);
        phoneNumberTextInput = view.findViewById(R.id.signup_user_phone_number);
        country_code = view.findViewById(R.id.country_code_picker);
        progressBar = view.findViewById(R.id.progress_indicator);

        progressBar.setVisibility(View.GONE);

        countryName = country_code.getSelectedCountryName();
        countryCode = country_code.getSelectedCountryCode();

        country_code.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryName = country_code.getSelectedCountryName();
                countryCode = country_code.getSelectedCountryCode();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatePhoneNumber()){
                    signupButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    contactDataInterface.sendContactData(countryName, countryCode, phoneNumber);
                }
            }
        });

        return view;
    }

    public interface ContactDataInterface{
        void sendContactData(String countryName, String countryCode, String phoneNumber);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity  = (Activity) context;

        try {
            contactDataInterface = (ContactDetailsFragment.ContactDataInterface) activity;
        }catch (RuntimeException e){
            throw new  RuntimeException(activity.toString() + "Implement Method");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contactDataInterface = null;
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