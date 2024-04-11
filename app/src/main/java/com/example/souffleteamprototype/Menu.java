package com.example.souffleteamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {

    private Button btnCustomerAccount, btnMoveToCart; // Add btnMoveToCart declaration here
    private ImageButton btnMoveToItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        btnCustomerAccount = findViewById(R.id.btnCustomerAccount);
        btnMoveToItem = findViewById(R.id.btnMoveToItem); // Initialize ImageButton
        btnMoveToCart = findViewById(R.id.btnMoveToCart); // Initialize your Button for moving to the cart

        // Sets onclick for btnCustomerAccount
        // Brings user to Customer Account page
        btnCustomerAccount.setOnClickListener(v -> {
            // Start the CustomerInfo activity
            Intent intent = new Intent(Menu.this, CustomerInfo.class);
            startActivity(intent);
        });

        // Sets onclick for btnMoveToItem
        // Brings user to CheeseCakeItem page
        btnMoveToItem.setOnClickListener(v -> {
            // Start the CheeseCakeItem activity
            Intent intent = new Intent(Menu.this, CheeseCakeItem.class);
            startActivity(intent);
        });

        // Sets onclick for btnMoveToCart
        // Brings user to ShopCart page
        btnMoveToCart.setOnClickListener(v -> {
            // Start the ShopCart activity
            Intent intent = new Intent(Menu.this, ShopCart.class);
            startActivity(intent);
        });
    }
}
