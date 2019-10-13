package com.example.nav;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskVoice extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView voiceInput;
    private ImageView addtaskmic;
    Button voicedate, voicetime;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Date datentime;
    String userid, email, task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_voice);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Task");

        voiceInput = findViewById(R.id.voiceInput);
        addtaskmic = findViewById(R.id.addtaskmic);
        addtaskmic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
    }

    //speech2text started

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Use format \'Task\' at \'Date\' on \'Time\'");
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
                voiceInput.setText(result.get(0));
                setalarm();
            }
                break; }
        }
    }

    //speech2text ended

    private void setalarm() {
        Calendar current = Calendar.getInstance();
        Date currenttime = current.getTime();

        String user_task = voiceInput.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        int a = user.getEmail().indexOf("@");
        email = user.getEmail().substring(0,a);

        if (TextUtils.isEmpty(user_task)) {
            Toast.makeText(this, "Please Enter Valid Input", Toast.LENGTH_LONG).show();

        } else {
            /*String id = databaseReference.push().getKey();
            TaskSender ts = new TaskSender(datentime, user_task);
            voiceInput.setText("");
            databaseReference.child(email).child(id).setValue(ts);*/
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_LONG).show();

        }
    }
}
