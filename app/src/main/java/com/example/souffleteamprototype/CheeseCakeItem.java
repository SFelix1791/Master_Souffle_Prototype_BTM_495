package com.example.souffleteamprototype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CheeseCakeItem extends AppCompatActivity {

    // Declare UI Components
    private CheckBox cbChocolate;
    private TextView txtQuantity;
    private Button btnAddToCart, btnViewCart, btnIncrease, btnDecrease;
    private final double price = 9.99;
    private SharedPreferences cartPreferences;
    private static final String CART_PREFS = "CartPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cheese_cake_item);

        // Setting up WindowInsets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        // Initialize SharedPreferences to store Shopping Cart information
        cartPreferences = this.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);

        // Initialize UI components
        cbChocolate = findViewById(R.id.cbChocolate);
        txtQuantity = findViewById(R.id.txtQuantity);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnViewCart = findViewById(R.id.btnViewCart);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        // Set onClickListener to increase quantity of item
        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            txtQuantity.setText(String.valueOf(quantity + 1));
        });

        // Set onClickListener to decrease quantity of the item
        // Ensures quantity does not go below 1
        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            if (quantity > 1) {
                txtQuantity.setText(String.valueOf(quantity - 1));
            }
        });

        // Set onClickListener to add the Cheese Cake item to the cart with the current quantity and selected toppings
        btnAddToCart.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            int newOrderIndex = cartPreferences.getInt("orderCount", 0) + 1;
            String toppingChoice = cbChocolate.isChecked() ? "Chocolate" : "None";

            // Concatenate order details into a single string with the updated quantity
            String orderDetails = quantity + ":" + price + ":" + toppingChoice;
            cartPreferences.edit()
                    .putString("order" + newOrderIndex, orderDetails)
                    .putInt("orderCount", newOrderIndex)
                    .apply();

            // "Item added to Cart" Notification
            Toast.makeText(CheeseCakeItem.this, "Item added to cart", Toast.LENGTH_SHORT).show();

            // Brings user to Menu.java page
            startActivity(new Intent(CheeseCakeItem.this, Menu.class));
        });

        // Sets onClickListener to bring user to ShopCart.java page
        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(CheeseCakeItem.this, ShopCart.class);
            startActivity(intent);
        });
    }
}
