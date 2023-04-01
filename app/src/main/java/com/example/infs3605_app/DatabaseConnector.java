package com.example.infs3605_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DatabaseConnector extends SQLiteOpenHelper {

    public DatabaseConnector(@Nullable Context context) {
        super(context, "database.db", null, 1);
    }

    // Called when database is first created.
    @Override
    public void onCreate(SQLiteDatabase db) {

        // creation of all required tables in the database.
        String createUserTable = "CREATE TABLE IF NOT EXISTS USERS " +
                "(" +
                "USER_ID TEXT PRIMARY KEY NOT NULL, " +
                "USER_NAME TEXT NOT NULL, " +
                "USER_FNAME TEXT NOT NULL, " +
                "USER_LNAME TEXT NOT NULL, " +
                "USER_GENDER TEXT NOT NULL, " +
                "USER_DOB INT, " +
                "USER_COUNTRY TEXT, " +
                "USER_CITY TEXT, " +
                "USER_PROF_IMAGE TEXT, " +
                "USER_EMAIL TEXT NOT NULL, " +
                "USER_TYPE INT NOT NULL, " +
                "USER_PASS INT NOT NULL" +
                ")";

        String createEventTable = "CREATE TABLE IF NOT EXISTS EVENTS " +
                "(" +
                "EVENT_ID TEXT PRIMARY KEY NOT NULL, " +
                "EVENT_NAME TEXT NOT NULL, " +
                "EVENT_LOC TEXT NOT NULL, " +
                "EVENT_DESC TEXT NOT NULL, " +
                "EVENT_OWNER TEXT NOT NULL, " +
                "EVENT_COUNTRY TEXT NOT NULL, " +
                "EVENT_CITY TEXT NOT NULL, " +
                "EVENT_CAT TEXT NOT NULL, " +
                "EVENT_PRED_ATTN_NUM INT DEFAULT 0, " +
                "EVENT_ACTUAL_ATTN_NUM INT DEFAULT 0, " +
                "EVENT_BUDGETED_COST REAL DEFAULT 0, " +
                "EVENT_COST_ID TEXT, " +
                "EVENT_TICKETED INT DEFAULT 0," +
                "EVENT_IMAGE TEXT, " +
                "EVENT_FACILITY TEXT, " +
                "EVENT_STAFFING INT NOT NULL, " +
                "EVENT_DATE INT NOT NULL, " +
                "EVENT_START_TIME TEXT NOT NULL, " +
                "EVENT_END_TIME TEXT NOT NULL, " +
                "EVENT_ISDELETED INT DEFAULT 0, " +
                "EVENT_ISAPPROVED INT DEFAULT 0, " +
                "FOREIGN KEY (EVENT_COST_ID) REFERENCES EVENT_COSTING(EVENT_COST_ID), " +
                "FOREIGN KEY (EVENT_OWNER) REFERENCES USERS(USER_ID)" +
                ")";


        String createUserFollowingTable = "CREATE TABLE IF NOT EXISTS USER_FOLLOWING " +
                "(" +
                "USER_ID TEXT NOT NULL, " +
                "FOLLOWING_ID TEXT NOT NULL, " +
                "FOLLOW_DATE INT NOT NULL" +
                ")";

        String createUserEventTable = "CREATE TABLE IF NOT EXISTS USER_EVENTS " +
                "(" +
                "USER_EVENT_ID TEXT PRIMARY KEY NOT NULL, " +
                "USER_FAV INT DEFAULT 0, " +
                "USER_ATTENDED INT DEFAULT 0, " +
                "USER_ID TEXT NOT NULL, " +
                "EVENT_ID TEXT NOT NULL, " +
                "USER_FEEDBACK_ID TEXT NOT NULL, " +
                "DONATION_AMOUNT REAL DEFAULT 0, " +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID), " +
                "FOREIGN KEY (EVENT_ID) REFERENCES EVENTS(EVENT_ID), " +
                "FOREIGN KEY (USER_FEEDBACK_ID) REFERENCES USER_FEEDBACK(USER_FEEDBACK_ID) " +
                ")";

        String createEventCostingTable = "CREATE TABLE IF NOT EXISTS EVENT_COSTING " +
                "(" +
                "EVENT_COST_ID TEXT PRIMARY KEY NOT NULL," +
                "FOOD_BEVERAGE TEXT, " +
                "FACILITY_RENTAL TEXT, " +
                "STAFFING TEXT, " +
                "PROGRAMMING TEXT, " +
                "OTHER TEXT" +
                ")";

        String createUserFeedbackTable = "CREATE TABLE IF NOT EXISTS USER_FEEDBACK " +
                "(" +
                "USER_FEEDBACK_ID TEXT PRIMARY KEY NOT NULL, " +
                "SATISFACTION_RATING INT NOT NULL, " +
                "USER_ID TEXT NOT NULL, " +
                "EVENT_ID TEXT NOT NULL, " +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID), " +
                "FOREIGN KEY (EVENT_ID) REFERENCES EVENTS(EVENT_ID)" +
                ")";

        String createUnswReceiptsTable = "CREATE TABLE IF NOT EXISTS UNSW_RECEIPTS " +
                "(" +
                "RECEIPT_ID TEXT PRIMARY KEY NOT NULL, " +
                "RECEIPT_AMT REAL NOT NULL DEFAULT 0, " +
                "RECEIPT_DATE_TIME TEXT NOT NULL, " +
                "USER_ID TEXT NOT NULL, " +
                "EVENT_ID TEXT NOT NULL, " +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID), " +
                "FOREIGN KEY (EVENT_ID) REFERENCES EVENTS(EVENT_ID)" +
                ")";

        String createUserCalendarTable = "CREATE TABLE IF NOT EXISTS USER_CALENDAR " +
                "(" +
                "USER_ID TEXT NOT NULL, " +
                "EVENT_ID TEXT NOT NULL, " +
                "CAL_DATE INT NOT NULL" +
                ")";

        db.execSQL(createUserTable);
        db.execSQL(createEventTable);
        db.execSQL(createUserFollowingTable);
        db.execSQL(createUserEventTable);
        db.execSQL(createUserCalendarTable);
        db.execSQL(createEventCostingTable);
        db.execSQL(createUserFeedbackTable);
        db.execSQL(createUnswReceiptsTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUserToDatabase(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("USER_ID", user.getUserID());
        cv.put("USER_NAME", user.getUserName());
        cv.put("USER_FNAME", user.getUserFirstName());
        cv.put("USER_EMAIL", user.getUserEmail());
        cv.put("USER_PASS", toHashCode(user.getUserPass()));
        cv.put("USER_LNAME", user.getUserLastName());
        cv.put("USER_GENDER", user.getUserGender());
        cv.put("USER_TYPE", Integer.parseInt(user.getUserType()));

        db.insert("USERS", null, cv);

        Log.d("> db: addUsertoDatabase", user.getUserName() + " added to db.");

        return true;

    }

    public int toHashCode(String pass) {
        final int hash = 39;
        final long hash2 = 231234425;
        int hashResult = 1;

        hashResult = hash * hashResult + (pass.hashCode());
        hashResult = hash * hashResult + (int) (hash2 ^ (hash2 >>> 32));
        return hashResult;
    }

    public ArrayList<User> getUserInfo () {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM USERS;";
        ArrayList<User> allUsers = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                User user = new User(cursor.getString(0), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(1), cursor.getString(9), String.valueOf(cursor.getInt(11)),
                        String.valueOf(cursor.getInt(10)));
                allUsers.add(user);
                cursor.moveToNext();
            }
        }

        return allUsers;

    }

    public ArrayList<Event> getEventInfo () {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM EVENTS;";
        ArrayList<Event> allEvents = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Event event = new Event(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getInt(8), cursor.getInt(9), cursor.getDouble(10),
                        cursor.getInt(12), cursor.getString(13), cursor.getLong(16), cursor.getString(17),
                        cursor.getString(18), cursor.getInt(19), cursor.getInt(20), cursor.getInt(15), cursor.getString(14));
                allEvents.add(event);
                cursor.moveToNext();
            }
        }

        return allEvents;
    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value*1000));
        return date;
    }

    public long formatDateToEpoch(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Australia/Sydney"));
        calendar.setTime(formatter.parse(date));
        long epochSeconds = calendar.getTimeInMillis() / 1000;
        return epochSeconds;
    }


    public boolean addEventToDatabase (Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("EVENT_ID", event.getEventId());
        cv.put("EVENT_NAME", event.getEventName());
        cv.put("EVENT_LOC", event.getEventLocation());
        cv.put("EVENT_DESC", event.getEventDescription());
        cv.put("EVENT_CAT", event.getEventCategory());
        cv.put("EVENT_OWNER", event.getEventOwner());
        cv.put("EVENT_COUNTRY", event.getEventCountry());
        cv.put("EVENT_STAFFING", event.getEventStaffing());
        cv.put("EVENT_CITY", event.getEventCity());
        cv.put("EVENT_FACILITY", "UNSW Roundhouse");
        cv.put("EVENT_PRED_ATTN_NUM", event.getEventPredAttn());
        cv.put("EVENT_BUDGETED_COST", event.getEventCost());
        cv.put("EVENT_TICKETED", event.getEventIsDeleted());
        cv.put("EVENT_DATE", formatEpoch(event.getEventDate()));
        cv.put("EVENT_START_TIME", event.getEventStartTime());
        cv.put("EVENT_END_TIME", event.getEventEndTime());

        db.insert("EVENTS", null, cv);

        return true;

    }

    public void addSampleEventData () {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < 10; i++) {
            ContentValues cv = new ContentValues();
            cv.put("EVENT_ID", String.format("eventid0%d", i));
            cv.put("EVENT_NAME", String.format("evname0%d", i));
            cv.put("EVENT_LOC", String.format("evloc0%d", i));
            cv.put("EVENT_OWNER", String.format("evowner0%d", i));
            cv.put("EVENT_DESC", String.format("evdesc0%d", i));
            cv.put("EVENT_COUNTRY", String.format("evcountry0%d", i));
            cv.put("EVENT_CITY", String.format("evcity0%d", i));
            cv.put("EVENT_CAT", "Network");
            cv.put("EVENT_PRED_ATTN_NUM", 100);
            cv.put("EVENT_ACTUAL_ATTN_NUM", 105);
            cv.put("EVENT_BUDGETED_COST", 55.55);
            cv.put("EVENT_TICKETED", 0);
            cv.put("EVENT_START_DATE", 1600000000);
            cv.put("EVENT_END_DATE", 1679660928);
            cv.put("EVENT_START_TIME", 4);
            cv.put("EVENT_END_TIME", 6);
            cv.put("EVENT_ISAPPROVED", 1);

            db.insert("EVENTS", null, cv);
        }

        for (int i = 100; i > 90; i--) {
            ContentValues cv = new ContentValues();
            cv.put("EVENT_ID", String.format("eventid0%d", i));
            cv.put("EVENT_NAME", String.format("evname0%d", i));
            cv.put("EVENT_LOC", String.format("evloc0%d", i));
            cv.put("EVENT_OWNER", String.format("evowner0%d", i));
            cv.put("EVENT_DESC", String.format("evdesc0%d", i));
            cv.put("EVENT_COUNTRY", String.format("evcountry0%d", i));
            cv.put("EVENT_CITY", String.format("evcity0%d", i));
            cv.put("EVENT_CAT", "Travel");
            cv.put("EVENT_PRED_ATTN_NUM", 100);
            cv.put("EVENT_ACTUAL_ATTN_NUM", 105);
            cv.put("EVENT_BUDGETED_COST", 55.55);
            cv.put("EVENT_TICKETED", 0);
            cv.put("EVENT_START_DATE", 1600000000);
            cv.put("EVENT_END_DATE", 1679660928);
            cv.put("EVENT_START_TIME", 4);
            cv.put("EVENT_END_TIME", 6);
            cv.put("EVENT_ISAPPROVED", 1);

            db.insert("EVENTS", null, cv);
        }

        for (int i = 1000; i > 990; i--) {
            ContentValues cv = new ContentValues();
            cv.put("EVENT_ID", String.format("eventid0%d", i));
            cv.put("EVENT_NAME", String.format("evname0%d", i));
            cv.put("EVENT_LOC", String.format("evloc0%d", i));
            cv.put("EVENT_OWNER", String.format("evowner0%d", i));
            cv.put("EVENT_DESC", String.format("evdesc0%d", i));
            cv.put("EVENT_COUNTRY", String.format("evcountry0%d", i));
            cv.put("EVENT_CITY", String.format("evcity0%d", i));
            cv.put("EVENT_CAT", "Social");
            cv.put("EVENT_PRED_ATTN_NUM", 100);
            cv.put("EVENT_ACTUAL_ATTN_NUM", 105);
            cv.put("EVENT_BUDGETED_COST", 55.55);
            cv.put("EVENT_TICKETED", 0);
            cv.put("EVENT_START_DATE", 1600000000);
            cv.put("EVENT_END_DATE", 1679660928);
            cv.put("EVENT_START_TIME", 4);
            cv.put("EVENT_END_TIME", 6);
            cv.put("EVENT_ISAPPROVED", 1);

            db.insert("EVENTS", null, cv);
            Log.d(">> addSampleData", "sample entries complete.");
        }
    }
}
