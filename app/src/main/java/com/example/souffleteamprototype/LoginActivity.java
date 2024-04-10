package com.example.souffleteamprototype;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, btnMoveToSignup; // Added button for moving to SignUp activity
    DatabaseHelper dbHelper;
    private int failedLoginAttempts = 0; // Added failed login attempts counter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializes DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initializes UI elements
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnMoveToSignup = findViewById(R.id.btnMoveToSignup); // Initialize the button

        // Sets onClickListener to Log-in User
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Sets onClickListener to move to SignUp activity
        btnMoveToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSignUp();
            }
        });
    }

    // Method to move to SignUp activity
    private void moveToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(intent);
    }

    // Method logs user into the system
    private void login() {
        // Check if login attempts exceeded
        if (failedLoginAttempts >= 3) {
            // Display notification and return, preventing further login attempts
            Toast.makeText(this, "Login is Disabled, Please Contact Staff", Toast.LENGTH_LONG).show();
            return;
        }

        // Retrieves email and password from the EditTexts in UI
        String inputEmail = etEmail.getText().toString();
        String inputPassword = etPassword.getText().toString();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COLUMN_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {inputEmail, inputPassword};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        try {
            // Checks if user with matching log-in credentials is in Database
            if (cursor != null && cursor.moveToFirst()) {
                int firstNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME);
                int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);


                if (firstNameIndex == -1 || lastNameIndex == -1 || emailIndex == -1) {
                    Log.e("LoginActivity", "One or more column indices are invalid.");
                    Toast.makeText(this, "Invalid column indices", Toast.LENGTH_SHORT).show();
                } else {
                    String firstName = cursor.getString(firstNameIndex);
                    String lastName = cursor.getString(lastNameIndex);
                    String email = cursor.getString(emailIndex);

                    // Saves user info in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("firstName", firstName);
                    editor.putString("lastName", lastName);
                    editor.putString("email", email);
                    editor.apply();

                    // Reset failed login attempts
                    failedLoginAttempts = 0;

                    // Brings user to the Menu Page upon successful log-in
                    Intent intent = new Intent(LoginActivity.this, Menu.class);
                    startActivity(intent);
                    finish(); // Close LoginActivity
                }
            } else {
                // Increment failed login attempts
                failedLoginAttempts++;

                // Displays error message
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();

                // Check if login attempts exceeded after increment
                if (failedLoginAttempts >= 3) {
                    // Display notification
                    Toast.makeText(this, "Login is Disabled, Please Contact Staff", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Login failed", e);
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
}

