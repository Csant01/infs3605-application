package com.example.infs3605_app;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class User {
    private String userID;
    private String userName;
    private String userEmail;
    private String userPass;
    private String userType;
    private String userFirstName;
    private String userLastName;
    private String userGender;
    public static ArrayList<String> currentlyLoggedIn = new ArrayList<>();

    public User (String userID, String userFirstName, String userLastName, String userGender,
                 String userName, String userEmail,
                 String userPass, String userType) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userType = userType;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userGender = userGender;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public boolean hasEmptyFields() {
        // Get all the fields in the class
        Field[] fields = getClass().getDeclaredFields();

        // Iterate over all the fields and check if any of them have an empty value
        for (Field field : fields) {
            try {
                Object value = field.get(this);
                if (value == null || TextUtils.isEmpty(value.toString())) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}


