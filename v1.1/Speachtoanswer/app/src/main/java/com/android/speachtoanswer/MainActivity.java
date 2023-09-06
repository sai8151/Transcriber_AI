package com.android.speachtoanswer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
ImageButton imgBtn;
TextView tv;
    EditText et;
    ArrayList<String> data;
    SpeechRecognizer sp;

    Intent spi;
    String plamit;
    ProgressBar pb;
int count=0;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgBtn=findViewById(R.id.imageButton);
        tv=findViewById(R.id.textView);
        pb=findViewById(R.id.progressBar);
        et=findViewById(R.id.et);
        sp=SpeechRecognizer.createSpeechRecognizer(this);
        spi=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        imgBtn.setOnClickListener(this);
        pb.setAlpha(0);

        sp.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                tv.setText("\nready to listen\n");
            }

            @Override
            public void onBeginningOfSpeech() {
                tv.setText("\nlistening\n");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                tv.append("quary started");
            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                tv.append("\nError!\n");
            }

            @Override
            public void onResults(Bundle bundle) {
               data= bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                assert data != null;
                et.setText(data.get(0));
               plamit=et.getText().toString();

               new NetworkTask().execute();

            }

            @Override
            public void onPartialResults(Bundle bundle) {
                tv.append("\npartial results\n");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                tv.append("event initiated");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can start using the speech recognition
            } else {
                // Permission denied, handle this situation (show a message, etc.)
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(imgBtn)) {
            if (count == 0) {
                imgBtn.setImageDrawable(getDrawable(android.R.drawable.ic_input_delete));
                //start

                count = 1;
                sp.startListening(spi);
            } else {
                imgBtn.setImageDrawable(getDrawable(android.R.drawable.stat_notify_call_mute));
                //stop
                count = 0;
                sp.stopListening();

            }
        }
    }
    public class NetworkTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            pb.setAlpha(1);
            pb.setIndeterminate(true);
            String url = "https://generativelanguage.googleapis.com/v1beta2/models/text-bison-001:generateText?key=AIzaSyC9aC8pAlJ6FwGReHJZy7YyhO4ZB68PRVc";
            URL obj = null;
            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) obj.openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonPayload = "{ \"prompt\": { \"text\": \"" +
                        plamit +
                        "\" } }";
                tv.setText(plamit);
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.writeBytes(jsonPayload);
                    wr.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    tv.setText(response.toString());
                    if (response != null) {
                        tv.setText(response);
                    }
                    return response.toString();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                connection.disconnect();
                pb.setIndeterminate(false);
                pb.setAlpha(0);
            }

        }

        @SuppressLint({"SuspiciousIndentation", "SetTextI18n"})
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response.toString() != null) {
                try {
                    JSONArray candidatesArray = new JSONArray(response);
                    if (candidatesArray.length() > 0) {
                        JSONObject candidateObject = candidatesArray.getJSONObject(0);
                        String output = candidateObject.getString("output");
                        String cleanedOutput = output.replaceAll("\\n", "");
                        tv.setText(cleanedOutput);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

