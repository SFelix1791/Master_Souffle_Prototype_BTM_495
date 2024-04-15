// ViewOrderFeedback.java
package com.example.souffleteamprototype;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewOrderFeedback extends AppCompatActivity {

    // Declare UI elements and DatabaseHelper
    private ListView listViewFeedback;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_feedback);

        // Initialize UI elements and DatabaseHelper
        listViewFeedback = findViewById(R.id.listViewFeedback);
        dbHelper = new DatabaseHelper(this);

        loadFeedback();

        Button btnBackToFeedback = findViewById(R.id.btnBackToFeedback);
        // Set onClickListener to bring user to Feedback.java page
        btnBackToFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Feedback activity
                Intent intent = new Intent(ViewOrderFeedback.this, Feedback.class);
                startActivity(intent);
                finish();  // Close the current activity
            }
        });
    }

    // Method loads the user's previous feedback
    private void loadFeedback() {
        long userId = getCurrentUserId(); // Fetch the current user's ID
        Cursor cursor = dbHelper.getFeedbackByUserId(userId); // Retrieve order feedback from database for the specific user
        List<String> feedbackList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // Assuming your feedback columns are in this order: 0 - Text, 1 - Rating
                String feedback = cursor.getString(0) + " - Rating: " + cursor.getInt(1);
                feedbackList.add(feedback);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (feedbackList.isEmpty()) {
            Toast.makeText(this, "No feedback available", Toast.LENGTH_LONG).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feedbackList);
            listViewFeedback.setAdapter(adapter);
        }
    }

    // Retrieves user's user ID
    private long getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return prefs.getLong("userId", -1); // Default to -1 if no user ID is found
    }

}
