package com.example.souffleteamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        Button btnViewComments = findViewById(R.id.btnViewComments);
        Button btnViewOrderFeedback = findViewById(R.id.btnViewOrderFeedback);

        // Sets onClickListener to bring user to Review.java page
        btnViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Review activity
                startActivity(new Intent(Feedback.this, Review.class));
            }
        });

        // Sets onClickListener to bring user to ViewOrderFeedback.java page
        btnViewOrderFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Feedback.this, ViewOrderFeedback.class);
                startActivity(intent);
            }
        });

    }
}
