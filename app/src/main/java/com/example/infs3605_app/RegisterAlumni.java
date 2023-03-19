package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterAlumni extends AppCompatActivity {

    TextView alumniFirstName, alumniLastName, alumniEmail, graduationYear, uniDegree;
    Button registerAlumni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_alumni);

        alumniFirstName = findViewById(R.id.alumniFirstName);
        alumniLastName = findViewById(R.id.alumniLastName);
        alumniEmail = findViewById(R.id.alumniEmail);
        graduationYear = findViewById(R.id.graduationYear);
        uniDegree = findViewById(R.id.uniDegree);
        registerAlumni = findViewById(R.id.registerAlumni);
    }
}