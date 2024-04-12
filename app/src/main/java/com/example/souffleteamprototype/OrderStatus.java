package com.example.souffleteamprototype;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_status);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the btnModifyOrder button by its ID
        Button btnModifyOrder = findViewById(R.id.btnModifyOrder);

        // Set an OnClickListener for the btnModifyOrder button
        btnModifyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ShopCart.java activity
                Intent intent = new Intent(OrderStatus.this, ShopCart.class);
                startActivity(intent);

                // Display Order Modification Request Status notification
                // Since there is no Staff interface in the prototype, it is automatically approved
                Toast.makeText(OrderStatus.this, "Order Modification Request Approved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}