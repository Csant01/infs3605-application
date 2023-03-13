package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AllEventsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        // Bottom Navigation set for All Events (student view)
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.allEventsNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.homeNavButton:
                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.savedNavButton:
                        startActivity(new Intent(getApplicationContext(), SavedEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.pastEventsNavButton:
                        startActivity(new Intent(getApplicationContext(), PastEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.followingNavButton:
                        startActivity(new Intent(getApplicationContext(), FollowingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.allEventsNavButton:
                        return true;
                }
                return false;
            }
        });

    }
}