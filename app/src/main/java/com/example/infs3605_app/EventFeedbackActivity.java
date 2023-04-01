package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class EventFeedbackActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_feedback);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Feedback");
        setSupportActionBar(toolbar);

        // Bottom Navigation set for Events Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.feedbackNavButton);

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

                    case R.id.approvalsNavButton:
                        startActivity(new Intent(getApplicationContext(), EventsPendingApprovalActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.feedbackNavButton:
                        return true;
                }
                return false;
            }
        });
    }
}