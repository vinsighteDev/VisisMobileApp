package com.example.visisapp;


import android.graphics.Bitmap;

import android.os.AsyncTask;

import android.widget.Button;
import android.widget.TextView;


import com.example.visisapp.ui.color.ColorRecognitionTask;
import com.example.visisapp.ui.object.ObjectRecognitionTask;
import com.example.visisapp.ui.scene.SceneRecognitionTask;
import com.example.visisapp.ui.text.TextRecognitionTask;


import java.io.ByteArrayOutputStream;


// This class contains runs the API's recognition functions
public class Recognizer {
    // Initializing UI text view component
    private TextView textView;
    private Button sendButton;

    // Initializing Main class object
    private MainActivity mainActivity;

    // Class Constructor
    public Recognizer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    // Microsoft Computer Vision API for Text Recognition (2ND CHOICE)
    public void runTextRecognition2(Bitmap bitmap) {
        // Create output stream byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Compress given Bitmap to output stream and keep quality 100%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        // Create an Async task for object recognition
        AsyncTask<byte[], String, String> visionTask = new TextRecognitionTask(mainActivity);
        // Execute Async task and send image to task
        // Request API and display progress dialog in UI at the same time
        visionTask.execute(outputStream.toByteArray());
    }

    // Microsoft Computer Vision API for Scene Recognition (ONLY CHOICE)
    public void runSceneRecognition(Bitmap bitmap) {
        // Create output stream byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Compress given Bitmap to output stream and keep quality 100%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        // Create an Async task for scene recognition
        SceneRecognitionTask<byte[], String, String> visionTask = new SceneRecognitionTask(mainActivity);

        // Execute Async task and send image to task
        // Request API and display progress dialog in UI at the same time
        visionTask.execute(outputStream.toByteArray());

    }

    // Microsoft Computer Vision API for Object Recognition (1ST CHOICE)
    public void runObjectRecognition(Bitmap bitmap) {
        // Create output stream byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Compress given Bitmap to output stream and keep quality 100%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        // Create an Async task for object recognition
        ObjectRecognitionTask<byte[], String, String> visionTask = new ObjectRecognitionTask(mainActivity);

        // Execute Async task and send image to task
        // Request API and display progress dialog in UI at the same time
        visionTask.execute(outputStream.toByteArray());

    }

    // Microsoft Computer Vision API for Color Recognition (ONLY CHOICE)
    public void runColorRecognition(Bitmap bitmap) {
        // Create output stream byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Compress given Bitmap to output stream and keep quality 100%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        // Create an Async task for color recognition
        ColorRecognitionTask<byte[], String, String> visionTask = new ColorRecognitionTask(mainActivity);

        // Execute Async task and send image to task
        // Request API and display progress dialog in UI at the same time
        visionTask.execute(outputStream.toByteArray());

    }
}
