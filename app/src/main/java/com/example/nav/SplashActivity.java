package com.example.nav;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    TextView splash_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,  MainScreen.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

        splash_txt = findViewById(R.id.splash_txt);
        Animation text = AnimationUtils.loadAnimation(this, R.anim.splash_text);
        splash_txt.startAnimation(text);
    }
}
