package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class StudentSavedEventsActivity extends AppCompatActivity implements StudentSavedEventsAdapter.OnSavedEventClickListener{

    BottomNavigationView bottomNavigationView;
    DatabaseConnector db;
    StudentSavedEventsAdapter adapter;
    RecyclerView savedEventsRv;
    ArrayList<Event> eventList;
    long epochSeconds;
    SearchView searchView;
    private static final String TAG = "StudentSavedEventsActivity";
    ImageView profileButton;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_saved_events);
        db = new DatabaseConnector(this);
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        ArrayList<String> userEvents = getUserEvents(user);
        Date currentDate = new Date();
        long epochMillis = currentDate.getTime();
        epochSeconds = epochMillis / 1000L;
        searchView = findViewById(R.id.savedSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                Log.d("PastEvents Filter", newText);
                return false;
            }
        });


        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Saved Events");
        setSupportActionBar(toolbar);

        profileButton = findViewById(R.id.menuButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                intent.putExtra("PAGE", TAG);
                startActivity(intent);
            }
        });

        //RV Setup
        eventList = new ArrayList<>();
        eventList = getEventDetails(userEvents);
        savedEventsRv = findViewById(R.id.savedEventsRV);
        adapter = new StudentSavedEventsAdapter(this, eventList, this);
        savedEventsRv.setAdapter(adapter);
        savedEventsRv.setLayoutManager(new LinearLayoutManager(this));

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
                if (allEvents.get(i).getEventDate() > epochSeconds) {
                    filteredEvents.add(allEvents.get(i));
                }
            }
        }

        return filteredEvents;
    }

    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("EVENT_ID", eventList.get(position).getEventId());
        intent.putExtra("USER_TYPE", "student");
        intent.putExtra("PAGE", "StudentSavedEvents");
        startActivity(intent);
    }
}