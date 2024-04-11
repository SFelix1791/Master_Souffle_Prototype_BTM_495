package com.example.souffleteamprototype;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerInfo extends AppCompatActivity {

    private TextView txtViewFirstName, txtViewLastName, txtViewEmail, txtViewCustomerId, txtViewPhone, txtViewPoints;
    private Button btnShowQR, btnMoveEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        // Initialize UI Elements
        txtViewFirstName = findViewById(R.id.txtViewFirstName);
        txtViewLastName = findViewById(R.id.txtViewLastName);
        txtViewEmail = findViewById(R.id.txtViewEmail);
        txtViewCustomerId = findViewById(R.id.txtViewCustomerId);
        txtViewPhone = findViewById(R.id.txtViewPhone); // Initialize the TextView for phone
        txtViewPoints = findViewById(R.id.txtViewPoints); // Initialize the TextView for points
        btnShowQR = findViewById(R.id.btnShowQR);
        btnMoveEditProfile = findViewById(R.id.btnMoveEditProfile);

        // Loads user info from SharedPreferences
        loadUserInfoFromSharedPreferences();

        btnShowQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to QRPage activity
                Intent intent = new Intent(CustomerInfo.this, QRPage.class);
                intent.putExtra("customerID", txtViewCustomerId.getText().toString());
                startActivity(intent);
            }
        });

        btnMoveEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EditProfileActivity
                Intent intent = new Intent(CustomerInfo.this, EditProfile.class);
                intent.putExtra("email", txtViewEmail.getText().toString());
                startActivity(intent);
            }
        });
    }

    // Method displays user info
    private void loadUserInfoFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String firstName = prefs.getString("firstName", "N/A");
        String lastName = prefs.getString("lastName", "N/A");
        String email = prefs.getString("email", "N/A");
        String customerId = prefs.getString("customerId", "N/A");
        String phone = prefs.getString("phone", "N/A"); // Retrieve the phone number
        int points = prefs.getInt("points", 0); // Retrieve the points

        txtViewFirstName.setText(String.format("%s", firstName));
        txtViewLastName.setText(String.format("%s", lastName));
        txtViewEmail.setText(String.format("%s", email));
        txtViewCustomerId.setText(String.format("Customer ID: %s", customerId));
        txtViewPhone.setText(String.format("Phone: %s", phone)); // Display the phone number
        txtViewPoints.setText(String.format("%d", points)); // Display the points
    }
}
