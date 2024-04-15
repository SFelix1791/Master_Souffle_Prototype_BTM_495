package com.example.souffleteamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProceedPayment extends AppCompatActivity {

    // Declare UI elements
    private Button btnProceedPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proceed_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        btnProceedPayment = findViewById(R.id.btnProceedPayment);

        // Since prototype is not connected to Clover payment system, it automatically accepts any input to proceed and brings user to OrderStatus.java page
        btnProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Brings user to OrderStatus activity
                Intent intent = new Intent(ProceedPayment.this, OrderStatus.class);
                startActivity(intent);

                // Notifies user of payment approval
                Toast.makeText(ProceedPayment.this, "Payment Approved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
