package com.example.souffleteamprototype;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {

    private EditText editEmail, editFirstName, editLastName;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dbHelper = new DatabaseHelper(this);

        editEmail = findViewById(R.id.editEmail);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);

        // Get the email from intent extras
        String userEmail = getIntent().getStringExtra("email");
        if (userEmail != null && !userEmail.isEmpty()) {
            // Set the email to the editEmail field
            editEmail.setText(userEmail);
        }

        // Set editEmail to be non-editable
        editEmail.setEnabled(false);

        Button btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                if (!email.isEmpty()) {
                    updateUserInformation(email);
                } else {
                    Toast.makeText(EditProfile.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateUserInformation(String email) {
        Cursor cursor = dbHelper.getUserByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            String firstName = editFirstName.getText().toString();
            String lastName = editLastName.getText().toString();

            int rowsAffected = dbHelper.updateUserInfo(email, firstName, lastName);

            if (rowsAffected > 0) {
                Log.d("EditProfile", "User info updated successfully.");
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                // Redirect to the home page activity
                Intent intent = new Intent(EditProfile.this, Menu.class);
                startActivity(intent);
                finish(); // Close the current activity
            } else {
                Log.e("EditProfile", "Failed to update user info.");
                Toast.makeText(this, "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User with provided email not found", Toast.LENGTH_SHORT).show();
        }
    }
}
