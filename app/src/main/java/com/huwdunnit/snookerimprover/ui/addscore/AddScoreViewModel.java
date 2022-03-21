package com.huwdunnit.snookerimprover.ui.addscore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddScoreViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddScoreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Add Score fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}