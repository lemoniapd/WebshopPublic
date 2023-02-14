package com.alex.webshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alex.webshop.R;
import com.alex.webshop.model.Category;
import com.alex.webshop.model.Customer;
import com.alex.webshop.model.Order;
import com.alex.webshop.model.Product;
import com.alex.webshop.repository.FetchAddToCartAsyncTask;
import com.alex.webshop.service.DataHandler;
import com.alex.webshop.service.Response;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class ProductInfoDetailedActivity extends AppCompatActivity {
    private Customer currentCustomer;

    private Product currentProduct;

    private List<Order> allOrdersInDb;


    // TODO OM MAN ANGER INTE ETT ORDER NR SOM TILLHÖR SIG SJÄLV, TOAST FEL!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info_detailed);

        // Grab access to data being sent to this view with putExtra()
        Intent i = getIntent();
        currentCustomer = (Customer) i.getSerializableExtra("customerProfile");
        currentProduct = (Product) i.getSerializableExtra("currentProduct");
        Log.d("webshopen", "Customer object: " + currentCustomer.toString());
        Log.d("webshopen", "Product: " + currentProduct.toString());

        setTitle("Product: " + currentProduct.getBrand().getName() + " " + currentProduct.getName());

        loadProduct();
        handleGetAllOrders();


        // För egen Debugging
        List<Order> test = allOrdersInDb.stream()
                .filter(order -> order.getId() == 7)
                .collect(Collectors.toList());
        Log.d("webshopen", "Order 7 TEST : " + test);
        Log.d("webshopen", "Order 7 TEST: " + test.size());
        Log.d("webshopen", "Order 7 Products TEST: " + test.get(0).getListOfProducts());
        Log.d("webshopen", "Order 7 Product Size TEST: " + test.get(0).getListOfProducts().size());

    }

    private void handleGetAllOrders() {
        DataHandler dataHandler = new DataHandler();
        allOrdersInDb = dataHandler.getAllOrdersInDb();
        Log.d("webshopen", "All Orders: " + allOrdersInDb.toString());
        Log.d("webshopen", "All Orders Size: " + allOrdersInDb.size());
    }

    public void handleAddToCartButtonListener(View view) {
        Button addToCartBtn = findViewById(R.id.addToCartBtn);
        TextView orderEnteredByCustomer = findViewById(R.id.orderNumber);
        String input = orderEnteredByCustomer.getText().toString();

        addToCartBtn.setEnabled(false);

        if (input.isEmpty()) {
            handleAddToCart(orderEnteredByCustomer, Integer.MAX_VALUE); // Kommer ej finnas så den gör en ny order om tom sträng
        } else if (isOrderNumberLessThanNineDigits(input) && checkIfCustomerOwnsThisOrder(input)) {
            int orderId = Integer.parseInt(orderEnteredByCustomer.getText().toString());
            handleAddToCart(orderEnteredByCustomer, orderId);
        } else if (isOrderNumberLessThanNineDigits(input) && !checkIfOrderNrDoesntExistInDb(input)) {
            handleAddToCart(orderEnteredByCustomer, Integer.MAX_VALUE);// Lägger kund ett order nr som ej finns i db så skapas en ny order med automatgenerat order id
        } else {
            Toast.makeText(this, "Try another order nr!", Toast.LENGTH_SHORT).show();
        }
        addToCartBtn.setEnabled(true);

    }

    // Skriver man över Integer.MAX_VALUE får vi Numberformat Exception, så nu kan vi undvika det
    private boolean isOrderNumberLessThanNineDigits(String input) {
        return input.length() < 9;
    }

    private boolean checkIfOrderNrDoesntExistInDb(String input) {
        return allOrdersInDb.stream()
                .anyMatch(order -> order.getId() == Integer.parseInt(input));
    }

    private boolean checkIfCustomerOwnsThisOrder(String input) {
        return allOrdersInDb.stream()
                .filter(order -> order.getCustomer().getId() == currentCustomer.getId())
                .anyMatch(order -> order.getId() == Integer.parseInt(input));
    }

    private void playSuccessAnimation() {
        LottieAnimationView ctoBanner = findViewById(R.id.ctoBanner);
        ctoBanner.setAnimation(R.raw.order_success);
        ctoBanner.playAnimation();
    }

    private void playErrorAnimation() {
        LottieAnimationView ctoBanner = findViewById(R.id.ctoBanner);
        ctoBanner.setAnimation(R.raw.error_cat);
        ctoBanner.playAnimation();
    }

    // Efter man klickar på lägg till order så skall amount ändras på cardview så vi upd productDesc mha currentProduct objekt
    @SuppressLint("SetTextI18n")
    private void refreshCardView() {
        int currentStock = currentProduct.getStock();
        currentProduct.setStock(currentStock - 1);

        CardView productCard = findViewById(R.id.productCard);
        TextView desc = (TextView) productCard.getChildAt(0);
        desc.setText(currentProduct.getProductDescription() + "\nCategories: " + getCategoriesStringByProduct(currentProduct));
        productCard.invalidate();
    }

    private void handleAddToCart(TextView orderEnteredByCustomer, int orderId) {
        try {
            Response response = new FetchAddToCartAsyncTask(currentCustomer, orderId, currentProduct).execute().get();
            if (response.isSuccessful()) {
                orderEnteredByCustomer.setText("");
                playSuccessAnimation();
                refreshCardView();
            } else {
                playErrorAnimation();
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadProduct() {
        CardView cardView = findViewById(R.id.productCard);
        //TextView
        TextView textView = new TextView(this);
        textView.setTextSize(20F);
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsText.setMargins(30, 30, 30, 30);
        textView.setText(currentProduct.getProductDescription() + "\nCategories: " + getCategoriesStringByProduct(currentProduct));
        textView.setLayoutParams(layoutParamsText);

        cardView.setCardBackgroundColor(Color.parseColor("#ECF9FF"));

        cardView.addView(textView);
    }

    private String getCategoriesStringByProduct(Product currentProduct) {
        return currentProduct.getCategory().stream().map(Category::getName).collect(Collectors.joining(", ", "", ""));
    }
}