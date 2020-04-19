package com.example.visisapp.ui.scene;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SceneViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SceneViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is scene fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}