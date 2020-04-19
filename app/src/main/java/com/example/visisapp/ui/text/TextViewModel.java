package com.example.visisapp.ui.text;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TextViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TextViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is text fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}