package com.example.souffleteamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class ShoppingCart extends AppCompatActivity {

    private TextView tvItemList, tvSubtotal, tvTax, tvTotal;
    private EditText etPromoCode;
    private Button btnApplyPromo;
    private double price = 9.99; // Price for Cheesecake according to Uber Eats
    private int quantity;
    private final double TAX_RATE = 0.15; // 15% GST
    private final double DISCOUNT_RATE = 0.20; // 20% discount for promo code CAKE20
    private boolean isDiscountApplied = false; // Ensures discount is applied only once

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Initialize UI Elements
        tvItemList = findViewById(R.id.tvItemList);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        etPromoCode = findViewById(R.id.etPromoCode);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);

        // Get the quantity passed from Menu activity
        Intent intent = getIntent();
        quantity = intent.getIntExtra("quantity", 1); // Default to 1 if not found

        // Update the UI to show item details including the received quantity
        tvItemList.setText(String.format(Locale.getDefault(), "Item: Japanese Cheesecake Medium\nPrice: $%.2f each\nQuantity: %d", price, quantity));

        // Initially calculate and shows totals
        updateTotals();

        btnApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDiscountApplied) {
                    String promoCode = etPromoCode.getText().toString().trim();
                    if (promoCode.equalsIgnoreCase("CAKE20")) {
                        applyDiscount();
                        Toast.makeText(ShoppingCart.this, "Promo code applied successfully!", Toast.LENGTH_SHORT).show();
                        isDiscountApplied = true; // Prevents user from applying promo multiple times
                    } else {
                        Toast.makeText(ShoppingCart.this, "Invalid promo code.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShoppingCart.this, "Promo code has already been applied.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateTotals() {
        double subtotal = price * quantity;
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        if (isDiscountApplied) {
            total -= total * DISCOUNT_RATE; // Apply discount
        }

        tvSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        tvTax.setText(String.format(Locale.getDefault(), "$%.2f", tax));
        tvTotal.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }

    private void applyDiscount() {
        updateTotals(); // Recalculate totals with discount
    }
}
