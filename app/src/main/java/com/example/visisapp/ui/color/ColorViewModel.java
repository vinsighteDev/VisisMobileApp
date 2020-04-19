package com.example.visisapp.ui.color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ColorViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public ColorViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is color fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
