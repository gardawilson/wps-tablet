package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final String DEBUG_LAUNCHER_CLASS =
            "com.example.myapplication.DebugUiLauncherActivity";
    private static final long SPLASH_DURATION = 1500; // Durasi SplashScreen dalam milidetik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG && launchDebugUiWorkflow()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);

        // Menunda perpindahan ke aktivitas utama setelah durasi tertentu
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }

    private boolean launchDebugUiWorkflow() {
        try {
            Class<?> debugLauncher = Class.forName(DEBUG_LAUNCHER_CLASS);
            startActivity(new Intent(this, debugLauncher));
            return true;
        } catch (ClassNotFoundException exception) {
            Log.e(TAG, "Debug UI launcher tidak ditemukan", exception);
            return false;
        }
    }
}
