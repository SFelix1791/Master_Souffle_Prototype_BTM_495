package com.example.souffleteamprototype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    EditText etEmail, etPassword;
    Button btnLogin, btnMoveToSignup;
    DatabaseHelper dbHelper;

    // Sets failed login attempts to 0
    private int failedLoginAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Clear the shopping cart information when the app starts
        // Intended to refresh the cart when user logs out
        clearCartInfo();

        /// Initialize UI elements and DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnMoveToSignup = findViewById(R.id.btnMoveToSignup);

        btnLogin.setOnClickListener(v -> login());
        btnMoveToSignup.setOnClickListener(v -> moveToSignUp());
    }

    // Method brings user to SignUp.java page
    // Chose to display it this way for visual clarity
    private void moveToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(intent);
    }

    // Method logs in user, allows them to enter the system
    // Method also notifies user when login is disabled following 3 failed attempts
    private void login() {
        if (failedLoginAttempts >= 3) {
            Toast.makeText(this, "Login is Disabled, Please Contact Staff", Toast.LENGTH_LONG).show();
            return;
        }

        // Retrieve user input from editTexts
        String inputEmail = etEmail.getText().toString();
        String inputPassword = etPassword.getText().toString();

        // Retrieve database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.COLUMN_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {inputEmail, inputPassword};

        try {
            // Query for Database for user with specified email & password
            Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int userIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int firstNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME);
                int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);

                if (userIdIndex == -1 || firstNameIndex == -1 || lastNameIndex == -1 || emailIndex == -1) {
                    Log.e("LoginActivity", "One or more column indices are invalid.");
                    Toast.makeText(this, "Invalid column indices", Toast.LENGTH_SHORT).show();
                } else {
                    // Retrieve user details
                    long userId = cursor.getLong(userIdIndex); // Retrieve the user ID
                    String firstName = cursor.getString(firstNameIndex);
                    String lastName = cursor.getString(lastNameIndex);
                    String email = cursor.getString(emailIndex);

                    // Pass user details to shared preferences for later usage
                    SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("userId", userId); // Save the user ID
                    editor.putString("firstName", firstName);
                    editor.putString("lastName", lastName);
                    editor.putString("email", email);
                    editor.apply();

                    failedLoginAttempts = 0; // This is to reset failed login attempts
                    // Brings user to the home page / Menu.java page
                    Intent intent = new Intent(LoginActivity.this, Menu.class);
                    startActivity(intent);
                    finish(); // Closes page
                }
                cursor.close();
            } else {
                failedLoginAttempts++; // This increments the login attempts
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();

                if (failedLoginAttempts >= 3) {
                    Toast.makeText(this, "Login is Disabled, Please Contact Staff", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Login failed", e);
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        } finally {
            db.close(); // Closes the database afterwards
        }
    }

    // Clears all information in ShoppingCart upon Logging-out
    private void clearCartInfo() {
        SharedPreferences cartPreferences = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cartPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
