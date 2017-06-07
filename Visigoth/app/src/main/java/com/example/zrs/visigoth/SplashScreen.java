package com.example.zrs.visigoth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

    public static final String PREFS_NAME = "CoreSkillsPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Log.i("Username:", settings.getString("username", ""));

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    if (settings.getString("username", "") == null || settings.getString("username","") == ""){
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Log.i("HEY", "Why am I here");
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }

                }
            }
        };
        timerThread.start();
    }
}
