package com.example.lutongbahay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Handler handle;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Execute change of activity
        startChange();
    }

    public void startChange(){

        // Create handler
        handle = new Handler();

        // Delay change of activity
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {

                // Create intent and direct it to home activity after 3 seconds
                i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
            // Delay the process in 3 seconds
        }, 3000);
    }
}