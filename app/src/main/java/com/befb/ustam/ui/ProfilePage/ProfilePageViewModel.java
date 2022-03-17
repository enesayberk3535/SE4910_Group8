package com.befb.ustam.ui.ProfilePage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfilePageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfilePageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ProfilePage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}