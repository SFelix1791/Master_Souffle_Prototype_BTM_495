package com.example.souffleteamprototype;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
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
            if (cursor != null && cursor.moveToFirst()) {
                int firstNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME);
                int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);
                // Password index is not used further, consider removing it if not needed
                // int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD);

                if (firstNameIndex == -1 || lastNameIndex == -1 || emailIndex == -1) {
                    Log.e("LoginActivity", "One or more column indices are invalid.");
                    Toast.makeText(this, "Invalid column indices", Toast.LENGTH_SHORT).show();
                } else {
                    String firstName = cursor.getString(firstNameIndex);
                    String lastName = cursor.getString(lastNameIndex);
                    String email = cursor.getString(emailIndex);

                    // Save user information in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("firstName", firstName);
                    editor.putString("lastName", lastName);
                    editor.putString("email", email);
                    // You might not need to store the password here for security reasons
                    // Consider what information is necessary for your app's functionality
                    editor.apply();

                    // Proceed to the Menu activity
                    Intent intent = new Intent(LoginActivity.this, Menu.class);
                    startActivity(intent);
                    finish(); // Close LoginActivity
                }
            } else {
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
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
// Eventually we need to make log-in page lock you out after 3 failed attempts with a message saying email ha been sent