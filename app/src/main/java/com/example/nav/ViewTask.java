package com.example.nav;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewTask extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    TaskRetrieve userTasks;
    private String userid;
    final static int RQS_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        userTasks = new TaskRetrieve();

        listView = findViewById(R.id.agenda);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Task");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, R.layout.view_task_info, R.id.viewtaskinfo, list);

        //data using username
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userid = user.getUid();
        Query specific_user = databaseReference.child(user.getEmail().substring(0,user.getEmail().indexOf("@")));

        //data using userid
        /*Query specific_user = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());*/

        specific_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userTasks = ds.getValue(TaskRetrieve.class);
                    list.add(userTasks.getTask());
                    list.add(String.valueOf(userTasks.getDate()));
                    list.add(String.valueOf(userTasks.getTime()));
                    list.add("----------------------------------------------------------------");
                    alarm();

                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Toast.makeText(ViewTask.this, "Failed to retrieve data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void alarm() {
        Calendar current = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        Date c = new Date();
        try {
            c = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(userTasks.getDate() + userTasks.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cdt = Calendar.getInstance();
        cdt.setTime(c);

        /*Date currentDate = new Date();
        Date currentTime = Calendar.getInstance().getTime();
        String curDate = new SimpleDateFormat("dd-MMM-yyyy").format(currentDate);

        if (userTasks.getDate().compareTo(curDate) < 0) {
            if((userTasks.getTime().compareTo(currentTime.toString())) <= 0) {
                list.remove(userTasks.getTask());
                list.remove(String.valueOf(userTasks.getDate()));
                list.remove(String.valueOf(userTasks.getTime()));
                list.remove("----------------------------------------------------------------");
            }
        } else {
            setAlarm(cdt);
        }*/

        if (cdt.compareTo(current) < 0) {
            //The set Date/Time already passed
            list.remove(userTasks.getTask());
            list.remove(String.valueOf(userTasks.getDate()));
            list.remove(String.valueOf(userTasks.getTime()));
            list.remove("----------------------------------------------------------------");

        } else {
            //setAlarm(cdt);
        }
    }

    void setAlarm(Calendar targetCal){
        Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }
}