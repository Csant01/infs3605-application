package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class CreateUserActivity extends AppCompatActivity {

    private static final String TMP_STRING = "TEMP";
    TextView createUserName;
    TextView createUserPass;
    TextView createUserPassCheck;
    TextView createUserEmail;
    TextView createUserFirstName;
    TextView createUserLastName;
    AutoCompleteTextView createUserType;
    AutoCompleteTextView createUserGender;
    Button createUserButton;
    String userName;
    String userEmail;
    String userPass;
    String userPassCheck;
    String userType;
    String userFirstName;
    String userLastName;
    String userGender;
    ArrayList<String> userTypeArray = new ArrayList<>();
    ArrayAdapter<String> userTypeAdapter;
    ArrayList<String> userGenderArray = new ArrayList<>();
    ArrayAdapter<String> userGenderAdapter;
    DatabaseConnector db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        db = new DatabaseConnector(this);

        userTypeArray.add("Student");
        userTypeArray.add("UNSW Staff");
        userTypeArray.add("Event Co-ordinator");
        userTypeArray.add("UNSW Alumni");
        userGenderArray.add("Male");
        userGenderArray.add("Female");
        userGenderArray.add("Other");
        userGenderArray.add("Wish to not specify");

        createUserName = findViewById(R.id.createUserName);
        createUserPass = findViewById(R.id.createUserPass);
        createUserPassCheck = findViewById(R.id.createUserPassCheck);
        createUserEmail = findViewById(R.id.createUserEmail);
        createUserType = findViewById(R.id.createUserTypeSub);
        createUserButton = findViewById(R.id.createUserButton);
        createUserGender = findViewById(R.id.createUserGenderSub);
        createUserFirstName = findViewById(R.id.createUserFirstName);
        createUserLastName = findViewById(R.id.createUserLastName);

        userTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_list, userTypeArray);
        createUserType.setAdapter(userTypeAdapter);

        userGenderAdapter = new ArrayAdapter<>(this, R.layout.dropdown_list, userGenderArray);
        createUserGender.setAdapter(userGenderAdapter);

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = createUserName.getText().toString();
                userPass = createUserPass.getText().toString();
                userPassCheck = createUserPassCheck.getText().toString();
                userEmail = createUserEmail.getText().toString();
                userType = createUserType.getText().toString();
                userFirstName = createUserFirstName.getText().toString();
                userLastName = createUserLastName.getText().toString();
                userGender = createUserGender.getText().toString();
                User user = new User(TMP_STRING, userFirstName, userLastName, userGender, userName,
                        userEmail, userPass, userType);

                if (user.hasEmptyFields()) {
                    Toast.makeText(CreateUserActivity.this, "Please ensure all fields" +
                                    " are entered.",
                            Toast.LENGTH_SHORT).show();

                } else if (checkEmail(userEmail, userType)) {
                    if (checkPass(userPass, userPassCheck)) {
                        user.setUserID(createUserID(userType));
                        user.setUserType(checkUserType(user));
                        if(db.addUserToDatabase(user)) {
                            Toast.makeText(CreateUserActivity.this, "User created successfully.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CreateUserActivity.this, "Please ensure to use your" +
                                        " passwords match.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateUserActivity.this, "Please ensure to use your" +
                                    " UNSW email.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public boolean checkPass (String pass, String passCheck) {
        if (pass.equals(passCheck)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmail (String email, String userType) {

        String emailPattern = ".*@[^.@]+\\.unsw\\.edu\\.au$";

        if (!userType.equals("Student") && !userType.equals("UNSW Staff")) {
            return true;
        } else if (email.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }

    public String createUserID (String userType) {
        String first = userType.toLowerCase().substring(0,1);
        Random rand = new Random();
        int min = 1111111;
        int max = 9999999;
        int randomNumber = rand.nextInt((max - min) + 1) + min;

        return first + randomNumber;

    }

    public String checkUserType (User user) {
        if (user.getUserType().equals("Student")) {
            return "1";
        } else if (user.getUserType().equals("UNSW Staff")) {
            return "2";
        } else if (user.getUserType().equals("Event Co-ordinator")) {
            return "3";
        } else if (user.getUserType().equals("UNSW Alumni")) {
            return "4";
        } else {
            return "999";
        }
    }
}
