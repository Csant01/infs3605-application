package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrganiserPublicProfileActivity extends AppCompatActivity implements OrganiserProfileEventsAdapter.OnOrganiserEventClickListener {

    TextView organiserName, organiserFaculty, organiserType, clearFilter;
    RecyclerView organiserRv;
    ImageView organiserPicture;
    ImageButton pastEvents;
    OrganiserProfileEventsAdapter adapter;
    DatabaseConnector db;
    List<Event> eventList;
    long epochSeconds;
    String eventOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_public_profile);
        organiserName = findViewById(R.id.organiserNamePrint);
        organiserFaculty = findViewById(R.id.facultyPrint);
        organiserType = findViewById(R.id.organiserTypePrint);
        organiserPicture = findViewById(R.id.organiserImageView);
        pastEvents = findViewById(R.id.pastEventsButton);
        Date currentDate = new Date();
        long epochMillis = currentDate.getTime();
        epochSeconds = epochMillis / 1000L;
        db = new DatabaseConnector(this);
        eventOwner = getIntent().getStringExtra("ORGANISER_NAME");

        // Need Ariane to finalise form for Alumni/Organiser signup to assign fields.
        eventList = new ArrayList<>();
        organiserRv = findViewById(R.id.organiserEventsRv);
        adapter = new OrganiserProfileEventsAdapter(this, eventList, this);
        organiserRv.setAdapter(adapter);
        organiserRv.setLayoutManager(new LinearLayoutManager(this));
        if (!displayPastEventData(eventOwner)) {
            Toast.makeText(OrganiserPublicProfileActivity.this, "No events " +
                            " for this organiser.",
                    Toast.LENGTH_SHORT).show();
        }

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
                if (allEvents.get(i).getEventIsApproved() != 0
                        && allEvents.get(i).getEventOwner().equals(organiser)
                        && allEvents.get(i).getEventDate() >= epochSeconds
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

    }
}