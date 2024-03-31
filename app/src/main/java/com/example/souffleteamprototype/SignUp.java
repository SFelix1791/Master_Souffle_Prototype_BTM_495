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

    private EditText etFirstName, etLastName, etEmail, etPassword;
    private TextView etCustomerId;
    private Button btnSignUp;
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
        btnSignUp = findViewById(R.id.btnSignUp);

        // Display a unique CustomerID
        etCustomerId.setText(generateCustomerId());

        btnSignUp.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String customerId = etCustomerId.getText().toString(); // Retrieve the displayed CustomerID

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Method saves the user data and CustomerID in the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CUSTOMER_ID, customerId);
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, firstName);
        values.put(DatabaseHelper.COLUMN_LAST_NAME, lastName);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        long newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
        } else {
            // Stores the CustomerID and other user info in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("firstName", firstName);
            editor.putString("lastName", lastName);
            editor.putString("email", email);
            editor.putString("customerId", customerId); // Storing CustomerID
            editor.apply();

            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
            // Log for debugging plus I think it looks nice
            Log.d("SignUp", "Stored CustomerID in SharedPreferences: " + customerId);

            // Redirect to LoginActivity
            Intent intent = new Intent(SignUp.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the SignUp activity
        }

        db.close();
    }

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
