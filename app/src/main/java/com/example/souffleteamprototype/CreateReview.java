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

        editTextReview = findViewById(R.id.editTextReview);
        ratingBar = findViewById(R.id.ratingBar);
        Button buttonSubmit = findViewById(R.id.buttonSubmitReview);

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
        long userId = getCurrentUserId();

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

    private long getCurrentUserId() {
        return 1;
    }
}
