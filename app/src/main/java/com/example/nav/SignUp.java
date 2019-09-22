package com.example.nav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText signupemailEditText;
    private EditText signuppasswordEditTxt;
    private EditText signupconfirmpasswordEditTxt;
    private Button signupbtn1;

    private ProgressDialog progressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), Drawer.class));
        }

        //initializing views
        signupemailEditText = (EditText) findViewById(R.id.signupemailEditTxt);
        signuppasswordEditTxt = (EditText) findViewById(R.id.signuppasswordEditTxt);
        signupconfirmpasswordEditTxt = (EditText) findViewById(R.id.signupconfirmpasswordEditTxt);
        signupbtn1 = (Button) findViewById(R.id.signupbtn1);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        signupbtn1.setOnClickListener(this);

    }

    private void registerUser(){

        //getting email and password from edit texts
        String email = signupemailEditText.getText().toString().trim();
        String password  = signuppasswordEditTxt.getText().toString().trim();
        String confirmpassword  = signupconfirmpasswordEditTxt.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password doesn't match",Toast.LENGTH_LONG).show();
            return;
        }

        if(!(password == confirmpassword)){
            signupconfirmpasswordEditTxt.setError("Password doesn't match");
            return;
        }

        if(password.length() < 6 || password.length() > 10){
            signuppasswordEditTxt.setError("Password should be between 6 to 10 characters");
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), Drawer.class));
                        }else{
                            //display some message here
                            Toast.makeText(SignUp.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {

        if(view == signupbtn1){
            registerUser();
        }
    }
}