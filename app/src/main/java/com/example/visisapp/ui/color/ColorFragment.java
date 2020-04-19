package com.example.visisapp.ui.color;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.visisapp.R;


public class ColorFragment extends Fragment
{
    private ColorViewModel colorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        colorViewModel = ViewModelProviders.of(this).get(ColorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_color, container, false);
        //final TextView textView = root.findViewById(R.id.text_color);

        colorViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });
        return root;
    }
}
