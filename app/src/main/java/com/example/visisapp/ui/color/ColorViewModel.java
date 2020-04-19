package com.example.visisapp.ui.color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ColorViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public ColorViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}
