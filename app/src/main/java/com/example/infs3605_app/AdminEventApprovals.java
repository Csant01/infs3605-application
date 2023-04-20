package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class AdminEventApprovals extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TabLayout eventApprovalTabLayout;
    ViewPager eventApprovalViewPager;
    AdminEventApprovalViewPagerAdapter adapter;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_approvals);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_log_out);
        setTitle("Event Approvals");
        setSupportActionBar(toolbar);
        logout = findViewById(R.id.logOutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LandingPageActivity.class));
            }
        });

        eventApprovalTabLayout = findViewById(R.id.eventApprovalTabLayout);
        eventApprovalViewPager = findViewById(R.id.eventApprovalViewPager);
        adapter = new AdminEventApprovalViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentEventPending(), "PENDING");
        adapter.addFragment(new FragmentEventApproved(), "APPROVED");
        adapter.addFragment(new FragmentEventRejected(), "REJECTED");
        eventApprovalViewPager.setAdapter(adapter);
        eventApprovalTabLayout.setupWithViewPager(eventApprovalViewPager);

        // Bottom Navigation set for Admin Event Approvals Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.adminEventApprovalsButton);

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