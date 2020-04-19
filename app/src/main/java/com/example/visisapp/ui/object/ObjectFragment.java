package com.example.visisapp.ui.object;

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

public class ObjectFragment extends Fragment {

    private ObjectViewModel objectViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        objectViewModel =
                ViewModelProviders.of(this).get(ObjectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_object, container, false);
        //final TextView textView = root.findViewById(R.id.text_object);
        objectViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });
        return root;
    }
}