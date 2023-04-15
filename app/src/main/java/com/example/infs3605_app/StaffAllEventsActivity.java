package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class StaffAllEventsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TabLayout staffEventsTabLayout;
    ViewPager staffEventsViewPager;
    StaffEventsViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_all_events);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("All Events");
        setSupportActionBar(toolbar);

        staffEventsTabLayout = findViewById(R.id.staffEventsTabLayout);
        staffEventsViewPager = findViewById(R.id.staffEventsViewPager);
        adapter = new StaffEventsViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentStaffAll(), "ALL EVENTS");
        adapter.addFragment(new FragmentStaffOwn(), "YOUR EVENTS");

        staffEventsViewPager.setAdapter(adapter);
        staffEventsTabLayout.setupWithViewPager(staffEventsViewPager);

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