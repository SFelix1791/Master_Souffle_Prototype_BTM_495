package com.example.souffleteamprototype;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShopCart extends AppCompatActivity {

    //Declare UI elements and constants/variables relevant to the cart state
    private TextView tvItemList, tvSubtotal, tvTax, tvTotal, tvDiscount;
    private EditText etPromoCode;
    private Button btnApplyPromo, btnCheckout, btnClearCart;
    private ImageButton btnBackMenu;
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

        initViews();

        cartItems = new HashMap<>();

        calculateItems();
        updateTotals();

        setupListeners();
    }

    // Initialize the elements in the layout
    private void initViews() {
        tvItemList = findViewById(R.id.tvItemList);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        etPromoCode = findViewById(R.id.etPromoCode);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnClearCart = findViewById(R.id.btnClearCart);
        btnBackMenu = findViewById(R.id.btnBackMenu);
    }

    // Set onClick Listener for the following buttons
    // Displayed this way for visual clarity
    private void setupListeners() {
        btnApplyPromo.setOnClickListener(v -> applyPromoCode());
        btnCheckout.setOnClickListener(v -> checkout());
        btnClearCart.setOnClickListener(v -> clearCart());
        btnBackMenu.setOnClickListener(v -> backToMenu());
    }

    // Method brings user back to Menu.java page
    private void backToMenu() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    // Method applies Promotion Code
    private void applyPromoCode() {
        String promoCode = etPromoCode.getText().toString();
        if ("CAKE20".equalsIgnoreCase(promoCode) && !isDiscountApplied) {
            applyDiscount();
            Toast.makeText(this, "Discount applied!", Toast.LENGTH_SHORT).show();
            updateTotals();
        } else {
            Toast.makeText(this, isDiscountApplied ? "Promo code already applied." : "Invalid promo code.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method brings user to Checkout page which is the ProceedPayment.java page
    private void checkout() {
        Intent intent = new Intent(this, ProceedPayment.class);
        startActivity(intent);
    }

    // Method clears the cart and resets the price variables
    private void clearCart() {
        SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        cartItems.clear();
        subtotal = 0.0;
        discountAmount = 0.0;  // Reset discount amount
        isDiscountApplied = false;  // Reset discount application flag
        displayCartItems();
        updateTotals();

        tvDiscount.setText("$0.00");
        Toast.makeText(this, "Cart cleared.", Toast.LENGTH_SHORT).show();
    }

    // Method calculates items in the shopping cart
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

                CheesecakeItemWithQuantity item = cartItems.getOrDefault(topping, new CheesecakeItemWithQuantity(price, topping, 0));
                item.quantity += quantity;
                cartItems.put(topping, item);

                subtotal += price * quantity;
            }
        }

        displayCartItems();
    }

    // Method displays items in the cart and it's details
    private void displayCartItems() {
        StringBuilder itemListText = new StringBuilder();
        for (CheesecakeItemWithQuantity item : cartItems.values()) {
            itemListText.append(String.format(Locale.getDefault(),
                    "Item: Japanese Cheesecake Medium\nPrice: $%.2f each\nQuantity: %d\nTopping: %s\n\n",
                    item.price, item.quantity, item.topping));
        }
        tvItemList.setText(itemListText.toString());
    }

    // Method applies the discount rate
    private void applyDiscount() {
        isDiscountApplied = true;
        discountAmount = subtotal * DISCOUNT_RATE;
        tvDiscount.setText(String.format(Locale.getDefault(), "-$%.2f", discountAmount));
    }

    // Method updates total, tax, and subtotal
    private void updateTotals() {
        double tax = subtotal * TAX_RATE;
        double total = subtotal - discountAmount + tax;
        tvSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        tvTax.setText(String.format(Locale.getDefault(), "$%.2f", tax));
        tvTotal.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }

    // This represents cheesecake item
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
