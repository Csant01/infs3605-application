package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class StudentAllEventsActivity extends AppCompatActivity {

    RecyclerView allEventsRv;
    ArrayList<String> eventName, eventDate, eventOrg;
    Button networkBtn, careerBtn, travelBtn, socialBtn;
    DatabaseConnector db;
    StudentAllEventsAdapter adapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_all_events);
        db = new DatabaseConnector(this);

        // Assignment of UI elements
        networkBtn = findViewById(R.id.networkFilterButton);
        careerBtn = findViewById(R.id.careerFilterButton);
        travelBtn = findViewById(R.id.travelFilterButton);
        socialBtn = findViewById(R.id.socialFilterButton);


        networkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilteredEventData("Network");
                recreate();
            }
        });

        careerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilteredEventData("Careers");
                recreate();
            }
        });

        travelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilteredEventData("Travel");
                recreate();
            }
        });

        socialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilteredEventData("Social");
                recreate();
            }
        });



        // RecyclerView setup
        eventName = new ArrayList<>();
        eventDate = new ArrayList<>();
        eventOrg = new ArrayList<>();
        allEventsRv = findViewById(R.id.studentAllEventsRV);
        adapter = new StudentAllEventsAdapter(this, eventName, eventDate, eventOrg);
        allEventsRv.setAdapter(adapter);
        allEventsRv.setLayoutManager(new LinearLayoutManager(this));
        if (!displayEventData()) {
            Toast.makeText(StudentAllEventsActivity.this, "No events " +
                            " currently.",
                    Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bottom Navigation set for All Events (student view)
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.allEventsNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.homeNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentHomePageActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.savedNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentSavedEventsActivity.class));
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

                    case R.id.allEventsNavButton:
                        return true;
                }
                return false;
            }
        });

    }

    public boolean displayEventData () {
        ArrayList<Event> allEvents = db.getEventInfo();
        if (!allEvents.isEmpty()) {
            eventName.clear();
            eventDate.clear();
            eventOrg.clear();
            for (int i = 0; i < allEvents.size(); i++) {
                if (allEvents.get(i).getEventIsApproved() != 0) {
                    eventName.add(allEvents.get(i).getEventName());
                    eventDate.add(db.formatEpoch(allEvents.get(i).getEventEndDate()));
                    eventOrg.add(allEvents.get(i).getEventOwner());
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean displayFilteredEventData (String filter) {
        ArrayList<Event> allEvents = db.getEventInfo();
        if (!allEvents.isEmpty()) {
            eventName.clear();
            eventDate.clear();
            eventOrg.clear();
            for (int i = 0; i < allEvents.size(); i++) {
                if (allEvents.get(i).getEventCategory().equals(filter)) {
                    eventName.add(allEvents.get(i).getEventName());
                    eventDate.add(db.formatEpoch(allEvents.get(i).getEventEndDate()));
                    eventOrg.add(allEvents.get(i).getEventOwner());
                }
            }
            return true;
        } else {
            return false;
        }
    }
}