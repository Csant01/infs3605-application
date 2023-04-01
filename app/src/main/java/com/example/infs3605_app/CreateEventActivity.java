package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class CreateEventActivity extends AppCompatActivity {

    TextView eventName, eventDate, eventDescription, eventLocation, eventCity,
                eventCountry, eventFacility, predictedAttn, eventCost, eventStartTime, eventEndTime,
                eventCatering, eventStaffing;
    AutoCompleteTextView eventCategory;
    ArrayList<String> eventCategories;
    ArrayAdapter<String> categoryAdapter;
    Button submitEvent, ticketedYes, ticketedNo;
    BottomNavigationView bottomNavigationView;
    int checkTicketed = 0;
    DatabaseConnector db;
    ArrayList<TextView> uiArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        db = new DatabaseConnector(this);
        eventCategories = new ArrayList<>();
        eventCategories.add("Network");
        eventCategories.add("Careers");
        eventCategories.add("Social");
        eventCategories.add("Travel");

        eventName = findViewById(R.id.eventNameTextBox);
        eventDescription = findViewById(R.id.eventDescriptionTextBox);
        eventLocation = findViewById(R.id.eventLocationTextBox);
        eventCity = findViewById(R.id.eventCityTextBox);
        eventCountry = findViewById(R.id.eventCountryTextBox);
        eventFacility = findViewById(R.id.eventFacilityTextBox);
        predictedAttn = findViewById(R.id.eventAttendanceTextBox);
        eventCost = findViewById(R.id.eventCosting);
        ticketedYes = findViewById(R.id.yesTicketedButton);
        ticketedNo = findViewById(R.id.noTicketedButton);
        eventStartTime = findViewById(R.id.startTimeTextBox);
        eventEndTime = findViewById(R.id.endTimeTextBox);
        eventCatering = findViewById(R.id.cateringTextBox);
        eventStaffing = findViewById(R.id.eventStaffingTextBox);
        submitEvent = findViewById(R.id.eventFormSubmitButton);
        eventCategory = findViewById(R.id.eventCategoryDropdownSelector);
        eventDate = findViewById(R.id.eventDateTextBox);

        uiArrayList = new ArrayList<>();

        categoryAdapter = new ArrayAdapter<>(this, R.layout.dropdown_list, eventCategories);
        eventCategory.setAdapter(categoryAdapter);


        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Create New Event");
        setSupportActionBar(toolbar);

        ticketedYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTicketed = 1;
            }
        });

        ticketedNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTicketed = 0;
            }
        });

        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiArrayList.add(eventName);
                uiArrayList.add(eventDescription);
                uiArrayList.add(eventLocation);
                uiArrayList.add(eventCity);
                uiArrayList.add(eventCountry);
                uiArrayList.add(eventFacility);
                uiArrayList.add(predictedAttn);
                uiArrayList.add(eventCost);
                uiArrayList.add(eventStartTime);
                uiArrayList.add(eventEndTime);
                uiArrayList.add(eventCatering);
                uiArrayList.add(eventStaffing);
                uiArrayList.add(eventDate);

                ArrayList <TextView> emptyFields = checkInputs(uiArrayList);

                if (emptyFields.isEmpty()) {
                    String eventNameString = eventName.getText().toString();
                    String eventDescriptionString= eventDescription.getText().toString();
                    String eventLocationString= eventLocation.getText().toString();
                    String eventCityString= eventCity.getText().toString();
                    String eventCountryString= eventCountry.getText().toString();
                    String eventFacilityString= eventFacility.getText().toString();
                    int predictedAttnInt= Integer.parseInt(predictedAttn.getText().toString());
                    double eventCostString= Double.parseDouble(eventCost.getText().toString());;
                    String eventStartTimeString= eventStartTime.getText().toString();
                    String eventEndTimeString= eventEndTime.getText().toString();
                    String eventCateringString= eventCatering.getText().toString();
                    int eventStaffingInt = Integer.parseInt(eventStaffing.getText().toString());
                    String eventCategoryString= eventCategory.getText().toString();
                    String eventDateString= eventDate.getText().toString();

                    Event event = new Event(createEventID(), eventNameString, eventLocationString,
                            eventDescriptionString, User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1),
                            eventCountryString, eventCityString, eventCategoryString, predictedAttnInt,
                            0, eventCostString,checkTicketed,null,formatEpoch(eventDateString), eventStartTimeString,
                            eventEndTimeString, 0, 0, eventStaffingInt, eventFacilityString);
                    db.addEventToDatabase(event);

                    Toast.makeText(CreateEventActivity.this, "Event has been forwarded to staff for approval.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < emptyFields.size(); i++) {
                        emptyFields.get(i).setError("This is a required field");
                    }
                }

            }
        });



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

    public String createEventID () {
        Random rand = new Random();
        int min = 1111111;
        int max = 9999999;
        int randomNumber = rand.nextInt((max - min) + 1) + min;

        return "e" + randomNumber;

    }

    private boolean checkEmpty (TextView text) {
        return text.getText().toString().trim().length() > 0;
    }

    private void fillFieldHint (TextView text) {
        text.setHint("Please fill in this field");
        text.setHintTextColor(Color.RED);
    }

    private long formatEpoch (String date) {
        try {
            long epoch = new SimpleDateFormat("dd/MM/yyyy").parse(date).getTime();
            return epoch;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    private ArrayList<TextView> checkInputs (ArrayList<TextView> textList) {
        ArrayList<TextView> emptyText = new ArrayList<>();
        for (int i = 0; i < textList.size(); i++) {
            if (textList.get(i).getText().toString().trim().length() == 0) {
                emptyText.add(textList.get(i));
            }
        }
        return emptyText;
    }
}