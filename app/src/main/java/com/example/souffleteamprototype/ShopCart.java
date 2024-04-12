package com.example.souffleteamprototype;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShopCart extends AppCompatActivity {

    private TextView tvItemList, tvSubtotal, tvTax, tvTotal, tvDiscount;
    private EditText etPromoCode;
    private Button btnApplyPromo, btnCheckout;
    private final double TAX_RATE = 0.15;
    private final double DISCOUNT_RATE = 0.20;
    private double subtotal;
    private double discountAmount = 0.0;
    private boolean isDiscountApplied = false;
    private Map<String, CheesecakeItemWithQuantity> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        tvItemList = findViewById(R.id.tvItemList);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        etPromoCode = findViewById(R.id.etPromoCode);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);
        btnCheckout = findViewById(R.id.btnCheckout);

        cartItems = new HashMap<>();

        calculateItems();
        updateTotals();

        btnApplyPromo.setOnClickListener(v -> {
            String promoCode = etPromoCode.getText().toString();
            if ("CAKE20".equalsIgnoreCase(promoCode) && !isDiscountApplied) {
                applyDiscount();
                Toast.makeText(ShopCart.this, "Discount applied!", Toast.LENGTH_SHORT).show();
                updateTotals();
            } else if (isDiscountApplied) {
                Toast.makeText(ShopCart.this, "Promo code already applied.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShopCart.this, "Invalid promo code.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(ShopCart.this, ProceedPayment.class);
            startActivity(intent);
        });
    }

    private void calculateItems() {
        SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        cartItems.clear();
        subtotal = 0.0;

        int orderCount = prefs.getInt("orderCount", 0);
        for (int i = 1; i <= orderCount; i++) {
            String orderDetails = prefs.getString("order" + i, "");
            if (!orderDetails.isEmpty()) {
                String[] parts = orderDetails.split(":");
                int quantity = Integer.parseInt(parts[0]);
                double price = Double.parseDouble(parts[1]);
                String topping = parts[2];
                String key = topping;

                CheesecakeItemWithQuantity item = cartItems.getOrDefault(key, new CheesecakeItemWithQuantity(price, topping, 0));
                item.quantity += quantity;
                cartItems.put(key, item);

                subtotal += price * quantity;
            }
        }

        displayCartItems();
    }

    private void displayCartItems() {
        StringBuilder itemListText = new StringBuilder();
        for (CheesecakeItemWithQuantity item : cartItems.values()) {
            itemListText.append(String.format(Locale.getDefault(),
                    "Item: Japanese Cheesecake Medium\nPrice: $%.2f each\nQuantity: %d\nTopping: %s\n\n",
                    item.price, item.quantity, item.topping));
        }
        tvItemList.setText(itemListText.toString());
    }

    private void applyDiscount() {
        isDiscountApplied = true;
        discountAmount = subtotal * DISCOUNT_RATE;
        tvDiscount.setText(String.format(Locale.getDefault(), "-$%.2f", discountAmount));
    }

    private void updateTotals() {
        double tax = subtotal * TAX_RATE;
        double total = subtotal - discountAmount + tax;
        tvSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        tvTax.setText(String.format(Locale.getDefault(), "$%.2f", tax));
        tvTotal.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }

    private static class CheesecakeItemWithQuantity {
        double price;
        String topping;
        int quantity;

        CheesecakeItemWithQuantity(double price, String topping, int quantity) {
            this.price = price;
            this.topping = topping;
            this.quantity = quantity;
        }
    }
}
