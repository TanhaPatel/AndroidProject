package com.example.nav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText usernameEditTxt, signupemailEditTxt, signuppasswordEditTxt, signupconfirmpasswordEditTxt;
    private Button signupbtn1;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        usernameEditTxt = findViewById(R.id.usernameEditTxt);
        signupemailEditTxt = findViewById(R.id.signupemailEditTxt);
        signuppasswordEditTxt = findViewById(R.id.signuppasswordEditTxt);
        signupconfirmpasswordEditTxt = findViewById(R.id.signupconfirmpasswordEditTxt);
        signupbtn1 =  findViewById(R.id.signupbtn1);

        signupbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == signupbtn1) {
                    registeruser();
                }
            }
        });
    }

    private void registeruser() {
        String username = usernameEditTxt.getText().toString();
        String email = signupemailEditTxt.getText().toString();
        String password = signuppasswordEditTxt.getText().toString();
        String confirmpassword = signupconfirmpasswordEditTxt.getText().toString();

        if(!username.equals("") && !email.equals("") && !password.equals("") && !confirmpassword.equals(""))
        {
            startActivity(new Intent(getApplicationContext(), Drawer.class));
        } else {
            Toast.makeText(SignUp.this, "Fill all the required fields", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Registering user!");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User Logged in Successfully
                            Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Drawer.class));
                        } else {
                            Toast.makeText(SignUp.this, "Failed To register. Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}