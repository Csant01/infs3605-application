package com.example.infs3605_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreateEventActivity";
    private static final int SELECT_PICTURE = 100;

    TextView eventName, eventDate, eventDescription, eventLocation, eventCity,
                eventCountry, eventFacility, predictedAttn, eventCost, eventStartTime, eventEndTime,
                eventCatering, eventStaffing;
    String eventId;
    AutoCompleteTextView eventCategory;
    ArrayList<String> eventCategories;
    ArrayAdapter<String> categoryAdapter;
    Button submitEvent, ticketedYes, ticketedNo;
    BottomNavigationView bottomNavigationView;
    int checkTicketed = 0;
    DatabaseConnector db;
    ArrayList<TextView> uiArrayList;
    ImageView eventImage, profileButton;
    FloatingActionButton selectImage;
    byte[] blob;

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
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);

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

        eventImage = findViewById(R.id.createEventImage);
        selectImage = findViewById(R.id.createImageButton);
        selectImage.setOnClickListener(this);
        uiArrayList = new ArrayList<>();

        categoryAdapter = new ArrayAdapter<>(this, R.layout.dropdown_list, eventCategories);
        eventCategory.setAdapter(categoryAdapter);


        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Create New Event");
        setSupportActionBar(toolbar);

        profileButton = findViewById(R.id.menuButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrganiserProfileActivity.class);
                intent.putExtra("PAGE", TAG);
                startActivity(intent);
            }
        });

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
                try {
                    if (emptyFields.isEmpty() && regexCheck(eventDate.getText().toString(), 0)
                            && regexCheck(eventEndTime.getText().toString(), 1)
                            && regexCheck(eventStartTime.getText().toString(), 1)
                            && dateCheck(eventDate.getText().toString())
                            && blob != null ) {
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
                        String eventId = createEventID();


                        Event event = new Event(eventId, eventNameString, eventLocationString,
                                eventDescriptionString, db.getUserId(user),
                                eventCountryString, eventCityString, eventCategoryString, predictedAttnInt,
                                0, eventCostString,checkTicketed,null,formatEpoch(eventDateString), eventStartTimeString,
                                eventEndTimeString, 0, 0, eventStaffingInt, eventFacilityString);
                        db.addEventToDatabase(event);
                        db.assignEventIdToImage(eventId, blob);

                        Toast.makeText(CreateEventActivity.this, "Event has been forwarded to staff for approval.",
                                Toast.LENGTH_SHORT).show();

                    } else if (!emptyFields.isEmpty()){
                        for (int i = 0; i < emptyFields.size(); i++) {
                            emptyFields.get(i).setError("This is a required field");
                            Toast.makeText(CreateEventActivity.this, "Please fill in all required fields.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else if (!regexCheck(eventDate.getText().toString(), 0)) {
                        eventDate.setError("Please ensure format is in dd/mm/yyyy");
                        Toast.makeText(CreateEventActivity.this, "Please ensure date & time fields are" +
                                        " formatted correctly.", Toast.LENGTH_SHORT).show();
                    } else if (!regexCheck(eventEndTime.getText().toString(), 1)
                            || !regexCheck(eventStartTime.getText().toString(), 1)) {
                        eventStartTime.setError("Please ensure format is in H:mm am/pm.");
                        eventEndTime.setError("Please ensure format is in H:mm am/pm");
                        Toast.makeText(CreateEventActivity.this, "Please ensure date & time fields are" +
                                " formatted correctly.", Toast.LENGTH_SHORT).show();
                    } else if (!dateCheck(eventDate.getText().toString())) {
                        eventDate.setError("Please ensure date is not in the past.");
                        Toast.makeText(CreateEventActivity.this, "Please ensure date is not in the past.",
                                Toast.LENGTH_SHORT).show();
                    } else if (blob == null) {
                        Toast.makeText(CreateEventActivity.this, "Please ensure you add an image.",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("CreateEventActivity", "ParseException found:" + e);
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

    public void showMessage (String message) {
        Toast.makeText(CreateEventActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public String createEventID () {
        Random rand = new Random();
        int min = 1111111;
        int max = 9999999;
        int randomNumber = rand.nextInt((max - min) + 1) + min;

        return "e" + randomNumber;

    }

    private long formatEpoch (String date) throws ParseException {
        long epoch = new SimpleDateFormat("dd/MM/yyyy").parse(date).getTime();
        return epoch;
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

    private boolean regexCheck (String input, int check) {
        if (check == 0) {
            String DATE_PATTERN = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/((19|20)\\d\\d)$";
            Pattern datePattern = Pattern.compile(DATE_PATTERN);
            return datePattern.matcher(input).matches();
        } else {
            String TIME_PATTERN = "^([1-9]|1[012]):[0-5][0-9](am|pm)$";
            Pattern timePattern = Pattern.compile(TIME_PATTERN);
            return timePattern.matcher(input).matches();
        }
    }

    private boolean dateCheck (String inputDate) throws ParseException {
        Calendar currentCalendar = Calendar.getInstance();

        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

        String currentDateString = String.format("%02d/%02d/%04d", currentDay, currentMonth, currentYear);
        System.out.println("Current date: " + currentDateString);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date inputDateObject = dateFormat.parse(inputDate);

        Calendar inputCalendar = Calendar.getInstance();
        inputCalendar.setTime(inputDateObject);

        if (inputCalendar.after(currentCalendar)) {
            return true;
        } else if (inputCalendar.before(currentCalendar)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES};
        if(hasStoragePermission(CreateEventActivity.this)) {
            openImageChooser();
        } else {
            ActivityCompat.requestPermissions(((AppCompatActivity) CreateEventActivity.this), permissions, 200);
            Log.d(TAG, "Permission should open now.");
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Your Image"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectImageUri = data.getData();
                if (selectImageUri != null) {
                    if (saveImageInDatabase(selectImageUri)) {
                        showMessage("Image Saved.");
                        eventImage.setImageURI(selectImageUri);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                eventImage.setVisibility(View.GONE);
                            }
                        }, 2000);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loadImageFromDatabase()) {
                                eventImage.setVisibility(View.VISIBLE);
                            }
                        }
                    }, 3500);
                }
            }
        }
    }

    private boolean loadImageFromDatabase() {
        try {
            db.open();
            byte[] bytes = db.retrieveEventImageFromDatabase();
            eventImage.setImageBitmap(ImageUtils.getImage(bytes));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean saveImageInDatabase(Uri selectImageUri) {
        try {
            db.open();
            InputStream stream = getContentResolver().openInputStream(selectImageUri);
            byte[] inputData = ImageUtils.getBytes(stream);
            blob = inputData;
            db.insertEventImage(inputData, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasStoragePermission(Context context) {
        int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES);
        return read == PackageManager.PERMISSION_GRANTED;
    }
}