package com.example.nav;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddTaskTxt extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    EditText addTaskEditText;
    Button addTaskBtn, txtdate, txttime;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String userid, email;
    private FirebaseUser firebaseUser;

    private Context context = this;
    ImageView dateicon, timeicon;
    TextView set_date, set_time;
    Button datedone, timedone;
    final static int RQS_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_txt);

        addTaskEditText = findViewById(R.id.addTaskEditText);
        addTaskBtn = findViewById(R.id.addTaskBtn);
        txtdate = findViewById(R.id.txtdate);
        txttime = findViewById(R.id.txttime);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Task");
        firebaseUser = firebaseAuth.getCurrentUser();

        txtdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                final Dialog datedialog = new Dialog(context);
                datedialog.setCancelable(false);
                datedialog.setContentView(R.layout.custom_date_dialog);
                dateicon = datedialog.findViewById(R.id.dateicon);
                set_date = datedialog.findViewById(R.id.set_date);
                datedone = datedialog.findViewById(R.id.datedone);

                dateicon.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        DialogFragment date = new DatePickerFragment();
                        date.show(AddTaskTxt.this.getSupportFragmentManager(), "Date Picker");
                    }
                });
                datedialog.show();

                datedone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datedialog.dismiss();
                        Toast.makeText(AddTaskTxt.this, "Date added successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        txttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog timedialog = new Dialog(context);
                timedialog.setCancelable(false);
                timedialog.setContentView(R.layout.custom_time_dialog);
                timeicon = timedialog.findViewById(R.id.timeicon);
                set_time = timedialog.findViewById(R.id.set_time);
                timedone = timedialog.findViewById(R.id.timedone);

                timeicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment time = new TimePickerFragment();
                        time.show(getSupportFragmentManager(), "Time Picker");
                    }
                });
                timedialog.show();

                timedone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timedialog.dismiss();
                        Toast.makeText(AddTaskTxt.this, "Time added successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtask();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cdate = Calendar.getInstance();
        cdate.set(Calendar.YEAR,year);
        cdate.set(Calendar.MONTH,month);
        cdate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(cdate.getTime());
        txtdate.setText(currentDateString);
        set_date.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        if (minute < 10) {
            set_time.setText(hour + ":" + "0" + minute);
            txttime.setText(hour + ":" + "0" + minute);
        } else {
            set_time.setText(hour + ":" + minute);
            txttime.setText(hour + ":" + minute);
        }
    }

    //data using username
    public void addtask() {

        Date currentDate = new Date();
        Date currentTime = Calendar.getInstance().getTime();
        String curDate = new SimpleDateFormat("dd-MMM-yyyy").format(currentDate);

        String user_task = addTaskEditText.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        int a = user.getEmail().indexOf("@");
        email = user.getEmail().substring(0, a);

        if (TextUtils.isEmpty(user_task)) {
            Toast.makeText(this, "Please Enter Valid Input", Toast.LENGTH_LONG).show();

        } else if (txtdate.getText().toString().equals("Select Date")) {
            Toast.makeText(this, "Please set appropriate date", Toast.LENGTH_SHORT).show();

        } else if (txttime.getText().toString().equals("Select Time")) {
            Toast.makeText(this, "Please set appropriate time", Toast.LENGTH_SHORT).show();

        } else if (txtdate.getText().toString().compareTo(curDate) < 0) {
            Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show();

            if((txttime.getText().toString().compareTo(currentTime.toString())) <= 0) {
                Toast.makeText(this, "Invalid Time", Toast.LENGTH_SHORT).show();
            }
            
        } else {
            String id = databaseReference.push().getKey();
            TaskSender ts = new TaskSender(user_task, txtdate.getText().toString(), txttime.getText().toString());
            addTaskEditText.setText("");
            databaseReference.child(email).child(id).setValue(ts);
            Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_LONG).show();
            //setAlarm(cal);

        }
    }

    private void setAlarm(Calendar targetCal) {
       Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
       PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
       AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
       alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }
}