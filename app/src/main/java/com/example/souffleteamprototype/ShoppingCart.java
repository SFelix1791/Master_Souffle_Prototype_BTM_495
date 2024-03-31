package com.example.souffleteamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class ShoppingCart extends AppCompatActivity {

    private TextView tvItemList, tvSubtotal, tvTax, tvTotal;
    private EditText etPromoCode;
    private Button btnApplyPromo;
    private double price = 9.99; // Price for Cheesecake according to Uber Eats
    private int quantity;
    private final double TAX_RATE = 0.15; // 15% tax
    private final double DISCOUNT_RATE = 0.20; // 20% discount for promo code CAKE20

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

        // Sets onclick for btnApplyPromo
        // Applies the promo code
        btnApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promoCode = etPromoCode.getText().toString();
                if ("CAKE20".equalsIgnoreCase(promoCode)) {
                    applyDiscount(DISCOUNT_RATE);
                } else {
                    etPromoCode.setError("Invalid promo code");
                }
            }
        });
    }
    // Method updates totals
    private void updateTotals() {
        double subtotal = calculateSubtotal(price, quantity);
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;
        tvSubtotal.setText(String.format(Locale.getDefault(), "Subtotal: $%.2f", subtotal));
        tvTax.setText(String.format(Locale.getDefault(), "Tax: $%.2f", tax));
        tvTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
    }
    // Method calculates subtotal
    private double calculateSubtotal(double price, int quantity) {
        return price * quantity;
    }

    // Method applies discount code
    private void applyDiscount(double discountRate) {
        double subtotal = calculateSubtotal(price, quantity);
        double discount = subtotal * discountRate;
        double discountedSubtotal = subtotal - discount;
        double tax = discountedSubtotal * TAX_RATE;
        double total = discountedSubtotal + tax;
        tvSubtotal.setText(String.format(Locale.getDefault(), "Subtotal: $%.2f", discountedSubtotal));
        tvTax.setText(String.format(Locale.getDefault(), "Tax: $%.2f", tax));
        tvTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
    }
}
