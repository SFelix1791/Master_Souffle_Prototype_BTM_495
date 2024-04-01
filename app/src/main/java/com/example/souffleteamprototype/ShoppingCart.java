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
    private TextView tvItemList, tvSubtotal, tvTax, tvTotal, tvDiscount;
    private EditText etPromoCode;
    private Button btnApplyPromo;
    private double price = 9.99;
    private int quantity;
    private final double TAX_RATE = 0.15;
    private final double DISCOUNT_RATE = 0.20;
    private boolean isDiscountApplied = false;
    private double discountAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        tvItemList = findViewById(R.id.tvItemList);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        etPromoCode = findViewById(R.id.etPromoCode);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);

        Intent intent = getIntent();
        quantity = intent.getIntExtra("quantity", 1);

        tvItemList.setText(String.format(Locale.getDefault(), "Item: Japanese Cheesecake Medium\nPrice: $%.2f each\nQuantity: %d", price, quantity));

        updateTotals();

        btnApplyPromo.setOnClickListener(v -> {
            if (!isDiscountApplied) {
                String promoCode = etPromoCode.getText().toString().trim();
                if ("CAKE20".equalsIgnoreCase(promoCode)) {
                    applyDiscount();
                    Toast.makeText(ShoppingCart.this, "Promo code applied successfully!", Toast.LENGTH_SHORT).show();
                    isDiscountApplied = true;
                } else {
                    Toast.makeText(ShoppingCart.this, "Invalid promo code.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ShoppingCart.this, "Promo code has already been applied.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotals() {
        double subtotal = price * quantity;
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;
        if (isDiscountApplied) {
            discountAmount = total * DISCOUNT_RATE;
            total -= discountAmount;
        }
        tvSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        tvTax.setText(String.format(Locale.getDefault(), "$%.2f", tax));
        tvDiscount.setText(String.format(Locale.getDefault(), "$%.2f", discountAmount));
        tvTotal.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }

    private void applyDiscount() {
        isDiscountApplied = true;
        updateTotals();
    }
}

