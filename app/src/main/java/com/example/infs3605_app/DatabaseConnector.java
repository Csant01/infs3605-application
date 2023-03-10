package com.example.infs3605_app;

import android.content.Context;
import android.database.sqlite.*;

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
                "USER_FNAME TEXT NOT NULL, " +
                "USER_LNAME TEXT NOT NULL, " +
                "USER_GENDER TEXT NOT NULL, " +
                "USER_EMAIL TEXT NOT NULL, " +
                "USER_DOB INT NOT NULL, " +
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
                "EVENT_ATTN_COUNT INT, " +
                "EVENT_START_DATE INT NOT NULL, " +
                "EVENT_END_DATE INT NOT NULL, " +
                "EVENT_START_TIME INT NOT NULL, " +
                "EVENT_END_TIME INT NOT NULL" +
                ")";

        String createUserFollowingTable = "CREATE TABLE IF NOT EXISTS USER_FOLLOWING " +
                "(" +
                "USER_ID TEXT NOT NULL, " +
                "FOLLOWING_ID TEXT NOT NULL, " +
                "FOLLOW_DATE INT NOT NULL" +
                ")";

        String createUserEventTable = "CREATE TABLE IF NOT EXISTS USER_EVENT " +
                "(" +
                "USER_ID TEXT NOT NULL, " +
                "EVENT_ID TEXT NOT NULL" +
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
