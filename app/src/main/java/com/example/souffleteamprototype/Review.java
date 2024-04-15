package com.example.souffleteamprototype;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Review extends AppCompatActivity {

    // Declare UI elements
    private ListView listViewReviews;
    private Button btnMoveToCreateReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Initialize UI elements
        listViewReviews = findViewById(R.id.listViewReviews);
        btnMoveToCreateReview = findViewById(R.id.btnMoveToCreateReview);

        // Loads the previous user comment reviews/feedback
        loadUserReviews();

        // Set onClickListener to bring user to CreateReview.java where they can submit a new comment review
        btnMoveToCreateReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CreateReview activity
                Intent intent = new Intent(Review.this, CreateReview.class);
                startActivity(intent);
            }
        });
    }

    // Method loads previous user comment reviews from Database
    private void loadUserReviews() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        long userId = getCurrentUserId();
        Cursor cursor = dbHelper.getReviewsByUserId(userId);

        ArrayList<String> reviews = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            int reviewTextIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_REVIEW_TEXT);
            int ratingIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RATING);

            if (reviewTextIndex != -1 && ratingIndex != -1) { // Check if indices are valid
                do {
                    String reviewText = cursor.getString(reviewTextIndex);
                    int rating = cursor.getInt(ratingIndex);
                    reviews.add(reviewText + " - Rating: " + rating);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reviews);
        listViewReviews.setAdapter(adapter);
    }

    // Retrieves User ID
    private long getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return prefs.getLong("userId", -1); // Default to -1 if no user ID is found
    }
}
