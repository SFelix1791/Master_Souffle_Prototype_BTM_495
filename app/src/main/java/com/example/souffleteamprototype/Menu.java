package com.example.souffleteamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {

    private TextView tvQuantity;
    private Button btnIncreaseQuantity, btnDecreaseQuantity, btnViewCart, btnCustomerAccount;
    private int quantity = 1; // Starting with 1 item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find UI elements by ID
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncreaseQuantity = findViewById(R.id.btnIncreaseQuantity);
        btnDecreaseQuantity = findViewById(R.id.btnDecreaseQuantity);
        btnViewCart = findViewById(R.id.btnViewCart);
        btnCustomerAccount = findViewById(R.id.btnCustomerAccount);

        // Set OnClickListener for Customer Account button
        btnCustomerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the CustomerInfo activity
                Intent intent = new Intent(Menu.this, CustomerInfo.class);
                startActivity(intent);
            }
        });

        // Listener to increase the quantity
        btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                updateQuantityDisplay();
            }
        });

        // Listener to decrease the quantity
        btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) { // Ensure we don't go below 1
                    quantity--;
                    updateQuantityDisplay();
                }
            }
        });

        // Listener for the "View Cart" button
        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, ShoppingCart.class);
                // Pass the quantity to ShoppingCart
                intent.putExtra("quantity", quantity);
                startActivity(intent);
            }
        });
    }

    // Helper method to update the quantity display
    private void updateQuantityDisplay() {
        tvQuantity.setText(String.format("Quantity: %d", quantity));
    }
}
