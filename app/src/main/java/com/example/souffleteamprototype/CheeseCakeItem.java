package com.example.souffleteamprototype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CheeseCakeItem extends AppCompatActivity {

    private CheckBox cbChocolate;
    private Button btnAddToCart;
    private Button btnViewCart; // New button to view the cart
    private final double price = 9.99;
    private SharedPreferences cartPreferences;
    private static final String CART_PREFS = "CartPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cheese_cake_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        cartPreferences = this.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);

        cbChocolate = findViewById(R.id.cbChocolate);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnViewCart = findViewById(R.id.btnViewCart);

        btnAddToCart.setOnClickListener(v -> {
            // Generate a unique order ID
            int newOrderIndex = cartPreferences.getInt("orderCount", 0) + 1;
            String toppingChoice = cbChocolate.isChecked() ? "Chocolate" : "None";

            // Concatenate order details into a single string
            String orderDetails = "1:" + price + ":" + toppingChoice;
            cartPreferences.edit().putString("order" + newOrderIndex, orderDetails)
                    .putInt("orderCount", newOrderIndex)
                    .apply();

            Toast.makeText(CheeseCakeItem.this, "Item added to cart", Toast.LENGTH_SHORT).show();
        });

        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(CheeseCakeItem.this, ShopCart.class);
            startActivity(intent);
        });
    }
}
