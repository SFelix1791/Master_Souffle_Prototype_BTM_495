package com.example.souffleteamprototype;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// This class creates new comment Reviews
public class CreateReview extends AppCompatActivity {

    // Declares UI Components
    private EditText editTextReview;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        editTextReview = findViewById(R.id.editTextReview);
        ratingBar = findViewById(R.id.ratingBar);
        Button buttonSubmit = findViewById(R.id.buttonSubmitReview);

        // Sets onClickListener to submit comment review
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    // Method to submit comment reviews
    private void submitReview() {
        String reviewText = editTextReview.getText().toString().trim();
        float rating = ratingBar.getRating();
        long userId = getCurrentUserId();

        // Checks if the review text is not empty
        // Sends a notification regarding comment submission confirmation, failure, and basic error handling
        if (!reviewText.isEmpty()) {
            DatabaseHelper dbHelper = new DatabaseHelper(CreateReview.this);
            boolean success = dbHelper.addReview(userId, reviewText, (int) rating);
            if (success) {
                // Notification to declare success
                Toast.makeText(CreateReview.this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                finish();  // Closes the activity after submission
            } else {
                // Notification to declare failure
                Toast.makeText(CreateReview.this, "Failed to submit review.", Toast.LENGTH_LONG).show();
            }
        } else {
            // Basic Error Handling
            // Notifies user that the text box is empty
            Toast.makeText(CreateReview.this, "Review cannot be empty.", Toast.LENGTH_LONG).show();
        }
    }

    // Temp method
    private long getCurrentUserId() {
        return 1;
    }
}
