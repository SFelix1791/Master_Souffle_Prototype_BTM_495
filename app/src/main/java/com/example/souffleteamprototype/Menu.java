package com.example.souffleteamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // Import ImageButton
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {

    private Button btnCustomerAccount;
    private ImageButton btnMoveToItem; // Change the type to ImageButton

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

        // Sets onclick for btnMoveToItem
        // Brings user to CheeseCakeItem page
        btnMoveToItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the CheeseCakeItem activity
                Intent intent = new Intent(Menu.this, CheeseCakeItem.class);
                startActivity(intent);
            }
        });
    }
}
