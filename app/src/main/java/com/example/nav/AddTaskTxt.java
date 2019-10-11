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

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddTaskTxt extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    DatePicker pickerDate;
    TimePicker pickerTime;
    EditText addTaskEditText;
    Button addTaskBtn, txtdate, txttime;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Date datentime;
    String userid, email;
    private Calendar calendarView;
    private FirebaseUser firebaseUser;

    private Context context = this;
    ImageView dateicon, timeicon;

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

        //pickerDate = findViewById(R.id.pickerdate);
        //pickerTime = findViewById(R.id.pickertime);
        /*Calendar now = Calendar.getInstance();
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
        });*/

        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog datedialog = new Dialog(context);
                datedialog.setContentView(R.layout.custom_date_dialog);
                ImageButton dateicon = datedialog.findViewById(R.id.dateicon);
                dateicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AddTaskTxt.this, "DatePicker", Toast.LENGTH_SHORT).show();
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
                ImageButton timeicon = timedialog.findViewById(R.id.timeicon);
                timeicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AddTaskTxt.this, "TimePicker", Toast.LENGTH_SHORT).show();
                    }
                });
                timedialog.show();
            }
        });

        /*// toolbar activity starts

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Task Planner");
        toolbar.setSubtitle("Systematise your day!!!");

        // toolbar activity ends*/
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

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addtask_txt) {
            startActivity(new Intent(AddTaskTxt.this, AddTaskTxt.class));

        } else if (id == R.id.nav_addtask_voice) {
            startActivity(new Intent(AddTaskTxt.this, AddTaskVoice.class));

        } else if (id == R.id.nav_agenda) {
            startActivity(new Intent(AddTaskTxt.this, ViewTask.class));

        } else if (id == R.id.nav_cng_pass) {
            startActivity(new Intent(AddTaskTxt.this, ResetPassword.class));

        } else if (id == R.id.nav_del_acc) {
            firebaseUser.delete();
            startActivity(new Intent(this, Login.class));

        } else if (id == R.id.nav_logout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            signOut();
            finish();
            //starting login activity
            startActivity(new Intent(this, Login.class));

        } else if (id == R.id.nav_info) {
            startActivity(new Intent(this, Info.class));

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Check it out. Your message goes here";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Sharing Options"));
            return true;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {

    }*/
}
