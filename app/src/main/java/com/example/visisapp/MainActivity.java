package com.example.visisapp;

import android.graphics.Bitmap;

import android.os.Bundle;

import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.Switch;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GestureDetectorCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CameraKitEventListener, TextToSpeech.OnInitListener {
    // UI Components
    private CameraView cameraView;
    private Button cameraButton;
    private BottomNavigationView bottomNavigationView;
    private Switch flashSwitch;
    private GestureDetectorCompat mDetector;

    // Text to Speech Element
    private TextToSpeech textToSpeech;
    // Recognizer Class Object
    private Recognizer recognizer;

    private SpeechRecognizer mySpeechRecognizer;
    private static final String DEBUG_TAG = "Gestures";
    private boolean speech_recog = false;
    private boolean voice_text_recog = false;
    private boolean voice_scene_recog = false;
    private boolean voice_object_recog = false;
    private boolean voice_color_recog = false;

    // When the Activity is Created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initRecognitionElements();
    }

    // Initialize Main Activity View, Navigation Bar,send button, and Camera.
    private void initUI() {
        setContentView(R.layout.bottom_nav);

        flashSwitch = findViewById(R.id.flashSwitch);

        flashSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                speak("flashlight is on");
            } else {
                speak("flashlight is off");
            }
        });


        initNav();
        initCamera();
    }

    // Initialize Navigation Bar
    private void initNav() {
        bottomNavigationView = findViewById(R.id.bottomBar);
        // Identifying each menu choice
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_text)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                speak(item.toString());
                return true;
            }
        });

    }

    // Initialize Camera and Button. When button is pressed logic
    private void initCamera() {
        cameraButton = findViewById(R.id.cameraBtn);
        cameraView = findViewById(R.id.camView);


        cameraButton.setOnClickListener(new View.OnClickListener() {
            // When button is pressed, image is capture.
            // It also stops text to speech if available
            // Removes send button if needed.
            // Change camera button's text
            // if color recognition is selected, set flash to on
            @Override
            public void onClick(View v) {
                // check if speech still there, if it is stop it
                if (textToSpeech != null) {
                    textToSpeech.stop();
                }


                cameraView.start();

                // if button is stop, change text
                if (cameraButton.getText().equals("Stop Scan")) {
                    speak("Stopping text scan");
                    cameraButton.setText("Scan Text");
                    //speechButton.setVisibility(View.VISIBLE);
                } else // else change it to stop
                {
                    speak("Scanning");
                    cameraButton.setText("Stop Scan");
                }

                if (flashSwitch.isChecked()) {
                    // Turn flash ON
                    cameraView.setFlash(CameraKit.Constants.FLASH_ON);
                } else {
                    // Turn flash OFF
                    cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
                }
                // Capture picture
                cameraView.captureImage();
            }
        });

        cameraButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                textToSpeech.stop();
                return true;
            }
        });
    }

    // Initialize Recognition Elements: text to speech, recognizers (APIs) and Camera Listener
    private void initRecognitionElements() {
        initTextToSpeech();
        recognizer = new Recognizer(this);
        cameraView.addCameraKitListener(this);
    }

    // Initialize Text to Speech
    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(this, this);
    }


    // On Image taken, Convert it to a bitmap and run the corresponding API
    @Override
    public void onImage(CameraKitImage cameraKitImage) {
        // Create bitmap of image taken
        Bitmap bitmap = cameraKitImage.getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);

        // Get selected Menu Option ID
        int number = bottomNavigationView.getSelectedItemId();

        // Depending on selected option, we run the corresponding API
        if (!speech_recog) {
            // Stop camera preview while recognition is happening
            cameraView.stop();

            // if item selected is text, then run text recognition
            if (number == R.id.navigation_text) {
                recognizer.runTextRecognition2(bitmap);
            }
            if (number == R.id.navigation_scene) {
                recognizer.runSceneRecognition(bitmap);
            }
            if (number == R.id.navigation_object) {
                recognizer.runObjectRecognition(bitmap);
            }
            if (number == R.id.navigation_color) {
                recognizer.runColorRecognition(bitmap);
            }
        } else {
            if (voice_text_recog) {
                recognizer.runTextRecognition2(bitmap);
                voice_text_recog = false;
            }
            if (voice_scene_recog) {
                recognizer.runSceneRecognition(bitmap);
                voice_text_recog = false;
            }
            if (voice_object_recog) {
                recognizer.runObjectRecognition(bitmap);
                voice_text_recog = false;
            }
            if (voice_color_recog) {
                recognizer.runColorRecognition(bitmap);
                voice_text_recog = false;
            }
            speech_recog = false;
        }
    }

    // Initialize Text to Speech
    @Override
    public void onInit(int status) {
        // Set Text to Speech Language
        int lanResult = textToSpeech.setLanguage(Locale.US);

        // Check if language is supported
        if (lanResult == TextToSpeech.LANG_MISSING_DATA || lanResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("Language", "This Language is not supported");
        } else {
            Log.i("Language", "Language set to english");
        }

        // Check if Initialization Status is good
        if (status == TextToSpeech.ERROR) {
            Log.e("Initialization", "Text to speech initialization Failed!");
        } else {
            Log.i("Initialization", "Text to speech was initialized.");
        }
        speak("Welcome to the visis app! I am your virtual assistant, and i can help you recognize texts. " +
                " tap on the screen once to activate text scan and tap on the screen the second time to deactivate text scan");

    }

    // Text to Speech: Receives a String, converts it to speech
    public void speak(String text) {
        if (textToSpeech == null || "".equals(text)) {
            assert textToSpeech != null;
            textToSpeech.speak("Please try again", TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        }
    }


    // When Activity is resumed, start the camera, change button text
    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
        cameraButton.setText("Tap to scan");
    }

    // When Activity is Paused, stop the camera and text to speech, hide send button
    @Override
    public void onPause() {
        cameraView.stop();
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onPause();
    }

    @Override
    public void onEvent(CameraKitEvent cameraKitEvent) {
    }

    @Override
    public void onError(CameraKitError cameraKitError) {
    }

    @Override
    public void onVideo(CameraKitVideo cameraKitVideo) {
    }
}
