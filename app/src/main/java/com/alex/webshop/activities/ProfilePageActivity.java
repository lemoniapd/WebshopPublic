package com.alex.webshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.webshop.R;
import com.alex.webshop.model.Customer;
import com.alex.webshop.model.Product;
import com.alex.webshop.service.DataHandler;

import java.util.List;
import java.util.stream.Collectors;

public class ProfilePageActivity extends AppCompatActivity {
    private Customer currentCustomer;
    private List<Product> allProductsInDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Grab access to data being sent to this view with putExtra()
        Intent i = getIntent();
        currentCustomer = (Customer) i.getSerializableExtra("customerProfile");
        Log.d("webshopen", "Profile page Customer: " + currentCustomer.toString());

        String fullName = currentCustomer.getFirstName() + " " + currentCustomer.getLastName();

        // Changing title of Activity
        setTitle("Welcome " + fullName);

        // Hämtar alla Produkter från db
        fetchAllProducts();

        // Load Ui
        loadProductsOnUi(allProductsInDb);

        // Refresh Listener
        swipeToRefreshListener();
    }

    // Swipe to refresh
    public void swipeToRefreshListener() {
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> handleRefresh(refreshLayout));
    }

    public void fetchAllProducts() {
        DataHandler dataHandler = new DataHandler();
        allProductsInDb = dataHandler.getAllProductsInDb();
    }

    public void handleRefresh(SwipeRefreshLayout refreshLayout) {
        GridLayout theGrid = findViewById(R.id.productGrid);
        theGrid.removeAllViews();

        fetchAllProducts();
        loadProductsOnUi(allProductsInDb);
        theGrid.invalidate();
        refreshLayout.setRefreshing(false);

        ScrollView sv = findViewById(R.id.scrollView);
        sv.fullScroll(ScrollView.FOCUS_UP); // scroll to top on swipe to refresh

        Toast.makeText(this, "Page Refreshed!", Toast.LENGTH_SHORT).show();
    }

    private void turnOffLoadingSpinner(boolean value) {
        ProgressBar bar = findViewById(R.id.progressBar);
        int valueToSet = value ? View.GONE : View.VISIBLE;
        bar.setVisibility(valueToSet);
    }

    @SuppressLint("SetTextI18n")
    private void addProductsToGrid(Product product) {
        // Fetch the grid object
        GridLayout theGrid = findViewById(R.id.productGrid);

        //TextView
        TextView textView = new TextView(this);
        textView.setTextSize(20F);
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsText.setMargins(30, 30, 30, 30);
        textView.setText(product.getProductDescription());
        textView.setLayoutParams(layoutParamsText);

        // CardView
        LinearLayout.LayoutParams layoutParamsCard = new LinearLayout.LayoutParams(450, 450);
        layoutParamsCard.gravity = Gravity.CENTER;
        CardView cardView = new CardView(this);
        cardView.setCardBackgroundColor(Color.parseColor("#ECF9FF"));
        cardView.setLayoutParams(layoutParamsCard);
        cardView.setRadius(30);
        layoutParamsCard.setMargins(20, 20, 20, 20);

        // onClick listener på varje CardView
        cardView.setOnClickListener(view -> openDetailedProductView(product));

        // Lägg in TextView I CardView
        cardView.addView(textView, layoutParamsText);

        // Lägg in allt i GridView
        theGrid.addView(cardView, layoutParamsCard);
    }

    // Opens new Page with detailed View about a spec product + AddToCart
    private void openDetailedProductView(Product product) {
        Intent i = new Intent(this, ProductInfoDetailedActivity.class);
        i.putExtra("customerProfile", currentCustomer);
        i.putExtra("currentProduct", product);
        Bundle animationBundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(i, animationBundle);
    }

    private void loadProductsOnUi(List<Product> listToShow) {
        turnOffLoadingSpinner(false);

        listToShow.stream()
                .limit(20) // We only need to show 20 on welcome screen
                .forEach(product -> addProductsToGrid(product));

        turnOffLoadingSpinner(true);
    }

    private void loadSearchResultOnUI(List<Product> result) {
        if (result.size() > 0) {

            turnOffLoadingSpinner(false);

            // Fetch the grid object and clear its Views
            GridLayout theGrid = findViewById(R.id.productGrid);
            CardView cardView = (CardView) theGrid.getChildAt(0);
            theGrid.removeAllViews();

            // Load SearchResult list
            result.forEach(product -> addProductsToGrid(product));

            // onClick listener på varje CardView
            // parallelStream for better performance
            result.forEach(product -> cardView.setOnClickListener(view -> openDetailedProductView(product)));

            // Refresh Grid
            theGrid.invalidate();

            turnOffLoadingSpinner(true);
        } else {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchedProducts(View view) {
        TextView searchView = findViewById(R.id.search);
        String searchedText = searchView.getText().toString().trim();
        Log.d("webshopen", searchedText);

        if (!(searchedText.isEmpty())) {
            loadSearchResultOnUI(fetchProductsFromSearch(searchedText));
            searchView.setText("");
        } else {
            Toast.makeText(this, "Cant be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean filterProductsFromSearchInputString(String searchedText, Product product) {
        return product.getName().equalsIgnoreCase(searchedText)
                || product.getBrand().getName().equalsIgnoreCase(searchedText)
                || product.getColor().getName().equalsIgnoreCase(searchedText)
                || product.getCategory().stream()
                .anyMatch(category -> category.getName().equalsIgnoreCase(searchedText));
    }

    private boolean filterProductsFromSearchInputInt(String searchedText, Product product) {
        return product.getEuSize() == Integer.parseInt(searchedText);
    }

    private List<Product> fetchProductsFromSearch(String searchedText) {
        if (isDigit(searchedText)) {
            return allProductsInDb.stream()
                    .filter(product -> filterProductsFromSearchInputInt(searchedText, product))
                    .collect(Collectors.toList());
        } else {
            return allProductsInDb.stream()
                    .filter(product -> filterProductsFromSearchInputString(searchedText, product))
                    .collect(Collectors.toList());
        }
    }

    private boolean isDigit(String digit) {
        try {
            Integer.parseInt(digit);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
