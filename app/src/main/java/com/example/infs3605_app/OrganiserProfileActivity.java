package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class OrganiserProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView profileImage;
    TextView orgName, email, faculty;
    Button logout;
    ImageButton aboutPage;
    ImageView backButton;
    DatabaseConnector db;
    User user;
    byte[] bytes;
    String page;
    String pageClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_profile);
        String loggedIn = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        db = new DatabaseConnector(this);
        ArrayList<User> allUsers = db.getUserInfo();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserName().equals(loggedIn)) {
                user = allUsers.get(i);
            }
        }

        page = getIntent().getStringExtra("PAGE");
        pageClass = "com.example.infs3605_app." + page;

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Your Profile");
        setSupportActionBar(toolbar);

        profileImage = findViewById(R.id.staffProfileImage);
        orgName = findViewById(R.id.staffProfileName);
        email = findViewById(R.id.staffProfileEmail);
        faculty = findViewById(R.id.staffFaculty);
        logout = findViewById(R.id.staffLogOutButton);
        aboutPage = findViewById(R.id.staffProfileAboutButton);
        backButton = findViewById(R.id.backButton);

        bytes = db.retrieveOrganiserImageFromDatabaseFiltered(user.getUserID());
        if (bytes == null) {
            bytes = db.retrieveOrganiserImageDirect(user.getUserID());
            profileImage.setImageBitmap(ImageUtils.getImage(bytes));
        } else {
            profileImage.setImageBitmap(ImageUtils.getImage(bytes));
        }

        orgName.setText(user.getUserName());
        email.setText(user.getUserEmail());
        faculty.setText(user.getUserFaculty());

        aboutPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutPageActivity.class);
                intent.putExtra("PAGE", "OrganiserProfileActivity");
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!page.isEmpty()) {
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LandingPageActivity.class));
            }
        });



        // Bottom Navigation set for Events Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.eventsNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.dashboardNavButton:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.addEventNavButton:
                        startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.approvalsNavButton:
                        startActivity(new Intent(getApplicationContext(), EventsPendingApprovalActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.feedbackNavButton:
                        startActivity(new Intent(getApplicationContext(), EventFeedbackActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.eventsNavButton:
                        return true;
                }
                return false;
            }
        });

    }
}