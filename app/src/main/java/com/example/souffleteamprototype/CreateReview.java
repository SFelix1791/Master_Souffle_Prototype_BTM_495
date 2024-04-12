package com.example.souffleteamprototype;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateReview extends AppCompatActivity {

    private EditText editTextReview;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        editTextReview = findViewById(R.id.editTextReview);  // Assuming you have an EditText with this ID
        ratingBar = findViewById(R.id.ratingBar);  // Assuming you have a RatingBar with this ID
        Button buttonSubmit = findViewById(R.id.buttonSubmitReview);  // Assuming you have a Button with this ID

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void submitReview() {
        String reviewText = editTextReview.getText().toString().trim();
        float rating = ratingBar.getRating();
        long userId = getCurrentUserId();  // You need to implement this method to retrieve the user ID

        if (!reviewText.isEmpty()) {
            DatabaseHelper dbHelper = new DatabaseHelper(CreateReview.this);
            boolean success = dbHelper.addReview(userId, reviewText, (int) rating);
            if (success) {
                Toast.makeText(CreateReview.this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                finish();  // Close the activity after submission
            } else {
                Toast.makeText(CreateReview.this, "Failed to submit review.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CreateReview.this, "Review cannot be empty.", Toast.LENGTH_LONG).show();
        }
    }

    // Placeholder for method to retrieve current user ID
    private long getCurrentUserId() {
        // This method should retrieve the current logged-in user's ID
        // Example:
        // SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        // return prefs.getLong("userId", -1);
        return 1;  // Just an example, you need to implement it based on your user session management
    }
}
