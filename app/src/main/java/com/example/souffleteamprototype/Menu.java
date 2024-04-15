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

    // Declares UI elements
    private Button btnMoveToCart;
    private ImageButton btnMoveToItem, btnCustomerAccount;

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
        btnMoveToItem = findViewById(R.id.btnMoveToItem);
        btnMoveToCart = findViewById(R.id.btnMoveToCart);

        // Set onClickListener to bring user to CustomerInfo.java page
        btnCustomerAccount.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, CustomerInfo.class);
            startActivity(intent);
        });

        // Set onClickListener to bring user to CheeseCakeItem.java page
        btnMoveToItem.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, CheeseCakeItem.class);
            startActivity(intent);
        });

        // Set onClickListener to bring user to ShopCart.java page
        btnMoveToCart.setOnClickListener(v -> {
            // Start the ShopCart activity
            Intent intent = new Intent(Menu.this, ShopCart.class);
            startActivity(intent);
        });
    }
}
