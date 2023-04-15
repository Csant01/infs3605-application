package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminEventApprovals extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_approvals);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_log_out);
        setTitle("Event Approvals");
        setSupportActionBar(toolbar);

        // Bottom Navigation set for Admin Event Approvals Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.dashboardNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.adminUserApprovalsButton:
                        startActivity(new Intent(getApplicationContext(), AdminUserApprovals.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.adminEventApprovalsButton:
                        return true;
                }
                return false;
            }
        });

    }
}