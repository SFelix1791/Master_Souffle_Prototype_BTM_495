package com.example.souffleteamprototype;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    // Declare UI elements
    private EditText etFirstName, etLastName, etEmail, etPassword, etPhone;
    private TextView etCustomerId, etPoints;
    private Button btnSignUp, btnMoveToLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseHelper(this);

        // Initializes UI Elements
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCustomerId = findViewById(R.id.etCustomerId);
        etPhone = findViewById(R.id.etPhone);
        etPoints = findViewById(R.id.etPoints);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnMoveToLogin = findViewById(R.id.btnMoveToLogin);

        // Displays a unique CustomerID and initial points
        // These are invisible
        etCustomerId.setText(generateCustomerId());
        etPoints.setText("0"); // Display initial points as "0"

        btnSignUp.setOnClickListener(v -> signUp());

        // Add OnClickListener to bring user to Login page
        btnMoveToLogin.setOnClickListener(v -> {
            // Create an Intent to start LoginActivity
            Intent intent = new Intent(SignUp.this, LoginActivity.class);
            startActivity(intent); // Start the LoginActivity
        });
    }

    // Method retrieves user input
    private void signUp() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String customerId = etCustomerId.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Method saves the user data, phone number, and CustomerID in the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CUSTOMER_ID, customerId);
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, firstName);
        values.put(DatabaseHelper.COLUMN_LAST_NAME, lastName);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_PHONE, phone); // Add phone number to ContentValues
        values.put(DatabaseHelper.COLUMN_POINTS, 0); // Initialize points to 0

        long newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
        } else {
            // Stores the CustomerID, phone, and other user info in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("firstName", firstName);
            editor.putString("lastName", lastName);
            editor.putString("email", email);
            editor.putString("customerId", customerId); // Storing CustomerID
            editor.putString("phone", phone); // Storing phone number
            editor.putInt("points", 0); // Storing initial points
            editor.apply();

            // Notifies user of Successful sign up / account creation
            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
            // Log for debugging
            Log.d("SignUp", "User signed up with CustomerID: " + customerId + " and phone: " + phone);

            // Brings user to Login page
            Intent intent = new Intent(SignUp.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the SignUp activity
        }

        db.close();
    }

    // Method generates a unique user ID
    private String generateCustomerId() {
        // Initialize the customer ID and the database cursor
        String customerId = null;
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Method attempts to generate a unique customer ID
        do {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            // Generate a random 10-digit number
            customerId = String.valueOf(1000000000L + new java.util.Random().nextInt(900000000));

            // Check if this ID already exists in the database
            String[] columns = {DatabaseHelper.COLUMN_CUSTOMER_ID};
            String selection = DatabaseHelper.COLUMN_CUSTOMER_ID + " = ?";
            String[] selectionArgs = {customerId};
            cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        } while (cursor != null && cursor.moveToFirst()); // Continue if the ID is found


        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return customerId;
    }
}
