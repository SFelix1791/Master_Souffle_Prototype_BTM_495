package com.example.souffleteamprototype;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Locale;

public class ShopCart extends AppCompatActivity {

    private TextView tvItemList, tvSubtotal, tvTax, tvTotal;
    private final double TAX_RATE = 0.15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        tvItemList = findViewById(R.id.tvItemList);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);

        SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        int orderCount = prefs.getInt("orderCount", 0);
        StringBuilder itemListText = new StringBuilder();

        double subtotal = 0;

        for (int i = 1; i <= orderCount; i++) {
            String orderDetails = prefs.getString("order" + i, "");
            String[] parts = orderDetails.split(":");
            int quantity = Integer.parseInt(parts[0]);
            double price = Double.parseDouble(parts[1]);
            String topping = parts[2];

            subtotal += price * quantity;

            itemListText.append(String.format(Locale.getDefault(),
                    "Item: Japanese Cheesecake Medium\nPrice: $%.2f\nQuantity: %d\nTopping: %s\n\n",
                    price, quantity, topping));
        }

        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        tvItemList.setText(itemListText.toString());
        tvSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        tvTax.setText(String.format(Locale.getDefault(), "$%.2f", tax));
        tvTotal.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }
}