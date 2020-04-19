package com.example.visisapp.ui.text;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visisapp.BuildConfig;
import com.example.visisapp.MainActivity;
import com.example.visisapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// This class allows you to perform background operations and publish results on the UI thread
// Basically it shows the progress dialog meanwhile text recognition API is recognizing
public class TextRecognitionTask extends AsyncTask<byte[], String, String>
{
    // Main Activity Class Object
    private MainActivity mainActivity;
    // Progress Popup
    private ProgressDialog progressDialog;

    // API keymanife
    private final String API_KEY = BuildConfig.ApiKey;
    TextView textView;

    // Class Constructor
    public TextRecognitionTask(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
        this.progressDialog = new ProgressDialog(mainActivity);
        //textView = mainActivity.findViewById(R.id.txt_result);
    }

    // Invokes the UI thread before the task is executed
    @Override
    protected void onPreExecute()
    {
        // Shows Progress Bar in the UI
        progressDialog.show();
    }

    // Invokes on the background thread immediately after onPreExecute() finishes executing.
    // This function handles sending a HTTP POST request (with the image and the type of request)
    // to the API, then the API sends a response back
    @Override
    protected String doInBackground(byte[]... inputBytes)
    {
        try
        {
            // Publish message in progress dialog
            publishProgress("Recognizing");

            // API Url with API type request for Object recognition
            URL url = new URL("https://eastus.api.cognitive.microsoft.com/vision/v2.0/ocr?language=en");
            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set the request method to POST
            connection.setRequestMethod("POST");
            // Set the request Content-Type Header to application/octet-stream (inputStream image)
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            // Send Authorization Key for API
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);

            // Create a request body (content to send (image))
            // return output stream that write to the specified connection
            OutputStream outputStream = connection.getOutputStream();
            // convert input stream to bytes, and then write them in oputput stream
            outputStream.write(inputBytes[0]);
            // flushes output stream
            outputStream.flush();
            // closes output stream
            outputStream.close();

            // connects to API
            connection.connect();

            // Reading Response from the input stream
            // String buffer will contain the response
            StringBuffer response = new StringBuffer();
            // Gets the status response from the connection
            int responseCode = connection.getResponseCode();

            // Check if the connection code is good
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                // Gets input stream and creates a buffer
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Line to be read
                String inputLine;

                // Read each line in the buffer (input stream) and store it in response
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                // close the buffer/stream
                bufferedReader.close();
            }

            // return response as a string
            return response.toString();

        } catch (IOException e) // catch any errors
        {
            e.printStackTrace();
        }

        return null;
    }

    // Invokes on the UI thread after the background computation finishes
    @Override
    protected void onPostExecute(String s)
    {
        // Final String
        String recognitionText;

        // Check if response String is empty
        if (TextUtils.isEmpty(s))
        {
            // Response string was Empty
            Toast.makeText(mainActivity, "API returned empty result", Toast.LENGTH_SHORT);
        } else
        {
            // Dismiss dialog popup after API response is received
            progressDialog.dismiss();

            // Parse String response to a JSON Object
            JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
            // String Builder to build final String
            StringBuilder stringResult = new StringBuilder();
            // Creates JSON Array for Json objects
            JsonArray jsonRegionArray = jsonObject.getAsJsonArray("regions");

            for (int x = 0; x < jsonRegionArray.size(); x++)
            {
                JsonObject jsonRegion = jsonRegionArray.get(x).getAsJsonObject();

                JsonArray jsonLinesArray = jsonRegion.getAsJsonArray("lines");

                for (int i = 0; i < jsonLinesArray.size(); i++)
                {
                    JsonObject jsonLine = jsonLinesArray.get(i).getAsJsonObject();

                    JsonArray jsonWordsArray = jsonLine.getAsJsonArray("words");

                    for(int j = 0; j < jsonWordsArray.size(); j++)
                    {
                        // Append each value to string builder
                        stringResult.append(jsonWordsArray.get(j).getAsJsonObject().get("text").getAsString() + " ");
                    }
                }
            }

            // if nothing was added to the string builder, then display error
            if (stringResult.length() == 0)
            {
                stringResult.append("Sorry, I'm not sure what the text is");
            }

            // Build final string
            recognitionText = stringResult.toString();

            // Set the text to be the built string
           // textView.setText(recognitionText);

            // Sends text to Text to speech
            mainActivity.speak(recognitionText);
        }
    }

    // Invokes on the UI thread after a call to publishProgress(Progress...).
    @Override
    protected void onProgressUpdate(String... values)
    {
        // Sets message to "Recognizing"
        progressDialog.setMessage(values[0]);
    }
}