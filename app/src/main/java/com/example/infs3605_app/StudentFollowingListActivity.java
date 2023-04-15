package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class StudentFollowingListActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TabLayout followingTabLayout;
    ViewPager followingViewPager;
    StudentFollowingViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_following_list);
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Following List");
        setSupportActionBar(toolbar);

        followingTabLayout = findViewById(R.id.followingTabLayout);
        followingViewPager = findViewById(R.id.leaderboardViewPager);
        adapter = new StudentFollowingViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentGlobal(), "GLOBAL");
        adapter.addFragment(new FragmentFollowing(), "FOLLOWING");

        followingViewPager.setAdapter(adapter);
        followingTabLayout.setupWithViewPager(followingViewPager);
//        followingTabLayout.addTab(followingTabLayout.newTab().setText("GLOBAL"));
//        followingTabLayout.addTab(followingTabLayout.newTab().setText("FOLLOWING"));
        followingViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + position);
                if (fragment instanceof FragmentFollowing) {
                    ((FragmentFollowing) fragment).refreshRecyclerView(user);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        // Bottom Navigation set for Following List (student view)
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.followingNavButton);

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

                    case R.id.allEventsNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentAllEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.pastEventsNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentPastEventsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.followingNavButton:
                        return true;
                }
                return false;
            }
        });
    }
}