package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StudentFeedbackFormActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView feedbackFormImage, profileButton;
    TextView eventName, eventOrg, eventDate;
    RadioGroup satisfied, likelyToAttend, usefulness, overallRating;
    TextView additionalComments;
    RadioButton q11, q12, q13, q14, q15, q21, q22, q23, q24, q25, q31, q32, q33, q34, q35, q41,
            q42, q43, q44, q45;
    Button submitFeedback;
    DatabaseConnector db;
    Event event;
    byte[] bytes;
    private static final String TAG = "StudentFeedbackFormActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_feedback_form);
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        String eventId = getIntent().getStringExtra("EVENT_ID");
        Log.d(TAG, "eventId: " + eventId);
        db = new DatabaseConnector(this);
        Toolbar toolbar = findViewById(R.id.toolbar);


        feedbackFormImage = findViewById(R.id.feedbackFormImage);
        eventName = findViewById(R.id.eventNameFormPrint);
        eventOrg = findViewById(R.id.eventOrganiserFormPrint);
        eventDate = findViewById(R.id.eventDateFormPrint);
        satisfied = findViewById(R.id.radioGroupQuestionOne);
        likelyToAttend = findViewById(R.id.radioGroupQuestionTwo);
        usefulness = findViewById(R.id.radioGroupQuestionThree);
        overallRating = findViewById(R.id.radioGroupQuestionFour);
        additionalComments = findViewById(R.id.feedbackAdditional);
        submitFeedback = findViewById(R.id.submitFeedbackFormButton);

        ArrayList<Event> allEvents = db.getEventInfo();
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getEventId().equals(eventId)) {
                event = allEvents.get(i);
            }
        }

        bytes = db.retrieveEventImageFromDatabaseFiltered(eventId);
        if (bytes == null) {
            bytes = db.retrieveEventImageDirect(eventId);
        }

        eventName.setText(event.getEventName());
        eventOrg.setText(db.getUserName(event.getEventOwner()));
        eventDate.setText(formatEpoch(event.getEventDate()));
        feedbackFormImage.setImageBitmap(ImageUtils.getImage(bytes));

        setTitle(event.getEventName() + " Feedback");
        setSupportActionBar(toolbar);

        q11 = findViewById(R.id.questionOneOne);
        q12 = findViewById(R.id.questionOneTwo);
        q13 = findViewById(R.id.questionOneThree);
        q14 = findViewById(R.id.questionOneFour);
        q15 = findViewById(R.id.questionOneFive);
        q21 = findViewById(R.id.questionTwoOne);
        q22 = findViewById(R.id.questionTwoTwo);
        q23 = findViewById(R.id.questionTwoThree);
        q24 = findViewById(R.id.questionTwoFour);
        q25 = findViewById(R.id.questionTwoFive);
        q31 = findViewById(R.id.questionThreeOne);
        q32 = findViewById(R.id.questionThreeTwo);
        q33 = findViewById(R.id.questionThreeThree);
        q34 = findViewById(R.id.questionThreeFour);
        q35 = findViewById(R.id.questionThreeFive);
        q41 = findViewById(R.id.questionFourOne);
        q42 = findViewById(R.id.questionFourTwo);
        q43 = findViewById(R.id.questionFourThree);
        q44 = findViewById(R.id.questionFourFour);
        q45 = findViewById(R.id.questionFourFive);

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId1 = satisfied.getCheckedRadioButtonId();
                int selectedId2 = likelyToAttend.getCheckedRadioButtonId();
                int selectedId3 = usefulness.getCheckedRadioButtonId();
                int selectedId4 = overallRating.getCheckedRadioButtonId();

                // Check that at least one radio button is selected in each group
                boolean allGroupsSelected = true;
                if (selectedId1 == -1) {
                    allGroupsSelected = false;
                    Toast.makeText(getApplicationContext(), "Please select an answer for Question 1", Toast.LENGTH_SHORT).show();
                }
                if (selectedId2 == -1) {
                    allGroupsSelected = false;
                    Toast.makeText(getApplicationContext(), "Please select an answer for Question 2", Toast.LENGTH_SHORT).show();
                }
                if (selectedId3 == -1) {
                    allGroupsSelected = false;
                    Toast.makeText(getApplicationContext(), "Please select an answer for Question 3", Toast.LENGTH_SHORT).show();
                }
                if (selectedId4 == -1) {
                    allGroupsSelected = false;
                    Toast.makeText(getApplicationContext(), "Please select an answer for Question 4", Toast.LENGTH_SHORT).show();
                }

                if (allGroupsSelected) {
                    RadioButton selected1 = findViewById(selectedId1);
                    RadioButton selected2 = findViewById(selectedId2);
                    RadioButton selected3 = findViewById(selectedId3);
                    RadioButton selected4 = findViewById(selectedId4);

                    int answer1 = Integer.parseInt(selected1.getText().toString());
                    int answer2 = Integer.parseInt(selected2.getText().toString());
                    int answer3 = Integer.parseInt(selected3.getText().toString());
                    int answer4 = Integer.parseInt(selected4.getText().toString());
                    String additional = additionalComments.getText().toString();

                    EventFeedback feedback = new EventFeedback(answer1, answer2, answer3, answer4, additional, db.getUserId(user), eventId);
                    db.submitFeedback(user, eventId, feedback);
                    Toast.makeText(getApplicationContext(), "Feedback submitted for " + event.getEventName(), Toast.LENGTH_SHORT).show();

                }
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

        // Bottom Navigation set for Past Events (student view)
        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.pastEventsNavButton);

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
        Instant instant = Instant.ofEpochMilli(value);
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("d' 'MMM' 'uuuu"));
        return formattedDate;
    }


}