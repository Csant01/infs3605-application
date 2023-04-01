package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class CreateEventActivity extends AppCompatActivity {

    TextView eventName, eventDate, eventTime, eventDescription, eventLocation, eventCity,
                eventCountry, eventFacility, predictedAttn, eventCost, eventStartTime, eventEndTime,
                eventCatering, eventStaffing;
    AutoCompleteTextView eventCategory;
    ArrayList<String> eventCategories;
    ArrayAdapter<String> categoryAdapter;
    Button submitEvent, ticketedYes, ticketedNo;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventCategories = new ArrayList<>();
        eventCategories.add("Network");
        eventCategories.add("Careers");
        eventCategories.add("Social");
        eventCategories.add("Travel");

        eventName = findViewById(R.id.editEventNameTextBox);
        eventDescription = findViewById(R.id.editEventDescriptionTextBox);
        eventLocation = findViewById(R.id.editEventLocationTextBox);
        eventCity = findViewById(R.id.editEventCityTextBox);
        eventCountry = findViewById(R.id.editEventCountryTextBox);
        eventFacility = findViewById(R.id.editEventFacilityTextBox);
        predictedAttn = findViewById(R.id.editEventAttendanceTextBox);
        eventCost = findViewById(R.id.editEventCosting);
        ticketedYes = findViewById(R.id.editYesTicketedButton);
        ticketedNo = findViewById(R.id.editNoTicketedButton);
        eventStartTime = findViewById(R.id.editStartTimeTextBox);
        eventEndTime = findViewById(R.id.editEndTimeTextBox);
        eventCatering = findViewById(R.id.editCateringTextBox);
        eventStaffing = findViewById(R.id.editEventStaffingTextBox);
        submitEvent = findViewById(R.id.updatedEventFormSubmitButton);
        eventCategory = findViewById(R.id.editEventCategoryDropdownSelector);

        categoryAdapter = new ArrayAdapter<>(this, R.layout.dropdown_list, eventCategories);
        eventCategory.setAdapter(categoryAdapter);


        // Bottom Navigation set for Create Event Page
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.addEventNavButton);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.dashboardNavButton:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.eventsNavButton:
                        startActivity(new Intent(getApplicationContext(), StaffAllEventsActivity.class));
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

                    case R.id.addEventNavButton:
                        return true;
                }
                return false;
            }
        });


    }
}