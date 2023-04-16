package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    TextView emailInput, passwordInput;
    String userEmail, userPass;
    Button buttonLogin, buttonAlumniSignUp;
    DatabaseConnector db;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseConnector(this);
//        db.addSampleUserFeedbackData();
//        db.addSampleUserEventData();

        buttonAlumniSignUp = findViewById(R.id.buttonAlumniSignUp);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = emailInput.getText().toString();
                userPass = passwordInput.getText().toString();
                if (checkLogin(userEmail, userPass)) {
                    if (checkUserType(userEmail) == 3) {
                        startActivity(new Intent(getApplicationContext(), StaffAllEventsActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), StaffAllEventsActivity.class));
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Please ensure all details are correct.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkLogin (String email, String pass) {
        ArrayList<User> allUsers = db.getUserInfo();

        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserEmail().equals(email)) {
                if (db.toHashCode(pass) == Integer.parseInt(allUsers.get(i).getUserPass())) {
                    User.currentlyLoggedIn.add(allUsers.get(i).getUserName());
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    public int checkUserType (String email) {
        ArrayList<User> allUsers = db.getUserInfo();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserEmail().equals(email)) {
                user = allUsers.get(i);
                return Integer.parseInt(user.getUserType());
            }
        }

        return 99;
    }
}