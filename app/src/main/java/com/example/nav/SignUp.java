package com.example.nav;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText signupemailEditText;
    private EditText signuppasswordEditTxt;
    private Button signupbtn1;
    private CheckBox signupshowpassword;

    private ProgressDialog progressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(!isConnected(SignUp.this))
        {
            buildDialog(SignUp.this).show();
        }

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
        signupbtn1 = (Button) findViewById(R.id.signupbtn1);
        signupshowpassword = findViewById(R.id.signupshowpassword);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        signupbtn1.setOnClickListener(this);
        signupshowpassword.setOnClickListener(this);

    }

    //Internet detection code Start

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setCancelable(false);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        return builder;

    }

    // Internet detection code ends

    private void registerUser(){

        //getting email and password from edit texts
        String email = signupemailEditText.getText().toString().trim();
        String password  = signuppasswordEditTxt.getText().toString().trim();

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

        if(password.length() < 6 || password.length() > 10){
            signuppasswordEditTxt.setError("Password should be between 6 to 10 characters");
            return;
        }

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
                    sendVerificationEmail();

                } else {
                    // if failed
                    Toast.makeText(SignUp.this,"Registration Error",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
                }
            });
    }

    // show password code starts

    private void showpassword() {
        if(signupshowpassword.isChecked()) {
            // show password
            signuppasswordEditTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //Toast.makeText(MainActivity.this, "WordPress Checked", Toast.LENGTH_LONG).show();
        } else {
            // hide password
            signuppasswordEditTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //Toast.makeText(MainActivity.this, "WordPress Un-Checked", Toast.LENGTH_LONG).show();
        }
    }

    // show password code ends

    // email verification code starts

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // email sent
                    // after email is sent just logout the user and finish this activity
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(SignUp.this, Login.class));
                    finish();
                } else {
                    // email not sent, so display message and restart the activity or do whatever you wish to do
                    //restart this activity
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                }
                }
            });
    }

    // email verification code ends

    @Override
    public void onClick(View v) {

        if(v == signupbtn1){
            registerUser();
        }

        if (v == signupshowpassword) {
            showpassword();
        }
    }
}