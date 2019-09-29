package com.example.nav;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTask extends AppCompatActivity {

    EditText addTaskEditText;
    Button addTaskBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

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

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtask();
            }
        });
    }

    public void addtask() {

        String user_task = addTaskEditText.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (TextUtils.isEmpty(user_task)) {
            Toast.makeText(this, "Please Enter Valid Input", Toast.LENGTH_LONG).show();

        } else {
            String id = databaseReference.push().getKey();
            TaskSender Rn = new TaskSender(id, user_task);
            addTaskEditText.setText("");
            databaseReference.child(id).setValue(Rn);
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_LONG).show();

        }
    }
}