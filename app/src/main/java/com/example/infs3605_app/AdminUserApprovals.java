package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class AdminUserApprovals extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView logout;
    TabLayout userApprovalTabLayout;
    ViewPager userApprovalViewPager;
    AdminUserApprovalViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_approvals);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_log_out);
        setTitle("User Approvals");
        setSupportActionBar(toolbar);
        logout = findViewById(R.id.logOutButton);
        userApprovalTabLayout = findViewById(R.id.userApprovalTabLayout);
        userApprovalViewPager = findViewById(R.id.userApprovalViewPager);
        adapter = new AdminUserApprovalViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentUserPending(), "PENDING");
        adapter.addFragment(new FragmentUserApproved(), "APPROVED");
        adapter.addFragment(new FragmentUserRejected(), "REJECTED");
        userApprovalViewPager.setAdapter(adapter);
        userApprovalTabLayout.setupWithViewPager(userApprovalViewPager);


        // Bottom Navigation set for Admin User Approvals Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.dashboardNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.adminEventApprovalsButton:
                        startActivity(new Intent(getApplicationContext(), AdminEventApprovals.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.adminUserApprovalsButton:
                        return true;
                }
                return false;
            }
        });

    }
}