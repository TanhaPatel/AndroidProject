package com.example.nav;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class TTSandSTT extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private ImageButton mSpeakBtn;
    private TextToSpeech textToSpeech;
    private Button btn;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //speech2text started

        mVoiceInputTv = findViewById(R.id.voiceInput);
        mSpeakBtn = findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v) {
                startVoiceInput();
            }
        });

        //speech2text ended


        //text2speech started

        btn = findViewById(R.id.speechbtn);
        editText = findViewById(R.id.speechEditTxt);
        textToSpeech = new android.speech.tts.TextToSpeech(getApplicationContext(), new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == android.speech.tts.TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String data = editText.getText().toString();
                Log.i("TTS", "button clicked: " + data);
                int speechStatus = textToSpeech.speak(data, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null);

                if (speechStatus == android.speech.tts.TextToSpeech.ERROR) {
                    Log.e("TTS", "Error in converting Text to Speech!");
                }
            }

        });

        //text2speech ended

    }

    //speech2text started

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
            { if (resultCode == RESULT_OK && null != data)
            { ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mVoiceInputTv.setText(result.get(0));
            }
                break; }
        }
    }

    //speech2text ended


    //text2speech function

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    //text2speech function ended

}
