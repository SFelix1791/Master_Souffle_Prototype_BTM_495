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

    EditText etEmail, etPassword;
    Button btnLogin, btnMoveToSignup;
    DatabaseHelper dbHelper;
    private int failedLoginAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Clear the cart information when the app starts
        clearCartInfo();

        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnMoveToSignup = findViewById(R.id.btnMoveToSignup);

        btnLogin.setOnClickListener(v -> login());
        btnMoveToSignup.setOnClickListener(v -> moveToSignUp());
    }

    private void moveToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(intent);
    }

    private void login() {
        if (failedLoginAttempts >= 3) {
            Toast.makeText(this, "Login is Disabled, Please Contact Staff", Toast.LENGTH_LONG).show();
            return;
        }

        String inputEmail = etEmail.getText().toString();
        String inputPassword = etPassword.getText().toString();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.COLUMN_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {inputEmail, inputPassword};

        try (Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null, selection, selectionArgs, null, null, null)) {
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

                    SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("firstName", firstName);
                    editor.putString("lastName", lastName);
                    editor.putString("email", email);
                    editor.apply();

                    failedLoginAttempts = 0;
                    Intent intent = new Intent(LoginActivity.this, Menu.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                failedLoginAttempts++;
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();

                if (failedLoginAttempts >= 3) {
                    Toast.makeText(this, "Login is Disabled, Please Contact Staff", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Login failed", e);
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
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
