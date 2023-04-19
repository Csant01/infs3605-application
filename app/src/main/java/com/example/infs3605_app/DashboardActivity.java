package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TabLayout dashboardTabLayout;
    ViewPager dashboardViewPager;
    DashboardViewPagerAdapter adapter;
    ImageView profileButton;
    private static final String TAG = "DashboardActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Dashboard");
        setSupportActionBar(toolbar);

        dashboardTabLayout = findViewById(R.id.dashboardTabLayout);
        dashboardViewPager = findViewById(R.id.dashboardViewPager);
        adapter = new DashboardViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentDashboardGeneral(), "GENERAL METRICS");
        adapter.addFragment(new FragmentDashboardCosting(), "COSTING METRICS");
        dashboardViewPager.setAdapter(adapter);
        dashboardTabLayout.setupWithViewPager(dashboardViewPager);

        profileButton = findViewById(R.id.menuButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrganiserProfileActivity.class);
                intent.putExtra("PAGE", TAG);
                startActivity(intent);
            }
        });

        // Bottom Navigation set for Dashboard Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.dashboardNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.eventsNavButton:
                        startActivity(new Intent(getApplicationContext(), StaffAllEventsActivity.class));
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

                    case R.id.dashboardNavButton:
                        return true;
                }
                return false;
            }
        });

    }



}