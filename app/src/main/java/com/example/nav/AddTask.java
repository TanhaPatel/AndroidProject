package com.example.nav;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddTask extends AppCompatActivity {

    DatePicker pickerDate;
    TimePicker pickerTime;
    TextView info;
    EditText addTaskEditText;
    Button addTaskBtn;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Date datentime;
    String userid, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        addTaskEditText = findViewById(R.id.addTaskEditText);
        addTaskBtn = findViewById(R.id.addTaskBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Task");

        info = findViewById(R.id.info);
        pickerDate = findViewById(R.id.pickerdate);
        pickerTime = findViewById(R.id.pickertime);
        Calendar now = Calendar.getInstance();
        pickerDate.init(
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH),
            null);
        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Calendar cal = Calendar.getInstance();
            cal.set(pickerDate.getYear(),
                    pickerDate.getMonth(),
                    pickerDate.getDayOfMonth(),
                    pickerTime.getCurrentHour(),
                    pickerTime.getCurrentMinute(),
                    00);

            datentime = cal.getTime();
            addtask();
            }
        });
    }

    //data using username
    public void addtask() {

        Calendar current = Calendar.getInstance();
        Date currenttime = current.getTime();

        String user_task = addTaskEditText.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        int a = user.getEmail().indexOf("@");
        email = user.getEmail().substring(0,a);

        if (TextUtils.isEmpty(user_task)) {
            Toast.makeText(this, "Please Enter Valid Input", Toast.LENGTH_LONG).show();

        } else if(datentime.compareTo(currenttime) <= 0) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();

        } else {
            String id = databaseReference.push().getKey();
            TaskSender ts = new TaskSender(datentime, user_task);
            addTaskEditText.setText("");
            databaseReference.child(email).child(id).setValue(ts);
            Toast.makeText(this, "Task added at " + datentime, Toast.LENGTH_LONG).show();

        }
    }

    /*//data using userid
    public void addtask() {

        String user_task = addTaskEditText.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userid = user.getUid();

        if (TextUtils.isEmpty(user_task)) {
            Toast.makeText(this, "Please Enter Valid Input", Toast.LENGTH_LONG).show();

        } else {
            String id = databaseReference.push().getKey();
            TaskSender ts = new TaskSender(datentime, id, user_task);
            addTaskEditText.setText("");
            databaseReference.child(userid).setValue(ts);
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_LONG).show();

        }
    }*/
}