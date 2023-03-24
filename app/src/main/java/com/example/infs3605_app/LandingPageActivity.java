package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LandingPageActivity extends AppCompatActivity {

    Button testBtn;
    DatabaseConnector db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        testBtn = findViewById(R.id.testBtn);
        db = new DatabaseConnector(this);
//        db.addSampleEventData();

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StudentAllEventsActivity.class));
            }
        });
    }
}