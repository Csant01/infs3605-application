package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateUserActivity extends AppCompatActivity {

    TextView createUserName;
    TextView createUserPass;
    TextView createUserPassCheck;
    TextView createUserEmail;
    AutoCompleteTextView createUserType;
    Button createUserButton;
    String userName;
    String userEmail;
    String userPass;
    String userPassCheck;
    String userType;
    ArrayList<String> userTypeArray = new ArrayList<>();
    ArrayAdapter<String> userTypeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        userTypeArray.add("Student");
        userTypeArray.add("UNSW Staff");
        userTypeArray.add("Event Co-ordinator");
        userTypeArray.add("UNSW Alumni");

        createUserName = findViewById(R.id.createUserName);
        createUserPass = findViewById(R.id.createUserPass);
        createUserPassCheck = findViewById(R.id.createUserPassCheck);
        createUserEmail = findViewById(R.id.createUserEmail);
        createUserType = findViewById(R.id.createUserTypeSub);
        createUserButton = findViewById(R.id.createUserButton);

        userTypeAdapter = new ArrayAdapter<>(this, R.layout.user_type_list, userTypeArray);
        createUserType.setAdapter(userTypeAdapter);

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = createUserName.getText().toString();
                userPass = createUserPass.getText().toString();
                userPassCheck = createUserPassCheck.getText().toString();
                userEmail = createUserEmail.getText().toString();
                userType = createUserType.getText().toString();
            }
        });




    }
}