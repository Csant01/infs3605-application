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

public class ViewParticipantsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewParticipantsAdapter adapter;
    RecyclerView viewParticipantsRv;
    ImageView backButton, profileButton;
    ArrayList<User> allUsers;
    DatabaseConnector db;
    String eventId;
    private static final String TAG = "ViewParticipantsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_participants);
        viewParticipantsRv = findViewById(R.id.viewParticipantsRv);

        eventId = getIntent().getStringExtra("EVENT_ID");
        db = new DatabaseConnector(this);
        allUsers = db.getParticipants(eventId);
        adapter = new ViewParticipantsAdapter(this, allUsers);
        viewParticipantsRv.setAdapter(adapter);
        viewParticipantsRv.setLayoutManager(new LinearLayoutManager(this));

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Participants");
        setSupportActionBar(toolbar);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StaffAllEventsActivity.class));
            }
        });

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