package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class StudentHomePageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private SwipeDeck eventSwipeDeck;
    private ArrayList<Event> eventList;
    DatabaseConnector db;
    ImageView profileButton;
    private static final String TAG = "StudentHomePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);


        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Explore Events");
        setSupportActionBar(toolbar);

        db = new DatabaseConnector(this);
        eventList = new ArrayList<>();
        eventSwipeDeck = findViewById(R.id.homePageSwipeDeck);
        displayEventData();

        final SwipeCardAdapter adapter = new SwipeCardAdapter(eventList, this);
        eventSwipeDeck.setAdapter(adapter);

        eventSwipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {

            }

            @Override
            public void cardSwipedRight(int position) {
                db = new DatabaseConnector(getApplicationContext());
                int userGoing = db.setUserGoing(User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1),
                        eventList.get(position).getEventId());
                if (userGoing == 0) {
                    Toast.makeText(getApplicationContext(), "You are now RSVP'ed for " + eventList.get(position).getEventName(),
                            Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void cardsDepleted() {
                eventSwipeDeck.setAdapter(adapter);

            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });

        profileButton = findViewById(R.id.menuButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                intent.putExtra("PAGE", TAG);
                startActivity(intent);
            }
        });


        // Bottom Navigation set for Home (student view)
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.homeNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
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
                        startActivity(new Intent(getApplicationContext(), StudentFollowingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.homeNavButton:
                        return true;
                }
                return false;
            }
        });

    }

    public boolean displayEventData () {
        ArrayList<Event> allEvents = db.getEventInfo();
        if (!allEvents.isEmpty()) {
            eventList.clear();
            for (int i = 0; i < allEvents.size(); i++) {
                if (allEvents.get(i).getEventIsApproved() > 0) {
                    eventList.add(allEvents.get(i));
                }
            }
            return true;
        } else {
            return false;
        }
    }
}