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

    // Quantity of items selected
    private int quantity = 1;

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
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncreaseQuantity = findViewById(R.id.btnIncreaseQuantity);
        btnDecreaseQuantity = findViewById(R.id.btnDecreaseQuantity);
        btnViewCart = findViewById(R.id.btnViewCart);
        btnCustomerAccount = findViewById(R.id.btnCustomerAccount);

        // Sets onclick for btnCustomerAccount
        // Brings user to Customer Account page
        btnCustomerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the CustomerInfo activity
                Intent intent = new Intent(Menu.this, CustomerInfo.class);
                startActivity(intent);
            }
        });

        // Increases quantity of item
        btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                updateQuantityDisplay();
            }
        });

        // Decreases quantity of item
        btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) { // Ensure we don't go below 1
                    quantity--;
                    updateQuantityDisplay();
                }
            }
        });

        // Sets onclick for btnViewCart
        // Brings user to Cart page
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

    // Updates the quantity display
    private void updateQuantityDisplay() {
        tvQuantity.setText(String.format("Quantity: %d", quantity));
    }
}
