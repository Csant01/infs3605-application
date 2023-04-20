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
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class EventsPendingApprovalActivity extends AppCompatActivity implements StaffPendingEventsAdapter.StaffPendingEventsInterface {

    BottomNavigationView bottomNavigationView;
    ImageView profileButton;
    private static final String TAG = "EventsPendingApprovalActivity";
    RecyclerView pendingEventsRv;
    StaffPendingEventsAdapter adapter;
    ArrayList<Event> allEvents;
    DatabaseConnector db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_pending_approval);
        pendingEventsRv = findViewById(R.id.pendingEventsRv);
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        db = new DatabaseConnector(this);

        allEvents = db.getPendingEvents(user);
        adapter = new StaffPendingEventsAdapter(this, allEvents, this);
        pendingEventsRv.setAdapter(adapter);
        pendingEventsRv.setLayoutManager(new LinearLayoutManager(this));

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Events Pending Approval");
        setSupportActionBar(toolbar);
        profileButton = findViewById(R.id.menuButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrganiserProfileActivity.class);
                intent.putExtra("PAGE", TAG);
                startActivity(intent);
            }
        });


        // Bottom Navigation set for Events Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.approvalsNavButton);

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

                    case R.id.eventsNavButton:
                        startActivity(new Intent(getApplicationContext(), StaffAllEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.feedbackNavButton:
                        startActivity(new Intent(getApplicationContext(), EventFeedbackActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.approvalsNavButton:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(getApplicationContext(), ManageEventDetailActivity.class);
        intent.putExtra("PAGE", TAG);
        intent.putExtra("EVENT_ID", allEvents.get(position).getEventId());
        startActivity(intent);
    }
}