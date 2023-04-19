package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutPageActivity extends AppCompatActivity {

    ImageView backButton, profileButton;
    String page;
    String pageClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Your Profile");
        setSupportActionBar(toolbar);
        page = getIntent().getStringExtra("PAGE");
        pageClass = "com.example.infs3605_app." + page;

        backButton = findViewById(R.id.backButton);
        profileButton = findViewById(R.id.menuButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class<?> cls = Class.forName(pageClass);
                    Intent intent = new Intent(getApplicationContext(), cls);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class<?> cls = Class.forName(pageClass);
                    Intent intent = new Intent(getApplicationContext(), cls);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}