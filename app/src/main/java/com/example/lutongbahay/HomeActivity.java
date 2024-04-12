package com.example.lutongbahay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    ImageButton btn_tofilter, btn_toinsert;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Access resources
        btn_tofilter = findViewById(R.id.btn_tofilter);
        btn_toinsert = findViewById(R.id.btn_toinsert);

        // Direct to filter activity
        btn_tofilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(HomeActivity.this, FilterActivity.class);
                startActivity(i);
            }
        });

        // Direct to insert activity
        btn_toinsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(HomeActivity.this, InsertActivity.class);
                startActivity(i);
            }
        });
    }
}