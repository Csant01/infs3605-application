package com.example.infs3605_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.InputStream;
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
import java.util.List;
import java.util.TimeZone;

public class DatabaseConnector extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseConnector";
    public static final String IMAGE_ID = "id";
    public static final String IMAGE = "image";
    private SQLiteDatabase mDb;
    private DatabaseConnector mDbc;
    long epochSeconds;

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
                "USER_FNAME TEXT, " +
                "USER_LNAME TEXT, " +
                "USER_GENDER TEXT, " +
                "USER_DOB INT, " +
                "USER_COUNTRY TEXT, " +
                "USER_CITY TEXT, " +
                "USER_PROF_IMAGE TEXT, " +
                "USER_EMAIL TEXT NOT NULL, " +
                "USER_TYPE INT NOT NULL, " +
                "USER_PASS INT NOT NULL, " +
                "USER_IMAGE BLOB, " +
                "USER_FACULTY TEXT, " +
                "USER_ISAPPROVED INT DEFAULT 0" +
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
                "EVENT_IMAGE BLOB, " +
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
                "USER_EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USER_FAV INT DEFAULT 0, " +
                "USER_ATTENDED INT DEFAULT 0, " +
                "USER_ID TEXT NOT NULL, " +
                "EVENT_ID TEXT NOT NULL, " +
                "USER_FEEDBACK_ID TEXT, " +
                "FEEDBACK_COMPLETED INT DEFAULT 0, " +
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
                "USER_FEEDBACK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Q1 INT NOT NULL, " +
                "Q2 INT NOT NULL, " +
                "Q3 INT NOT NULL, " +
                "Q4 INT NOT NULL, " +
                "ADDITIONAL_COMMENTS TEXT, " +
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

        String createEventImagesTable = "CREATE TABLE IF NOT EXISTS EVENT_IMAGES " +
                "(" +
                "IMAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "EVENT_IMAGE BLOB NOT NULL, " +
                "EVENT_ID TEXT, " +
                "FOREIGN KEY (EVENT_ID) REFERENCES EVENTS(EVENT_ID)" +
                ")";

        String createOrganiserImagesTable = "CREATE TABLE IF NOT EXISTS ORGANISER_IMAGES " +
                "(" +
                "ORG_IMAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ORG_IMAGE BLOB NOT NULL, " +
                "USER_ID TEXT, " +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)" +
                ")";

        db.execSQL(createUserTable);
        db.execSQL(createEventTable);
        db.execSQL(createUserFollowingTable);
        db.execSQL(createUserEventTable);
        db.execSQL(createUserCalendarTable);
        db.execSQL(createEventCostingTable);
        db.execSQL(createUserFeedbackTable);
        db.execSQL(createUnswReceiptsTable);
        db.execSQL(createEventImagesTable);
        db.execSQL(createOrganiserImagesTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public int getUserApprovedStatus (String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT USER_ISAPPROVED FROM USERS WHERE USER_ID = '%s' AND USER_TYPE NOT IN (3, 999)", userId);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return 99;

    }

    public void tmpQuery () {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE USERS SET USER_ISAPPROVED = 1 WHERE USER_ID = 'USR021'";
        db.execSQL(query);
    }
    public ArrayList<Event> getPendingEvents (String userName) {
        String userId = getUserId(userName);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT EVENT_ID FROM EVENTS WHERE EVENT_ISAPPROVED = 0 AND EVENT_OWNER = '%s'", userId);
        ArrayList<Event> pendingEvents = new ArrayList<>();
        ArrayList<Event> allEvents = getEventInfo();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                for (int i = 0; i < allEvents.size(); i++) {
                    if (allEvents.get(i).getEventId().equals(cursor.getString(0))) {
                        pendingEvents.add(allEvents.get(i));
                    }
                }
                cursor.moveToNext();
            }
        }

        return pendingEvents;
    }

    public ArrayList<String> getFeedbackComments (String eventName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String eventId = getEventId(eventName);
        String query = String.format("SELECT ADDITIONAL_COMMENTS FROM USER_FEEDBACK WHERE EVENT_ID = '%s'", eventId);
        ArrayList<String> allComments = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allComments.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }

        return allComments;
    }

    public ArrayList<String> getFeedbackCommentsAll () {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = String.format("SELECT ADDITIONAL_COMMENTS FROM USER_FEEDBACK");
        ArrayList<String> allComments = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allComments.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }

        return allComments;
    }

    public ArrayList<FeedbackAverage> getFeedbackAverages (ArrayList<String> eventIds) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FeedbackAverage> feedbackAverages = new ArrayList<>();
        for (String eventId : eventIds) {
            String query = String.format("SELECT Q1, Q2, Q3, Q4 FROM USER_FEEDBACK WHERE EVENT_ID = '%s'", eventId);
            Cursor cursor = db.rawQuery(query, null);
            String eventName = getEventName(eventId);
            ArrayList<Integer> satisfaction = new ArrayList<>();
            ArrayList<Integer> likeliness = new ArrayList<>();
            ArrayList<Integer> usefulness = new ArrayList<>();
            ArrayList<Integer> rating = new ArrayList<>();

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    satisfaction.add(cursor.getInt(0));
                    likeliness.add(cursor.getInt(1));
                    usefulness.add(cursor.getInt(2));
                    rating.add(cursor.getInt(3));
                    cursor.moveToNext();
                }
            }

            int satisfactionSum = 0;
            for (int i : satisfaction) {
                satisfactionSum += i;
            }

            int likelinessSum = 0;
            for (int i : likeliness) {
                likelinessSum += i;
            }

            int usefulnessSum = 0;
            for (int i : usefulness) {
                usefulnessSum += i;
            }

            int ratingSum = 0;
            for (int i : rating) {
                ratingSum += i;
            }

            double satisfactionAvg = (double) satisfactionSum / satisfaction.size();
            double likelinessAvg = (double) likelinessSum / likeliness.size();
            double usefulnessAvg = (double) usefulnessSum / usefulness.size();
            double ratingAvg = (double) ratingSum / rating.size();

            String satisfactionAvgFormatted = String.format("%.2f", satisfactionAvg);
            String likelinessAvgFormatted = String.format("%.2f", likelinessAvg);
            String usefulnessAvgFormatted = String.format("%.2f", usefulnessAvg);
            String ratingAvgFormatted = String.format("%.2f", ratingAvg);

            FeedbackAverage averages = new FeedbackAverage(eventName, Double.parseDouble(satisfactionAvgFormatted), Double.parseDouble(likelinessAvgFormatted),
                    Double.parseDouble(usefulnessAvgFormatted), Double.parseDouble(ratingAvgFormatted));
            feedbackAverages.add(averages);

        }

        return feedbackAverages;
    }

    public Event getEventById (String eventId) {
        ArrayList<Event> allEvents = getEventInfo();
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getEventId().equals(eventId)) {
                Event event = allEvents.get(i);
                return event;
            }
        }

        return null;
    }

    public String getEventName (String eventId) {
        ArrayList<Event> allEvents = getEventInfo();
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getEventId().equals(eventId)) {
                return allEvents.get(i).getEventName();
            }
        }
        return null;
    }

    public String getEventId (String eventName) {
        ArrayList<Event> allEvents = getEventInfo();
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getEventName().equals(eventName)) {
                return allEvents.get(i).getEventId();
            }
        }
        return null;
    }


    public void submitFeedback (String userName, String eventId, EventFeedback feedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        String userId = getUserId(userName);
        ContentValues cv = new ContentValues();
        cv.put("Q1", feedback.getQ1());
        cv.put("Q2", feedback.getQ2());
        cv.put("Q3", feedback.getQ3());
        cv.put("Q4", feedback.getQ4());
        cv.put("ADDITIONAL_COMMENTS", feedback.getAdditional());
        cv.put("USER_ID", userId);
        cv.put("EVENT_ID", eventId);
        db.insert("USER_FEEDBACK", null, cv);
    }

    public ArrayList<User> getParticipants (String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT USER_ID FROM USER_EVENTS WHERE USER_ATTENDED = 0 AND EVENT_ID = '%s'", eventId);
        ArrayList<String> userIds = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                userIds.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        ArrayList<User> allUsers = getUserInfo();
        ArrayList<User> participants = new ArrayList<>();
        for (String userId : userIds) {
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getUserID().equals(userId)) {
                    participants.add(allUsers.get(i));
                    break;
                }
            }
        }
        return participants;
    }

    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EVENT_NAME", event.getEventName());
        values.put("EVENT_LOC", event.getEventLocation());
        values.put("EVENT_DESC", event.getEventDescription());
        values.put("EVENT_COUNTRY", event.getEventCountry());
        values.put("EVENT_CITY", event.getEventCity());
        values.put("EVENT_FACILITY", event.getEventFacility());
        values.put("EVENT_CAT", event.getEventCategory());
        values.put("EVENT_PRED_ATTN_NUM", event.getEventPredAttn());
        values.put("EVENT_BUDGETED_COST", event.getEventCost());
        values.put("EVENT_TICKETED", event.getEventTicketed());
        values.put("EVENT_START_TIME", event.getEventStartTime());
        values.put("EVENT_END_TIME", event.getEventEndTime());
        values.put("EVENT_ACTUAL_ATTN_NUM", event.getEventActAttn());
        values.put("EVENT_STAFFING", event.getEventStaffing());

        String[] eventId = {event.getEventId()};
        db.update("EVENTS", values, "EVENT_ID = ?", eventId);
        db.close();
    }

    public String getUserType (int user) {
        if (user == 1) {
            return "UNSW Student Society";
        } else if (user == 2) {
            return "Partner University";
        } else if (user == 3) {
            return "Other";
        } else if (user == 4) {
            return "UNSW Alumni";
        } else {
            return "Other";
        }
    }

    public String getUserName (String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT USER_NAME FROM USERS WHERE USER_ID = '%s'", userId);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            return name;
        }

        return null;
    }

    public void setEventApproval (String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("UPDATE EVENTS SET EVENT_ISAPPROVED = 1 WHERE EVENT_ID = '%s'", eventId);
        db.execSQL(query);
    }

    public void rejectEvent (String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("UPDATE EVENTS SET EVENT_ISAPPROVED = -1 WHERE EVENT_ID = '%s'", eventId);
        db.execSQL(query);
    }

    public void rejectUser (String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("UPDATE USERS SET USER_ISAPPROVED = -1 WHERE USER_ID = '%s'", userId);
        db.execSQL(query);
    }

    public void setUserApproval (String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("UPDATE USERS SET USER_ISAPPROVED = 1 WHERE USER_ID = '%s'", userId);
        db.execSQL(query);
    }

    public ArrayList<User> getNonStudents () {
        ArrayList<User> allUsers = getUserInfo();
        ArrayList<User> nonStudents = new ArrayList<>();
        for (int i = 0; i < allUsers.size(); i++) {
            if (Integer.parseInt(allUsers.get(i).getUserType()) != 3 && Integer.parseInt(allUsers.get(i).getUserType()) != 999) {
                nonStudents.add(allUsers.get(i));
            }
        }
        return nonStudents;
    }

    public ArrayList<String> getUserFollowingString (String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userId = getUserId(userName);
        ArrayList<String> followingList = new ArrayList<>();
        String query = String.format("SELECT FOLLOWING_ID FROM USER_FOLLOWING WHERE USER_ID = '%s'", userId);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                followingList.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }

        return followingList;
    }

    public ArrayList<User> getUserFollowingUser(String userName) {
        ArrayList<String> followingString = getUserFollowingString(userName);
        ArrayList<User> allUsers = getUserInfo();
        ArrayList<User> followingUser = new ArrayList<>();

        for (int i = 0; i < allUsers.size(); i++) {
            for (String followingName : followingString) {
                if (allUsers.get(i).getUserID().equals(followingName)) {
                    followingUser.add(allUsers.get(i));
                    Log.d(TAG, "getUserFollowingUser: " + allUsers.get(i).getUserName());
                    break;
                }
            }
        }
        return followingUser;
    }
    public boolean checkFollowing (String userName, String followingId) {
        String userId = getUserId(userName);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT FOLLOWING_ID FROM USER_FOLLOWING WHERE USER_ID = '%s'", userId);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(0).equals(followingId)) {
                    return true;
                }
                cursor.moveToNext();
            }
        }

        return false;
    }

    public void unsetUserFollowing (String userName, String followingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String userId = getUserId(userName);
        String query = String.format("DELETE FROM USER_FOLLOWING WHERE USER_ID = '%s' AND FOLLOWING_ID = '%s'", userId, followingId);
        Log.d(TAG, "unsetUserFollowing: " + followingId + " unfollowed");
        db.execSQL(query);

    }

    public boolean setUserFollowing (String userName, String followingId) {
        ArrayList<String> followingList = getUserFollowingString(userName);
        if (followingList.contains(followingId)) {
            return false;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            String userId = getUserId(userName);
            Date currentDate = new Date();
            long epochMillis = currentDate.getTime();
            epochSeconds = epochMillis / 1000L;
            ContentValues cv = new ContentValues();

            cv.put("USER_ID", userId);
            cv.put("FOLLOWING_ID", followingId);
            cv.put("FOLLOW_DATE", epochSeconds);
            db.insert("USER_FOLLOWING", null, cv);
            Log.d(TAG, "setUserFollowing: " + followingId + " followed");


            return true;
        }

    }

    public String getUserId (String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT USER_ID FROM USERS WHERE USER_NAME = '%s'", userName);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String id = cursor.getString(0);
            return id;
        }

        return null;
    }

    public void insertEventImage(byte[] imageBytes, @Nullable String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("EVENT_ID", eventId);
        cv.put("EVENT_IMAGE", imageBytes);
        db.insert("EVENT_IMAGES", null, cv);

    }

    public void insertOrganiserImage(byte[] imageBytes, @Nullable String eventOwner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("USER_ID", eventOwner);
        cv.put("ORG_IMAGE", imageBytes);
        db.insert("ORGANISER_IMAGES", null, cv);

    }

    public byte[] retrieveEventImageFromDatabase () {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT EVENT_IMAGE FROM EVENT_IMAGES ORDER BY EVENT_ID DESC";
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(0);
            return blob;
        }

        return null;
    }

    public byte[] retrieveOrganiserImageFromDatabase () {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ORG_IMAGE FROM ORGANISER_IMAGES ORDER BY ORG_IMAGE_ID DESC";
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(0);
            return blob;
        }

        return null;
    }

    public void assignEventIdToImage (String eventId, byte[] blob) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE EVENT_IMAGES SET EVENT_ID = ? WHERE EVENT_IMAGE = ?";
        db.execSQL(query, new Object[] {eventId, blob});
        assignImageToEvent(eventId, blob);
    }

    public void assignOrgIdToImage (String eventOwner, byte[] blob) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ORGANISER_IMAGES SET USER_ID = ? WHERE ORG_IMAGE = ?";
        db.execSQL(query, new Object[] {eventOwner, blob});
        assignImageToOrganiser(eventOwner, blob);
    }

    public void assignImageToEvent(String eventId, byte[] blob) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE EVENTS SET EVENT_IMAGE = ? WHERE EVENT_ID = ?";
        db.execSQL(query, new Object[] {blob, eventId});
    }

    public void assignImageToOrganiser(String eventOwner, byte[] blob) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE USERS SET USER_IMAGE = ? WHERE USER_ID = ?";
        db.execSQL(query, new Object[] {blob, eventOwner});
    }

    public byte[] retrieveEventImageFromDatabaseFiltered (String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT EVENT_IMAGE FROM EVENT_IMAGES WHERE EVENT_ID = '%s'", eventId);
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(0);
            return blob;
        }

        return null;
    }

    public byte[] retrieveEventImageDirect (String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT EVENT_IMAGE FROM EVENTS WHERE EVENT_ID = '%s'", eventId);
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(0);
            return blob;
        }

        return null;
    }

    public byte[] retrieveOrganiserImageFromDatabaseFiltered (String eventOwner) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT ORG_IMAGE FROM ORGANISER_IMAGES WHERE USER_ID = '%s'", eventOwner);

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(0);
            return blob;
        }

        return null;
    }

    public byte[] retrieveOrganiserImageDirect (String eventOwner) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT USER_IMAGE FROM USERS WHERE USER_ID = '%s'", eventOwner);
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(0);
            return blob;
        }

        return null;
    }

    public DatabaseConnector open() throws SQLException {
        mDb = this.getWritableDatabase();
        return this;
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
        cv.put("USER_FACULTY", user.getUserFaculty());

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
                        String.valueOf(cursor.getInt(10)), cursor.getString(13), cursor.getBlob(12));
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
                        cursor.getInt(12), cursor.getBlob(13), cursor.getLong(16), cursor.getString(17),
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
        cv.put("EVENT_DATE", event.getEventDate());
        cv.put("EVENT_START_TIME", event.getEventStartTime());
        cv.put("EVENT_END_TIME", event.getEventEndTime());

        db.insert("EVENTS", null, cv);

        return true;

    }

    public boolean addUserEventToDatabase (UserEvent userEvent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("USER_EVENT_ID", userEvent.getUserEventId());
        cv.put("USER_FAV", userEvent.getUserFavourited());
        cv.put("USER_ATTENDED", userEvent.getUserAttended());
        cv.put("USER_ID", userEvent.getUserId());
        cv.put("EVENT_ID", userEvent.getEventId());
        cv.put("USER_FEEDBACK_ID", userEvent.getUserFeedbackId());
        cv.put("FEEDBACK_COMPLETED", userEvent.getFeedbackCompleted());

        db.insert("USER_EVENTS", null, cv);
        return true;
    }


    public ArrayList<UserEvent> getUserEvents (String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userId = getUserId(user);


        String query = String.format("SELECT * FROM USER_EVENTS WHERE USER_ID = '%s'", userId);
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<UserEvent> userEvents = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                UserEvent userEvent = new UserEvent(cursor.getString(0), cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
                userEvents.add(userEvent);
                cursor.moveToNext();
            }
        }

        return userEvents;
    }

    public int setUserGoing (String userName, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String userId = getUserId(userName);

        if (!checkUserGoing(userName, eventId) && checkEventDate(eventId)) {
            cv.put("USER_FAV", 1);
            cv.put("USER_ATTENDED", 0);
            cv.put("USER_ID", userId);
            cv.put("EVENT_ID", eventId);
            db.insert("USER_EVENTS", null, cv);
            return 0;
        } else if (!checkEventDate(eventId)) {
            return 1;
        } else {
            String query = String.format("DELETE FROM USER_EVENTS WHERE USER_ID = '%s' AND EVENT_ID = '%s'", userId, eventId);
            db.execSQL(query);
            return 2;
        }

    }

    public boolean checkEventDate (String eventId) {
        Date currentDate = new Date();
        long epochMillis = currentDate.getTime();
        epochSeconds = epochMillis / 1000L;
        List<Event> allEvents = getEventInfo();


        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getEventId().equals(eventId)) {
                if (allEvents.get(i).getEventDate() > epochSeconds) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    public boolean checkUserGoing (String userName, String eventId) {
        ArrayList<UserEvent> userEvents = getUserEvents(userName);

        for (int i = 0; i < userEvents.size(); i++) {
            if (userEvents.get(i).getEventId().equals(eventId)) {
                if (userEvents.get(i).getUserFavourited() == 1 && userEvents.get(i).getUserAttended() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;

    }
}
