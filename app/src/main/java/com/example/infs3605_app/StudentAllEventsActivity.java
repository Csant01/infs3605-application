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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class StudentAllEventsActivity extends AppCompatActivity implements StudentAllEventsAdapter.OnAllEventClickListener {

    RecyclerView allEventsRv;
    ArrayList<Event> eventList;
    Button networkBtn, careerBtn, travelBtn, socialBtn;
    DatabaseConnector db;
    StudentAllEventsAdapter adapter;
    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    TextView clearFilter;

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
        clearFilter = findViewById(R.id.clearFilter);
        searchView = findViewById(R.id.allSearchView);
        searchView.clearFocus();

        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEventData();
            }
        });


        networkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilteredEventData("Network");

            }
        });

        careerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilteredEventData("Careers");

            }
        });

        travelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilteredEventData("Travel");

            }
        });

        socialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilteredEventData("Social");
            }
        });



        // RecyclerView setup
        eventList = new ArrayList<>();
        allEventsRv = findViewById(R.id.studentAllEventsRV);
        adapter = new StudentAllEventsAdapter(this, eventList, this);
        allEventsRv.setAdapter(adapter);
        allEventsRv.setLayoutManager(new LinearLayoutManager(this));
        if (!displayEventData()) {
            Toast.makeText(StudentAllEventsActivity.this, "No events " +
                            " currently.",
                    Toast.LENGTH_SHORT).show();
        }

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("All Events");
        setSupportActionBar(toolbar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                Log.d("AllEvents Filter", newText);
                return false;
            }
        });

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
            allEventsRv.getRecycledViewPool().clear();
            eventList.clear();
            adapter.notifyDataSetChanged();
            for (int i = 0; i < allEvents.size(); i++) {
                if (allEvents.get(i).getEventIsApproved() != 0) {
                    eventList.add(allEvents.get(i));
                    adapter.notifyDataSetChanged();
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
            allEventsRv.getRecycledViewPool().clear();
            eventList.clear();
            adapter.notifyDataSetChanged();
            for (int i = 0; i < allEvents.size(); i++) {
                if (allEvents.get(i).getEventCategory().equals(filter)) {
                    eventList.add(allEvents.get(i));
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onEventClick(int position) {
        eventList.get(position);
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("EVENT_ID", eventList.get(position).getEventId());
        intent.putExtra("USER_TYPE", "student");
        intent.putExtra("PAGE", "StudentAllEvents");
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        SearchView searchView = findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }
}