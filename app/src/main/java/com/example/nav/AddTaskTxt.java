package com.example.nav;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddTaskTxt extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    EditText addTaskEditText;
    Button addTaskBtn, txtdate, txttime;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Date datentime;
    String userid, email;
    private FirebaseUser firebaseUser;

    private Context context = this;
    ImageView dateicon, timeicon;
    TextView set_date, set_time;

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
                datedialog.setContentView(R.layout.custom_date_dialog);
                dateicon = datedialog.findViewById(R.id.dateicon);
                dateicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment date = new DatePickerFragment();
                        date.show(AddTaskTxt.this.getSupportFragmentManager(), "Date Picker");
                    }
                });
                datedialog.show();
            }
        });

        txttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog timedialog = new Dialog(context);
                timedialog.setContentView(R.layout.custom_time_dialog);
                timeicon = timedialog.findViewById(R.id.timeicon);
                timeicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment time = new TimePickerFragment();
                        time.show(getSupportFragmentManager(), "Time Picker");
                    }
                });
                timedialog.show();
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
        email = user.getEmail().substring(0, a);

        if (TextUtils.isEmpty(user_task)) {
            Toast.makeText(this, "Please Enter Valid Input", Toast.LENGTH_LONG).show();

        } else if (datentime.compareTo(currenttime) <= 0) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();

        } else {
            String id = databaseReference.push().getKey();
            TaskSender ts = new TaskSender(datentime, user_task);
            addTaskEditText.setText("");
            databaseReference.child(email).child(id).setValue(ts);
            Toast.makeText(this, "Task added at " + datentime, Toast.LENGTH_LONG).show();

        }
    }
}
