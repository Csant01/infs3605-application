package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrganiserPublicProfileActivity extends AppCompatActivity implements OrganiserProfileEventsAdapter.OnOrganiserEventClickListener {

    TextView organiserName, organiserFaculty, organiserType, clearFilter;
    RecyclerView organiserRv;
    ImageView organiserPicture;
    ImageButton pastEvents;
    Button followButton;
    OrganiserProfileEventsAdapter adapter;
    DatabaseConnector db;
    List<Event> eventList;
    long epochSeconds;
    String eventId;
    String eventOwner;
    byte[] bytes;
    String eventOwnerId;
    String intentOrgName;
    User user;
    String followingCheck;
    boolean bool;
    private static final String TAG = "OrganiserPublicProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_public_profile);

        String loggedIn = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        organiserName = findViewById(R.id.organiserNamePrint);
        organiserFaculty = findViewById(R.id.organiserCountry);
        organiserType = findViewById(R.id.organiserTypePrint);
        organiserPicture = findViewById(R.id.organiserImageView);
        pastEvents = findViewById(R.id.pastEventsButton);
        clearFilter = findViewById(R.id.organiserClearFilter);
        followButton = findViewById(R.id.organiserProfileFollowButton);
        Date currentDate = new Date();
        long epochMillis = currentDate.getTime();
        epochSeconds = epochMillis / 1000L;
        db = new DatabaseConnector(this);
        eventId = getIntent().getStringExtra("EVENT_ID");
        intentOrgName = getIntent().getStringExtra("ORGANISER_ID");
        followingCheck = getIntent().getStringExtra("FOLLOWING_CHECK");
        ArrayList<Event> allEvents = db.getEventInfo();
        ArrayList<User> allUsers = db.getUserInfo();

        if (eventId != null) {
            for (int i = 0; i < allEvents.size(); i++) {
                if (allEvents.get(i).getEventId().equals(eventId)) {
                    eventOwner = allEvents.get(i).getEventOwner();
                }
            }


            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getUserName().equals(eventOwner)) {
                    eventOwnerId = allUsers.get(i).getUserID();
                    user = allUsers.get(i);
                }
            }
        } else {
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getUserName().equals(intentOrgName)) {
                    eventOwnerId = allUsers.get(i).getUserID();
                    eventOwner = intentOrgName;
                    user = allUsers.get(i);
                }
            }
        }

        bytes = db.retrieveOrganiserImageFromDatabaseFiltered(eventOwnerId);
        organiserPicture.setImageBitmap(ImageUtils.getImage(bytes));
        organiserName.setText(eventOwner);
        organiserType.setText(convertTypeToString(Integer.parseInt(user.getUserType())));
        organiserFaculty.setText(user.getUserFaculty());

        // Need Ariane to finalise form for Alumni/Organiser signup to assign fields.
        eventList = new ArrayList<>();
        organiserRv = findViewById(R.id.organiserEventsRv);
        adapter = new OrganiserProfileEventsAdapter(this, eventList, this);
        organiserRv.setAdapter(adapter);
        organiserRv.setLayoutManager(new LinearLayoutManager(this));
        displayFutureEventData(eventOwner);

        pastEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPastEventData(eventOwner);
            }
        });

        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFutureEventData(eventOwner);
            }
        });

        bool = db.checkFollowing(loggedIn, eventOwnerId);

        if (bool) {
            followButton.setText("UNFOLLOW");
        } else {
            followButton.setText("FOLLOW");
        }
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Button clicked");
                bool = db.checkFollowing(loggedIn, eventOwnerId);
                Log.d(TAG, "onClick: Current value of bool: " + bool);
                if (bool) {
                    db.unsetUserFollowing(loggedIn, eventOwnerId);
                    followButton.setText("FOLLOW");
                    Log.d(TAG, "unsetUserFollowing: " + bool);
                    Toast.makeText(getApplicationContext(), "Unfollowed " + eventOwner,
                            Toast.LENGTH_SHORT).show();
                    bool = false; // Update the value of bool
                } else {
                    db.setUserFollowing(loggedIn, eventOwnerId);
                    followButton.setText("UNFOLLOW");
                    Toast.makeText(getApplicationContext(), "Followed " + eventOwner,
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "setUserFollowing: " + bool);
                    bool = true;
                }
            }
        });


        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Explore Profile");
        setSupportActionBar(toolbar);

    }

    public boolean displayFutureEventData (String organiser) {
        ArrayList<Event> allEvents = db.getEventInfo();
        if (!allEvents.isEmpty()) {
            organiserRv.getRecycledViewPool().clear();
            eventList.clear();
            adapter.notifyDataSetChanged();
            for (int i = 0; i < allEvents.size(); i++) {
                if (//allEvents.get(i).getEventIsApproved() != 0
                        /*&&*/ allEvents.get(i).getEventOwner().equals(organiser)
                        && allEvents.get(i).getEventDate() >= epochSeconds
                        && allEvents.get(i).getEventIsDeleted() != 1) {
                    eventList.add(allEvents.get(i));
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean displayPastEventData (String organiser) {
        ArrayList<Event> allEvents = db.getEventInfo();
        if (!allEvents.isEmpty()) {
            organiserRv.getRecycledViewPool().clear();
            eventList.clear();
            adapter.notifyDataSetChanged();
            for (int i = 0; i < allEvents.size(); i++) {
                if (allEvents.get(i).getEventOwner().equals(organiser)
                        && allEvents.get(i).getEventDate() < epochSeconds
                        && allEvents.get(i).getEventIsApproved() != 0
                        && allEvents.get(i).getEventIsDeleted() != 1) {
                    eventList.add(allEvents.get(i));
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onEventClick(int position) {
        eventList.get(position);
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("EVENT_ID", eventList.get(position).getEventId());
        intent.putExtra("USER_TYPE", "student");
        intent.putExtra("PAGE", "OrganiserPublicProfile");
        startActivity(intent);
    }

    public String convertTypeToString (int type) {
        if (type == 1) {
            return "UNSW Student Society";
        } else if (type == 2) {
            return "Partner University";
        } else if (type == 3) {
            return "Other";
        } else {
            return "UNSW Alumni";
        }
    }

}