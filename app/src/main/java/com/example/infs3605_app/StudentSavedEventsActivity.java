package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class StudentSavedEventsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    DatabaseConnector db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_saved_events);
        db = new DatabaseConnector(this);
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);


        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Saved Events");
        setSupportActionBar(toolbar);

        // Bottom Navigation set for Saved Events (student view)
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.savedNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.homeNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentHomePageActivity.class));
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

                    case R.id.savedNavButton:
                        return true;
                }
                return false;
            }
        });
    }

    private ArrayList<String> getUserEvents (String user) {
        ArrayList<String> events = new ArrayList<>();
        ArrayList<UserEvent> userEvents = db.getUserEvents(user);
        for (int i = 0; i < userEvents.size(); i++) {
            events.add(userEvents.get(i).getEventId());
        }

        return events;
    }

    private ArrayList<Event> getEventDetails (ArrayList<String> userEvents) {
        ArrayList<Event> allEvents = db.getEventInfo();
        ArrayList<Event> filteredEvents = new ArrayList<>();

        for (int i = 0; i < allEvents.size(); i++) {
            if (userEvents.contains(allEvents.get(i).getEventId())) {
                filteredEvents.add(allEvents.get(i));
            }
        }

        return filteredEvents;
    }
}