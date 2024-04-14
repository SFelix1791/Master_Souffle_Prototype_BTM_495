package com.example.souffleteamprototype;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderFeedback extends AppCompatActivity {

    private EditText editTextFeedback;
    private RatingBar ratingBarFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_feedback);

        // Setting up Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the EditText and RatingBar by their IDs
        editTextFeedback = findViewById(R.id.editTextFeedback);
        ratingBarFeedback = findViewById(R.id.ratingBarFeedback);

        // Initialize the button and set its onClickListener
        Button buttonSubmitFeedback = findViewById(R.id.buttonSubmitFeedback);
        buttonSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

        // Initialize the Image button to return to the Menu and set its onClickListener
        ImageButton btnX = findViewById(R.id.btnX);
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Menu activity
                Intent intent = new Intent(OrderFeedback.this, Menu.class);
                startActivity(intent);
                finish();  // Close the current activity
            }
        });
    }

    private void submitFeedback() {
        String feedbackText = editTextFeedback.getText().toString().trim();
        float rating = ratingBarFeedback.getRating();

        // Checks if the feedback is not empty
        if (!feedbackText.isEmpty()) {
            long userId = getCurrentUserId();  // Fetch the current user's ID

            DatabaseHelper dbHelper = new DatabaseHelper(OrderFeedback.this);
            boolean success = dbHelper.addOrderFeedback(userId, feedbackText, (int) rating);
            if (success) {
                Toast.makeText(OrderFeedback.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                // Brings User to the Home / Menu page
                Intent intent = new Intent(OrderFeedback.this, Menu.class);
                startActivity(intent);
                finish();  // Closes the current activity
            } else {
                Toast.makeText(OrderFeedback.this, "Failed to submit feedback.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(OrderFeedback.this, "Feedback cannot be empty.", Toast.LENGTH_LONG).show();
        }
    }

    private long getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return prefs.getLong("userId", -1); // Default to -1 if no user ID is found
    }
}
