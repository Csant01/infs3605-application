package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

public class CreateEventActivity extends AppCompatActivity {

    TextInputLayout eventName, eventCategory, eventDate, eventTime, eventDescription;
    AppCompatButton selectImage, submitEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventName = findViewById(R.id.eventName);
        eventCategory = findViewById(R.id.eventCategory);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        eventDescription = findViewById(R.id.eventDescription);
        selectImage = findViewById(R.id.selectImage);
        submitEvent = findViewById(R.id.submitEvent);

    }
}