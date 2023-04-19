package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Random;

public class StudentProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView profileImage, backButton;
    TextView email, fullName, linkedIn;
    ImageButton aboutPage;
    Button logout;
    DatabaseConnector db;
    User user;
    String page;
    String pageClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Your Profile");
        setSupportActionBar(toolbar);

        String loggedInUser = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        Random rand = new Random();
        db = new DatabaseConnector(this);
        page = getIntent().getStringExtra("PAGE");
        pageClass = "com.example.infs3605_app." + page;
        ArrayList<User> allUsers = db.getUserInfo();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserName().equals(loggedInUser)) {
                user = allUsers.get(i);
            }
        }

        profileImage = findViewById(R.id.studentProfileImage);
        email = findViewById(R.id.studentProfileEmail);
        fullName = findViewById(R.id.studentProfileName);
        aboutPage = findViewById(R.id.studentProfileAboutButton);
        logout = findViewById(R.id.studentLogOutButton);
        linkedIn = findViewById(R.id.studentProfileLinkedIn);

        profileImage.setImageResource(R.drawable.circle_user);
        email.setText(user.getUserEmail());
        fullName.setText(user.getUserFirstName() + " " + user.getUserLastName());
        String linkedInString = generateRandomLinkedIn(rand.nextInt(6)+1);
        linkedIn.setText(linkedInString);
        linkedIn.setTextColor(Color.BLUE);
        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkedInString));
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LandingPageActivity.class));
            }
        });

        aboutPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutPageActivity.class);
                intent.putExtra("PAGE", "StudentProfileActivity");
                startActivity(intent);
            }
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!page.isEmpty() && !page.equals("OrganiserPublicProfileActivity")) {
                    try {
                        Class<?> cls = Class.forName(pageClass);
                        Intent intent = new Intent(getApplicationContext(), cls);
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Bottom Navigation set for Student Profile
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.homeNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.savedNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentSavedEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.allEventsNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentAllEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.pastEventsNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentPastEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.followingNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentFollowingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.homeNavButton:
                        return true;
                }
                return false;
            }
        });

    }

    public String generateRandomLinkedIn (int num) {
        if (num == 1) {
            return "https://www.linkedin.com/in/williamhgates/";
        } else if (num == 2) {
            return "https://www.linkedin.com/in/daniel-ricciardo/";
        } else if (num == 3) {
            return "https://www.linkedin.com/in/barackobama/";
        } else if (num == 4) {
            return "https://www.linkedin.com/in/melindagates/";
        } else if (num == 5) {
            return "https://www.linkedin.com/in/susan-wojcicki-b136a99/";
        } else {
            return "https://www.linkedin.com/in/rbranson/";
        }
    }
}