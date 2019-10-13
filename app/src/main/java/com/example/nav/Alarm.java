package com.example.nav;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Alarm extends AppCompatActivity {

    TextView task;
    Button cancelbtn, snoozebtn;
    final static int RQS_1 = 1;
    TaskRetrieve userTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        cancelbtn = findViewById(R.id.cancelbtn);
        snoozebtn = findViewById(R.id.snooozebtn);
        task = findViewById(R.id.task);

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                finish();
            }
        });

        snoozebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 5*60*1000, pendingIntent);
                Toast.makeText(Alarm.this, "Busy?? Will Ping You After 5 mins...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}