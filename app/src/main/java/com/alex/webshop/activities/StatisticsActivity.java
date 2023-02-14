package com.alex.webshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.alex.webshop.R;
import com.alex.webshop.dto.AmountOfOrdersPerCustomerDTO;
import com.alex.webshop.dto.TopSellingProductDTO;
import com.alex.webshop.dto.TotalSpendPerCityDTO;
import com.alex.webshop.dto.TotalSpendPerCustomerDTO;
import com.alex.webshop.model.Customer;
import com.alex.webshop.model.Order;
import com.alex.webshop.model.Product;
import com.alex.webshop.service.DataHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class StatisticsActivity extends AppCompatActivity {
    private final DataHandler dataHandler = new DataHandler();
    private final List<Order> allOrders = dataHandler.getAllOrdersInDb();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Intent i = getIntent();
        String chosenMenu = i.getStringExtra("menuChoice");

        loadCorrectGUI(chosenMenu);
    }

    // Här Målas korrekt GUI upp beroende på vad för Menu val man gjorde på sidan innan
    private void loadCorrectGUI(String chosenMenu) {
        switch (chosenMenu) {
            case "All customers that have bought specific product":
                Log.d("webshopen", "All customers that have bought specific product");
                loadAllCustomersSpecificProductGUI();
                break;
            case "Amount of orders per Customer":
                Log.d("webshopen", "Amount of orders per Customer");
                loadAmountOfOrdersPerCustomerGUI();
                break;
            case "Total spend per Customer":
                Log.d("webshopen", "Total spend per Customer");
                loadTotalSpendPerCustomerGUI();
                break;
            case "Total spend per City":
                Log.d("webshopen", "Total spend per City");
                loadTotalSpendPerCityGUI();
                break;
            case "Top 3 selling Products":
                Log.d("webshopen", "Top selling Products");
                loadTopSellingProductsGUI();
                break;
        }
    }

    //To keep code dry
    private void makeGUISearchless() {
        TextView tv = findViewById(R.id.searchWord);
        Button resultBTN = findViewById(R.id.statsBtnSearch);
        tv.setVisibility(View.INVISIBLE);
        resultBTN.setVisibility(View.INVISIBLE);
    }

    // När vi valt detta menyval så ändras Sökrutans Hint för att bättre hjälpa användaren att göra slagningar
    private void loadAllCustomersSpecificProductGUI() {
        TextView tv = findViewById(R.id.searchWord);
        tv.setHint("Size / Color / Brand");
    }

    private void loadAmountOfOrdersPerCustomerGUI() {
        LottieAnimationView searchBanner = findViewById(R.id.searchBanner);
        searchBanner.setAnimation(R.raw.search_banner2);
        searchBanner.setRepeatCount(LottieDrawable.INFINITE);
        searchBanner.playAnimation();
        makeGUISearchless();

        Map<Customer, Long> match = allOrders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()));

        // DTO For Sorting by Amount Of Orders
        List<AmountOfOrdersPerCustomerDTO> unsortedDTO = new ArrayList<>();
        match.forEach( (key, val) -> unsortedDTO.add(new AmountOfOrdersPerCustomerDTO(key, val)));

        // Sorted by DESC order amount of orders
        List<AmountOfOrdersPerCustomerDTO> sortedDTOList = unsortedDTO.stream()
                .sorted(Comparator.comparing(AmountOfOrdersPerCustomerDTO::getAmount).reversed())
                .collect(Collectors.toList());

        loadResultCardsAmountOfOrdersPerCustomer(sortedDTOList);
    }


    private void loadTotalSpendPerCustomerGUI() {
        LottieAnimationView searchBanner = findViewById(R.id.searchBanner);
        searchBanner.setAnimation(R.raw.search_banner3);
        searchBanner.setRepeatCount(LottieDrawable.INFINITE);
        searchBanner.playAnimation();
        makeGUISearchless();

        Map<Customer, Long> match = allOrders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingLong(Order::getTotalOrderPrice)));

        // DTO For Sorting by Amount SEK
        List<TotalSpendPerCustomerDTO> unsortedDTO = new ArrayList<>();
        match.forEach((key, val) -> unsortedDTO.add(new TotalSpendPerCustomerDTO(key, val)));

        // Sorted desc order by amount
        List<TotalSpendPerCustomerDTO> sorted = unsortedDTO.stream()
                .sorted(Comparator.comparing(TotalSpendPerCustomerDTO::getAmount).reversed())
                .collect(Collectors.toList());

        loadResultCardsTotalOrderAmountPerCustomer(sorted);
    }

    private void loadTotalSpendPerCityGUI() {
        LottieAnimationView searchBanner = findViewById(R.id.searchBanner);
        searchBanner.setAnimation(R.raw.search_banner4);
        searchBanner.setRepeatCount(LottieDrawable.INFINITE);
        searchBanner.playAnimation();
        makeGUISearchless();

        Map<String, Long> match = allOrders.stream()
                .collect(Collectors.groupingBy(key -> key.getCustomer().getCity(), Collectors.summingLong(Order::getTotalOrderPrice)));

        // Using DTO to be able to sort the amount
        List<TotalSpendPerCityDTO> totalSpendPerCityDTO = new ArrayList<>();
        match.forEach( (key, value) -> totalSpendPerCityDTO.add(new TotalSpendPerCityDTO(key, value)));

        // Sorted desc order by amount
        List<TotalSpendPerCityDTO> sorted = totalSpendPerCityDTO.stream()
                .sorted(Comparator.comparing(TotalSpendPerCityDTO::getAmount).reversed())
                .collect(Collectors.toList());

        loadResultCardsTotalAmountPerCity(sorted);
    }


    private void loadTopSellingProductsGUI() {
        LottieAnimationView searchBanner = findViewById(R.id.searchBanner);
        searchBanner.setAnimation(R.raw.search_banner5);
        searchBanner.setRepeatCount(LottieDrawable.INFINITE);
        searchBanner.playAnimation();
        makeGUISearchless();

        // Hämtar alla Ordrars ProduktListor
        List<List<Product>> totalProductList = allOrders.stream()
                        .map(Order::getListOfProducts).collect(Collectors.toList());

        // Plattar ut och lägger in produkterna i 1 och samma lista
        List<Product> allProducts = totalProductList.stream()
                        .flatMap(products -> products.stream())
                                .collect(Collectors.toList());

        // Räknar produkternas förekomst i beställningar
        Map<String, Long> topSelling = allProducts.stream()
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()));

        // USING DTO to be able to SORT into TOP 5
        List<TopSellingProductDTO> topSellingProductDTOList = new ArrayList<>();
        topSelling.forEach( (key, value) -> topSellingProductDTOList.add(new TopSellingProductDTO(key, value)));

        // Sorting DESC order and Limit 3 of the total topSellingProductDTOList
        List<TopSellingProductDTO> fiveMostSoldDTOList = topSellingProductDTOList.stream()
                .sorted(Comparator.comparing(TopSellingProductDTO::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());


        loadResultCardsTopSellingProducts(fiveMostSoldDTOList);
    }

    // Sök Knappen Lyssnare

    public void handleSearchClick(View view) {
        EditText searchWordField = findViewById(R.id.searchWord);
        GridLayout theGrid = findViewById(R.id.gridSearchResult);
        theGrid.removeAllViews();

        String word = searchWordField.getText().toString().trim();
        searchWordField.setText("");

        searchBySpecifics(word);

        theGrid.invalidate();
    }
    private void searchBySpecifics(String word) {
        List<Order> ordersMatchingword;

        // Först måste vi hämta alla Orders som matchar "word"
        if (isOrderNumberLessThanNineDigits(word) && isDigit(word)) {
            ordersMatchingword =
                    allOrders.stream()
                            .filter(order -> order.getListOfProducts().stream().anyMatch(product -> product.getEuSize() == Integer.parseInt(word)))
                            .collect(Collectors.toList());
        } else {
            ordersMatchingword =
                    allOrders.stream()
                            .filter(order -> order.getListOfProducts().stream().anyMatch(product -> product.getBrand().getName().equalsIgnoreCase(word) || product.getColor().getName().equalsIgnoreCase(word)))
                            .collect(Collectors.toList());
        }

        // Sedan från detta Resultatet vi får, skall vi DISTINCT hämta alla kunder som har dessa sök kriterier
        List<Customer> distinctCustomerList = ordersMatchingword.stream()
                .map(Order::getCustomer)
                .distinct().collect(Collectors.toList());

        // DEBUGGING
        Log.d("webshopen", "Orders: " + ordersMatchingword);
        Log.d("webshopen", "Orders Size: " + ordersMatchingword.size());

        Log.d("webshopen", "Customers: " + distinctCustomerList);
        Log.d("webshopen", "Customers Size: " + distinctCustomerList.size());

        // Ladda Resultat I GUI
        loadResultCardsSpecificProduct(distinctCustomerList);

    }

    // Används i "searchBySpecifics()"

    public boolean isDigit(String digit) {
        try {
            Integer.parseInt(digit);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // Skriver man över Integer.MAX_VALUE får vi Numberformat Exception, så nu kan vi undvika det

    private boolean isOrderNumberLessThanNineDigits(String input) {
        return input.length() < 9;
    }


    // SPECIFIK PRODUKT RESULTAT
    private void loadResultCardsSpecificProduct(List<Customer> resultList) {
        if (resultList.isEmpty()) {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
        } else {
            resultList.forEach(customer -> addResultCards(customer.getCustomerDescription()));
            Toast.makeText(this, "Found " + resultList.size() + " results", Toast.LENGTH_SHORT).show();
        }
    }

    // ANTAL ORDER PER KUND
    private void loadResultCardsAmountOfOrdersPerCustomer(List<AmountOfOrdersPerCustomerDTO> resultList) {
        if (resultList.isEmpty()) {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
        } else {
            resultList.forEach((customerDTO) -> addResultCards(customerDTO.getCustomer().getCustomerDescription() + "\n" + customerDTO.getAmount() + " orders"));
            Toast.makeText(this, "Found " + resultList.size() + " results", Toast.LENGTH_SHORT).show();
        }
    }

    // ANTAL ORDERVÄRDE PER KUND
    private void loadResultCardsTotalOrderAmountPerCustomer(List<TotalSpendPerCustomerDTO> result) {
        if (result.isEmpty()) {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
        } else {
            result.forEach((customerDTO) -> addResultCards(customerDTO.getCustomer().getCustomerDescription() + "\n" + customerDTO.getAmount() + " SEK"));
            Toast.makeText(this, "Found " + result.size() + " results", Toast.LENGTH_SHORT).show();
        }
    }

    // ANTAL ORDERVÄRDE PER STAD
    private void loadResultCardsTotalAmountPerCity(List<TotalSpendPerCityDTO> result) {
        if (result.isEmpty()) {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
        } else {
            result.forEach((dto) -> addResultCards(dto.getName() + "\n" + dto.getAmount() + " SEK"));
            Toast.makeText(this, "Found " + result.size() + " results", Toast.LENGTH_SHORT).show();
        }
    }

    // TOP SELLING Products
    private void loadResultCardsTopSellingProducts(List<TopSellingProductDTO> result) {
        if (result.isEmpty()) {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
        } else {
            result.forEach(product -> addResultCards(product.getName() + "\n" + product.getAmount() + " X"));
            Toast.makeText(this, "Found " + result.size() + " results", Toast.LENGTH_SHORT).show();
        }
    }

    private void addResultCards(String result) {
        // Fetch the grid object
        GridLayout theGrid = findViewById(R.id.gridSearchResult);

        //TextView
        TextView textView = new TextView(this);
        textView.setTextSize(20F);
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsText.setMargins(30, 30, 30, 30);
        textView.setText(result); // här blir text
        textView.setLayoutParams(layoutParamsText);

        // CardView
        LinearLayout.LayoutParams layoutParamsCard = new LinearLayout.LayoutParams(800, 400);
        layoutParamsCard.gravity = Gravity.CENTER;
        CardView cardView = new CardView(this);
        cardView.setCardBackgroundColor(Color.parseColor("#ECF9FF"));
        cardView.setLayoutParams(layoutParamsCard);
        cardView.setRadius(30);
        layoutParamsCard.setMargins(100, 30, 100, 30);

        // Lägg in TextView I CardView
        cardView.addView(textView, layoutParamsText);

        // Lägg in allt i GridView
        theGrid.addView(cardView, layoutParamsCard);
    }
}