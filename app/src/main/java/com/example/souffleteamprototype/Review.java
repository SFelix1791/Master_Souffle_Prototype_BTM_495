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

    private ListView listViewReviews;
    private Button btnMoveToCreateReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review); // Make sure you have a ListView in this layout with the id 'listViewReviews'

        listViewReviews = findViewById(R.id.listViewReviews);
        btnMoveToCreateReview = findViewById(R.id.btnMoveToCreateReview);

        loadUserReviews();

        btnMoveToCreateReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CreateReview activity
                Intent intent = new Intent(Review.this, CreateReview.class);
                startActivity(intent);
            }
        });
    }

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

    // Placeholder method to get the current user's ID
    private long getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return prefs.getLong("userId", -1); // Default to -1 if no user ID is found
    }
}
