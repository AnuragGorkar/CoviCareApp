package com.example.covicareapp.ui.fragments.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class PersonalDetailsFragment extends Fragment {

    //    Data Variables
    String gender, dateOfBirth;
    private int day, month, year;

    //    UI Variables
    MaterialButton goToContactDetailsButton;
    RadioGroup gender_radio_group;
    RadioButton selected_gender;
    DatePicker datePickerDob;

    PersonalDataInterface personalDataInterface;

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    public static PersonalDetailsFragment newInstance() {
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);

        //        UI Hooks
        gender_radio_group = view.findViewById(R.id.radio_group_gender);
        datePickerDob = view.findViewById(R.id.dob_date_picker);
        goToContactDetailsButton = view.findViewById(R.id.go_to_contact_details_btn);

        goToContactDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                //                Get gender
                selected_gender = view.findViewById(gender_radio_group.getCheckedRadioButtonId());
                gender = selected_gender.getText().toString().trim();

                //                Get Date of Birth
                day = datePickerDob.getDayOfMonth();
                month = datePickerDob.getMonth();
                year = datePickerDob.getYear();

                dateOfBirth = getDateofBirthFormattted(day, month, year);

                if(validateInput(view)){
                    personalDataInterface.sendPersonalData(gender, dateOfBirth);
                }
            }
        });

        return view;
    }

    public interface PersonalDataInterface{
        void sendPersonalData(String gender, String dateOfBirth);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity  = (Activity) context;

        try {
            personalDataInterface = (PersonalDetailsFragment.PersonalDataInterface) activity;
        }catch (RuntimeException e){
            throw new  RuntimeException(activity.toString() + "Implement Method");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        personalDataInterface = null;
    }

    private boolean validateInput(View view){
        return !(!validateGender(view) | !validateAge(view));
    }

    private boolean validateGender(View view){
        if(gender_radio_group.getCheckedRadioButtonId() == -1) {
            showSnackbar(view, "Please Select Gender", "", "Warning");
            return false;
        }else{
            return true;
        }
    }

    private boolean validateAge(View view){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = currentYear - datePickerDob.getYear();

        if(userAge < 14){
            showSnackbar(view, "Your age must be more than 13", "", "Warning");
            return false;
        }else{
            return true;
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

        return year + "-" + monthStr + "-" + dayStr;
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