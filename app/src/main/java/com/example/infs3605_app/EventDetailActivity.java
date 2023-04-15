package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventDetailActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView eventName, eventCat, eventDate, eventStartTime, eventEndTime, eventLoc,
            eventCity, eventCountry, eventOrg, eventDesc;
    ImageView eventImage;
    DatabaseConnector db;
    Event event;
    byte[] bytes;
    String fromLocation;
    private static final String TAG = "EventDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        db = new DatabaseConnector(this);
        String eventId = getIntent().getStringExtra("EVENT_ID");
        ArrayList<Event> allEvents = db.getEventInfo();
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getEventId().equals(eventId)) {
                event = allEvents.get(i);
            }
        }

        // UI elements
        eventName = findViewById(R.id.displayEventName);
        eventCat = findViewById(R.id.displayEventCategory);
        eventDate = findViewById(R.id.displayEventDate);
        eventStartTime = findViewById(R.id.displayEventStartTime);
        eventEndTime = findViewById(R.id.displayEventEndTime);
        eventLoc = findViewById(R.id.displayEventLocation);
        eventCity = findViewById(R.id.displayEventCity);
        eventCountry = findViewById(R.id.displayEventCountry);
        eventOrg = findViewById(R.id.displayEventOrganiser);
        eventDesc = findViewById(R.id.displayEventDescription);
        eventImage = findViewById(R.id.displayEventImage);

        eventName.setText(event.getEventName());
        eventCat.setText(event.getEventCategory());
        eventDate.setText(formatEpoch(event.getEventDate()));
        eventStartTime.setText(event.getEventStartTime());
        eventEndTime.setText(event.getEventEndTime());
        eventLoc.setText(event.getEventLocation());
        eventCity.setText(event.getEventCity());
        eventCountry.setText(event.getEventCountry());
        eventOrg.setText(db.getUserName(event.getEventOwner()));
        eventDesc.setText(event.getEventDescription());

        bytes = db.retrieveEventImageFromDatabaseFiltered(eventId);
        if (bytes == null) {
            bytes = db.retrieveEventImageDirect(eventId);
            eventImage.setImageBitmap(ImageUtils.getImage(bytes));
        }  else {
            eventImage.setImageBitmap(ImageUtils.getImage(bytes));
        }


        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(event.getEventName());
        setSupportActionBar(toolbar);

        eventOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrganiserPublicProfileActivity.class);
                intent.putExtra("ORGANISER_NAME", event.getEventOwner());
                intent.putExtra("EVENT_ID", event.getEventId());
                intent.putExtra("USER_TYPE", "student");
                intent.putExtra("PAGE", "EventDetail");
                startActivity(intent);
            }
        });

        // Bottom Navigation set for Events Detail Page
        fromLocation = getIntent().getStringExtra("PAGE");
        Log.d(TAG, "fromLocation: " + fromLocation);
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.eventsNavButton);
        if (fromLocation.equals("StudentHomePage")) {
            bottomNavigationView.setSelectedItemId(R.id.homeNavButton);
        } else if (fromLocation.equals("StudentPastEvents")) {
            bottomNavigationView.setSelectedItemId(R.id.pastEventsNavButton);
        } else if (fromLocation.equals("StudentSavedEvents")) {
            bottomNavigationView.setSelectedItemId(R.id.savedNavButton);
        } else if (fromLocation.equals("OrganiserPublicProfile")) {
            bottomNavigationView.setSelectedItemId(R.id.homeNavButton);
        } else if (fromLocation.equals("StudentAllEvents")) {
            bottomNavigationView.setSelectedItemId(R.id.allEventsNavButton);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.homeNavButton);
        }


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

                    case R.id.followingNavButton:
                        startActivity(new Intent(getApplicationContext(), StudentFollowingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.pastEventsNavButton:
                        return true;
                }
                return false;
            }
        });
    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value));
        return date;
    }
}