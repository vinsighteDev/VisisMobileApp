package com.example.visisapp.ui.object;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ObjectViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ObjectViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is object fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}