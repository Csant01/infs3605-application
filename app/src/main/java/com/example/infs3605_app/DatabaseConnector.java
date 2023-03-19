package com.example.infs3605_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

import androidx.annotation.Nullable;

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
                "EVENT_LOCATION TEXT NOT NULL, " +
                "EVENT_PRED_ATTN_NUM INT DEFAULT 0, " +
                "EVENT_ACTUAL_ATTN_NUT INT DEFAULT 0, " +
                "EVENT_BUDGETED_COST INT DEFAULT 0, " +
                "EVENT_COST_ID TEXT NOT NULL, " +
                "EVENT_TICKETED INT DEFAULT 0," +
                "EVENT_IMAGE TEXT, " +
                "EVENT_START_DATE INT NOT NULL, " +
                "EVENT_END_DATE INT NOT NULL, " +
                "EVENT_START_TIME INT NOT NULL, " +
                "EVENT_END_TIME INT NOT NULL, " +
                "EVENT_ISDELETED INT DEFAULT 0, " +
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
}
