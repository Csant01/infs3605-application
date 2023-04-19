package com.example.infs3605_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminEventApprovalsDetail extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView eventName, eventCat, eventDate, eventStartTime, eventEndTime, eventLoc,
            eventCity, eventCountry, eventOrg, eventDesc, approveText, rejectText;
    ImageView eventImage;
    LinearLayout layout;
    DatabaseConnector db;
    Event event;
    byte[] bytes;
    String fromLocation;
    ImageButton approveButton, rejectButton;
    private static final String TAG = "AdminEventDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_approvals_detail);
        db = new DatabaseConnector(this);
        fromLocation = getIntent().getStringExtra("EVENT_ID");
        Log.d(TAG, "Event ID: " + fromLocation);

        event = db.getEventById(fromLocation);
        eventImage = findViewById(R.id.displayEventImageApprovals);
        eventName = findViewById(R.id.displayEventNameApprovals);
        eventCat = findViewById(R.id.displayEventCategoryApprovals);
        eventOrg = findViewById(R.id.displayEventOrganiserApprovals);
        eventDate = findViewById(R.id.displayEventDateApprovals);
        eventStartTime = findViewById(R.id.displayEventStartTimeApprovals);
        eventEndTime = findViewById(R.id.displayEventEndTimeApprovals);
        eventLoc = findViewById(R.id.displayEventLocationApprovals);
        eventCity = findViewById(R.id.displayEventCityApprovals);
        eventCountry = findViewById(R.id.displayEventCountryApprovals);
        eventDesc = findViewById(R.id.displayEventDescriptionApprovals);
        approveButton = findViewById(R.id.adminEventApproveButton);
        rejectButton = findViewById(R.id.adminEventRejectButton);
        approveText = findViewById(R.id.approveButtonText);
        rejectText = findViewById(R.id.rejectButtonText);
        layout = findViewById(R.id.adminEventApprovalLinearLayout);

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

        if (event.getEventIsApproved() == 1) {
            approveButton.setVisibility(View.GONE);
            approveText.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rejectButton.getLayoutParams();
            params.weight = 1.0f;
            rejectButton.setLayoutParams(params);
            rejectButton.setVisibility(View.VISIBLE);
            rejectText.setVisibility(View.VISIBLE);
        } else if (event.getEventIsApproved() == -1) {
            rejectButton.setVisibility(View.GONE);
            rejectText.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) approveButton.getLayoutParams();
            params.weight = 1.0f;
            approveButton.setLayoutParams(params);
            approveButton.setVisibility(View.VISIBLE);
            approveText.setVisibility(View.VISIBLE);
        }

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.setEventApproval(fromLocation);
                Toast.makeText(getApplicationContext(), event.getEventName() + " approved.", Toast.LENGTH_SHORT).show();
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.rejectEvent(fromLocation);
                Toast.makeText(getApplicationContext(), event.getEventName() + " rejected.", Toast.LENGTH_SHORT).show();
            }
        });

        bytes = db.retrieveEventImageFromDatabaseFiltered(fromLocation);
        if (bytes == null) {
            bytes = db.retrieveEventImageDirect(fromLocation);
            eventImage.setImageBitmap(ImageUtils.getImage(bytes));
        }  else {
            eventImage.setImageBitmap(ImageUtils.getImage(bytes));
        }



    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value));
        return date;
    }
}