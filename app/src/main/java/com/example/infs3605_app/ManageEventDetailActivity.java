package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManageEventDetailActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView eventName, eventDescription, eventLocation, eventCity, eventCountry, eventFacility,
            eventPredAttn, eventCost, eventDate, eventStartTime, eventEndTime, eventStaffing;
    RadioButton ticketedYes, ticketedNo;
    Button updateEventDetails;
    String eventFromIntent;
    AutoCompleteTextView eventCategory;
    ImageView backButton, profileButton;
    DatabaseConnector db;
    Event event;
    RadioGroup ticketedRadioGroup;
    private static final String TAG = "ManageEventDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        eventName = findViewById(R.id.editEventName);
        eventDescription = findViewById(R.id.editEventDesc);
        eventLocation = findViewById(R.id.editEventLocation);
        eventCity = findViewById(R.id.editEventCity);
        eventCountry = findViewById(R.id.editEventCountry);
        eventFacility = findViewById(R.id.editEventFacility);
        eventCategory = findViewById(R.id.editCategoryDropdownSelector);
        eventPredAttn = findViewById(R.id.editPredictedAttendance);
        eventCost = findViewById(R.id.editEstimatedCost);
        ticketedYes = findViewById(R.id.editYesTicketedButton);
        ticketedNo = findViewById(R.id.editNoTicktedButton);
        eventDate = findViewById(R.id.editEventDate);
        eventStartTime = findViewById(R.id.editEventStartTime);
        eventEndTime = findViewById(R.id.editEventEndTime);
        eventStaffing = findViewById(R.id.editStaffingNumbers);
        updateEventDetails = findViewById(R.id.updateEventDetailsButton);
        ticketedRadioGroup = findViewById(R.id.linearLayout2);
        eventFromIntent = getIntent().getStringExtra("EVENT_ID");

        db = new DatabaseConnector(this);
        ArrayList<Event> allEvents = db.getEventInfo();
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getEventId().equals(eventFromIntent)) {
                event = allEvents.get(i);
            }
        }

        if (!event.getEventOwner().equals(db.getUserId(user))) {
            eventName.setFocusable(false);
            eventName.setFocusableInTouchMode(false);
            eventDescription.setFocusable(false);
            eventDescription.setFocusableInTouchMode(false);
            eventLocation.setFocusable(false);
            eventLocation.setFocusableInTouchMode(false);
            eventCity.setFocusable(false);
            eventCity.setFocusableInTouchMode(false);
            eventCountry.setFocusable(false);
            eventCountry.setFocusableInTouchMode(false);
            eventFacility.setFocusable(false);
            eventFacility.setFocusableInTouchMode(false);
            eventCategory.setFocusable(false);
            eventCategory.setFocusableInTouchMode(false);
            eventPredAttn.setFocusable(false);
            eventPredAttn.setFocusableInTouchMode(false);
            eventCost.setFocusable(false);
            eventCost.setFocusableInTouchMode(false);
            ticketedYes.setFocusable(false);
            ticketedYes.setFocusableInTouchMode(false);
            ticketedNo.setFocusable(false);
            ticketedNo.setFocusableInTouchMode(false);
            eventDate.setFocusable(false);
            eventDate.setFocusableInTouchMode(false);
            eventStartTime.setFocusable(false);
            eventStartTime.setFocusableInTouchMode(false);
            eventEndTime.setFocusable(false);
            eventEndTime.setFocusableInTouchMode(false);
            eventStaffing.setFocusable(false);
            eventStaffing.setFocusableInTouchMode(false);
            updateEventDetails.setVisibility(View.GONE);
        }

        eventName.setText(event.getEventName());
        eventDescription.setText(event.getEventDescription());
        eventLocation.setText(event.getEventLocation());
        eventCity.setText(event.getEventCity());
        eventCountry.setText(event.getEventCountry());
        eventFacility.setText(event.getEventFacility());
        eventCategory.setText(event.getEventCategory());
        eventPredAttn.setText(String.valueOf(event.getEventPredAttn()));
        eventCost.setText(String.valueOf(event.getEventCost()));
        eventDate.setText(formatEpoch(event.getEventDate()));
        eventStartTime.setText(event.getEventStartTime());
        eventEndTime.setText(event.getEventEndTime());
        eventStaffing.setText(String.valueOf(event.getEventStaffing()));
        if (event.getEventTicketed() == 1) {
            ticketedRadioGroup.check(R.id.editYesTicketedButton);
        } else {
            ticketedRadioGroup.check(R.id.editNoTicktedButton);
        }

        setTitle(event.getEventName());
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StaffAllEventsActivity.class));
            }
        });

        profileButton = findViewById(R.id.menuButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrganiserProfileActivity.class);
                intent.putExtra("PAGE", TAG);
                startActivity(intent);
            }
        });

        updateEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = eventName.getText().toString().trim();
                String updatedDesc = eventDescription.getText().toString().trim();
                String updatedLoc = eventLocation.getText().toString().trim();
                String updatedCity = eventCity.getText().toString().trim();
                String updatedCountry = eventCountry.getText().toString().trim();
                String updatedFacility = eventFacility.getText().toString().trim();
                String updatedCategory = eventCategory.getText().toString().trim();
                int updatedPredAttn = Integer.parseInt(eventPredAttn.getText().toString().trim());
                double updatedCost = Double.parseDouble(eventCost.getText().toString().trim());
                int updatedTicketed = ticketedYes.isChecked() ? 1 : 0;
                String updatedStartTime = eventStartTime.getText().toString().trim();
                String updatedEndTime = eventEndTime.getText().toString().trim();
                int updatedStaffing = Integer.parseInt(eventStaffing.getText().toString().trim());

                event.setEventName(updatedName);
                event.setEventDescription(updatedDesc);
                event.setEventLocation(updatedLoc);
                event.setEventCity(updatedCity);
                event.setEventCountry(updatedCountry);
                event.setEventFacility(updatedFacility);
                event.setEventCategory(updatedCategory);
                event.setEventPredAttn(updatedPredAttn);
                event.setEventCost(updatedCost);
                event.setEventTicketed(updatedTicketed);
                event.setEventStartTime(updatedStartTime);
                event.setEventEndTime(updatedEndTime);
                event.setEventStaffing(updatedStaffing);

                db.updateEvent(event);
                Toast.makeText(getApplicationContext(), "Event details updated for " + event.getEventName()
                        , Toast.LENGTH_SHORT).show();
            }
        });




        // Bottom Navigation set for Manage Events Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.eventsNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.dashboardNavButton:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
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

                    case R.id.eventsNavButton:
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