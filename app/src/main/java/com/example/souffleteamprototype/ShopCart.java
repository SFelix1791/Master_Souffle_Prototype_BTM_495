package com.example.souffleteamprototype;

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
    private Button btnApplyPromo;
    private final double TAX_RATE = 0.15;
    private final double DISCOUNT_RATE = 0.20;
    private double subtotal;
    private double discountAmount = 0.0;
    private boolean isDiscountApplied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        tvItemList = findViewById(R.id.tvItemList);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        tvDiscount = findViewById(R.id.tvDiscount); // Make sure you have a TextView with this ID
        etPromoCode = findViewById(R.id.etPromoCode); // And an EditText with this ID
        btnApplyPromo = findViewById(R.id.btnApplyPromo); // And a Button with this ID

        calculateItems();
        updateTotals();

        btnApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promoCode = etPromoCode.getText().toString();
                if ("CAKE20".equalsIgnoreCase(promoCode) && !isDiscountApplied) {
                    applyDiscount();
                    Toast.makeText(ShopCart.this, "Discount applied!", Toast.LENGTH_SHORT).show();
                    updateTotals(); // Update totals to show the discount
                } else if (isDiscountApplied) {
                    Toast.makeText(ShopCart.this, "Promo code already applied.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShopCart.this, "Invalid promo code.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calculateItems() {
        SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        Map<String, Integer> itemQuantities = new HashMap<>();
        subtotal = 0.0;

        int orderCount = prefs.getInt("orderCount", 0);
        for (int i = 1; i <= orderCount; i++) {
            String orderDetails = prefs.getString("order" + i, "");
            if (!orderDetails.isEmpty()) {
                // Assume orderDetails format is "quantity:price:topping"
                String[] parts = orderDetails.split(":");
                int quantity = Integer.parseInt(parts[0]);
                double price = Double.parseDouble(parts[1]);
                String topping = parts[2];

                // Create a key to uniquely identify the items
                String key = price + ":" + topping;

                // Update the quantity for the item
                int currentQuantity = itemQuantities.getOrDefault(key, 0);
                itemQuantities.put(key, currentQuantity + quantity);

                // Update the subtotal
                subtotal += price * quantity;
            }
        }

        // Build the item list string
        StringBuilder itemListText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String[] parts = entry.getKey().split(":");
            double price = Double.parseDouble(parts[0]);
            String topping = parts[1];
            int quantity = entry.getValue();

            itemListText.append(String.format(Locale.getDefault(),
                    "Item: Japanese Cheesecake Medium\nPrice: $%.2f each\nQuantity: %d\nTopping: %s\n\n",
                    price, quantity, topping));
        }
        tvItemList.setText(itemListText.toString());
    }

    private void applyDiscount() {
        isDiscountApplied = true;
        discountAmount = subtotal * DISCOUNT_RATE;
    }

    private void updateTotals() {
        double tax = subtotal * TAX_RATE;
        double total = subtotal - discountAmount + tax;
        tvSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        tvDiscount.setText(String.format(Locale.getDefault(), "-$%.2f", discountAmount));
        tvTax.setText(String.format(Locale.getDefault(), "Tax: $%.2f", tax));
        tvTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
    }
}
