package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class StudentPastEventsActivity extends AppCompatActivity implements StudentPastEventsAdapter.OnPastEventClickListener{

    BottomNavigationView bottomNavigationView;
    RecyclerView pastEventsRv;
    Button feedbackButton;
    DatabaseConnector db;
    ArrayList<Event> eventList;
    long epochSeconds;
    StudentPastEventsAdapter adapter;
    TextView feedbackCompleted;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_past_events);
        db = new DatabaseConnector(this);
        feedbackButton = findViewById(R.id.feedbackButton);
        // Need a method to check if feedback was completed.
        feedbackCompleted = findViewById(R.id.feedbackStatusLabel);
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        ArrayList<String> userEvents = getUserEvents(user);
        Date currentDate = new Date();
        long epochMillis = currentDate.getTime();
        epochSeconds = epochMillis / 1000L;
        searchView = findViewById(R.id.pastSearchView);

        eventList = new ArrayList<>();
        eventList = getEventDetails(userEvents);
        pastEventsRv = findViewById(R.id.pastEventsRv);
        adapter = new StudentPastEventsAdapter(this, eventList, this);
        pastEventsRv.setAdapter(adapter);
        pastEventsRv.setLayoutManager(new LinearLayoutManager(this) );

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
        setTitle("Past Events");
        setSupportActionBar(toolbar);

        // Bottom Navigation set for Past Events (student view)
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.pastEventsNavButton);

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

                    case R.id.allEventsNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentAllEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.followingNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentFollowingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.pastEventsNavButton:
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
                if (allEvents.get(i).getEventDate() < epochSeconds) {
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
        intent.putExtra("PAGE", "StudentPastEvents");
        startActivity(intent);
    }
}