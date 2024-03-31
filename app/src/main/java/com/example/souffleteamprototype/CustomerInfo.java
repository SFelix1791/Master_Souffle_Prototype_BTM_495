package com.example.souffleteamprototype;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerInfo extends AppCompatActivity {

    private TextView txtViewFirstName, txtViewLastName, txtViewEmail, txtViewCustomerId;
    private Button btnShowQR, btnMoveEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        // Initialize TextViews
        txtViewFirstName = findViewById(R.id.txtViewFirstName);
        txtViewLastName = findViewById(R.id.txtViewLastName);
        txtViewEmail = findViewById(R.id.txtViewEmail);
        txtViewCustomerId = findViewById(R.id.txtViewCustomerId);

        // Initialize the buttons and set their click listeners
        btnShowQR = findViewById(R.id.btnShowQR);
        btnShowQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to QRPage activity
                Intent intent = new Intent(CustomerInfo.this, QRPage.class);
                intent.putExtra("customerID", txtViewCustomerId.getText().toString());
                startActivity(intent);
            }
        });

        btnMoveEditProfile = findViewById(R.id.btnMoveEditProfile);
        btnMoveEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EditProfileActivity
                Intent intent = new Intent(CustomerInfo.this, EditProfile.class);
                // Optionally, pass the email as an extra if needed for fetching user info
                intent.putExtra("email", txtViewEmail.getText().toString());
                startActivity(intent);
            }
        });

        // Load user info from SharedPreferences
        loadUserInfoFromSharedPreferences();
    }

    private void loadUserInfoFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String firstName = prefs.getString("firstName", "N/A");
        String lastName = prefs.getString("lastName", "N/A");
        String email = prefs.getString("email", "N/A");
        String customerId = prefs.getString("customerId", "N/A");

        txtViewFirstName.setText(String.format("First Name: %s", firstName));
        txtViewLastName.setText(String.format("Last Name: %s", lastName));
        txtViewEmail.setText(String.format("%s", email));
        txtViewCustomerId.setText(String.format("Customer ID: %s", customerId));
    }
}
