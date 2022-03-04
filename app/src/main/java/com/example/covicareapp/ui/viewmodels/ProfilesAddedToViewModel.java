package com.example.covicareapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfilesAddedToViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfilesAddedToViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profiles added to fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}