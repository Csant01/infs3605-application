package com.example.infs3605_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TMP_STRING = "TEMP";
    TextView createUserName;
    TextView createUserPass;
    TextView createUserPassCheck;
    TextView createUserEmail;
    TextView createUserFirstName;
    AutoCompleteTextView createUserType, createUserFaculty;
    Button createUserButton;
    String userName;
    String userEmail;
    String userPass;
    String userPassCheck;
    String userType;
    String userFirstName;
    String userFaculty;
    ArrayList<String> userTypeArray = new ArrayList<>();
    ArrayAdapter<String> userTypeAdapter;
    ArrayList<String> userFacultyArray = new ArrayList<>();
    ArrayAdapter<String> userFacultyAdapter;
    DatabaseConnector db;
    ImageView userImage;
    FloatingActionButton selectImage;
    private static final int SELECT_PICTURE = 100;
    byte[] blob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        db = new DatabaseConnector(this);

        userTypeArray.add("UNSW Alumni");
        userTypeArray.add("UNSW Student Society");
        userTypeArray.add("Partner University");
        userTypeArray.add("Other");
        userFacultyArray.add("Business");
        userFacultyArray.add("Engineering");
        userFacultyArray.add("Computer Science");
        userFacultyArray.add("Law");
        userFacultyArray.add("Medicine");
        userFacultyArray.add("Arts & Literature");
        userFacultyArray.add("Other");

        createUserName = findViewById(R.id.createUserName);
        createUserPass = findViewById(R.id.createUserPass);
        createUserPassCheck = findViewById(R.id.createUserPassCheck);
        createUserEmail = findViewById(R.id.createUserEmail);
        createUserType = findViewById(R.id.createUserTypeSub);
        createUserButton = findViewById(R.id.createUserButton);
        createUserFirstName = findViewById(R.id.createUserFirstName);

        userImage = findViewById(R.id.uploadProfilePictureImageView);
        selectImage = findViewById(R.id.addProfilePictureButton);
        createUserFaculty = findViewById(R.id.createUserFacultySub);
        selectImage.setOnClickListener(this);


        userTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_list, userTypeArray);
        createUserType.setAdapter(userTypeAdapter);
        userFacultyAdapter = new ArrayAdapter<>(this, R.layout.dropdown_list, userFacultyArray);
        createUserFaculty.setAdapter(userFacultyAdapter);


        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = createUserName.getText().toString();
                userPass = createUserPass.getText().toString();
                userPassCheck = createUserPassCheck.getText().toString();
                userEmail = createUserEmail.getText().toString();
                userType = createUserType.getText().toString();
                userFirstName = createUserFirstName.getText().toString();
                userFaculty = createUserFaculty.getText().toString();

                User user = new User(TMP_STRING, userFirstName, null, null, userName,
                        userEmail, userPass, userType, userFaculty, null);

                if (checkEmail(userEmail, userType)) {
                    if (checkPass(userPass, userPassCheck)) {
                        user.setUserID(createUserID(userType));
                        user.setUserType(checkUserType(user));
                        if(db.addUserToDatabase(user)) {
                            db.assignOrgIdToImage(user.getUserID(), blob);
                            Toast.makeText(CreateUserActivity.this, "User created successfully.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    } else {
                        Toast.makeText(CreateUserActivity.this, "Please ensure to use your" +
                                        " passwords match.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateUserActivity.this, "Please ensure to use your" +
                                    " UNSW email.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public boolean checkPass (String pass, String passCheck) {
        if (pass.equals(passCheck)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmail (String email, String userType) {

        String emailPattern = ".*@[^.@]+\\.unsw\\.edu\\.au$";

        if (!userType.equals("Student") && !userType.equals("UNSW Staff")) {
            return true;
        } else if (email.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }

    public String createUserID (String userType) {
        String first = userType.toLowerCase().substring(0,1);
        Random rand = new Random();
        int min = 1111111;
        int max = 9999999;
        int randomNumber = rand.nextInt((max - min) + 1) + min;

        return first + randomNumber;

    }

    public String checkUserType (User user) {
        if (user.getUserType().equals("UNSW Student Society")) {
            return "1";
        } else if (user.getUserType().equals("Partner University")) {
            return "2";
        } else if (user.getUserType().equals("Other")) {
            return "3";
        } else if (user.getUserType().equals("UNSW Alumni")) {
            return "4";
        } else {
            return "999";
        }
    }

    public void showMessage (String message) {
        Toast.makeText(CreateUserActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES};
        if(hasStoragePermission(CreateUserActivity.this)) {
            openImageChooser();
        } else {
            ActivityCompat.requestPermissions(((AppCompatActivity) CreateUserActivity.this), permissions, 200);
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
                        userImage.setImageURI(selectImageUri);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                userImage.setVisibility(View.GONE);
                            }
                        }, 2000);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loadImageFromDatabase()) {
                                userImage.setVisibility(View.VISIBLE);
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
            byte[] bytes = db.retrieveOrganiserImageFromDatabase();
            userImage.setImageBitmap(ImageUtils.getImage(bytes));
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
            db.insertOrganiserImage(inputData, null);
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
