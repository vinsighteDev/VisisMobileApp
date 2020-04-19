package com.example.visisapp.ui.scene;

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


public class SceneFragment extends Fragment {

    private SceneViewModel sceneViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sceneViewModel =
                ViewModelProviders.of(this).get(SceneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scene, container, false);
        //final TextView textView = root.findViewById(R.id.text_scene);
        sceneViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });
        return root;
    }
}