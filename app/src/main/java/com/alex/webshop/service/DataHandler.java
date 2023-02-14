package com.alex.webshop.service;

import android.util.Log;

import com.alex.webshop.model.Category;
import com.alex.webshop.model.Customer;
import com.alex.webshop.model.Order;
import com.alex.webshop.model.Product;
import com.alex.webshop.repository.FetchCategoryAsyncTask;
import com.alex.webshop.repository.FetchCustomerAsyncTask;
import com.alex.webshop.repository.FetchOrderAsyncTask;
import com.alex.webshop.repository.FetchProductAsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataHandler {

    // Alla produkter i DB, färdigt mappade med alla kategorier
    private List<Product> allProductsInDb;
    private List<Category> allCategoriesInDb;
    private List<Customer> allCustomersInDb;
    private List<Order> allOrdersInDb;
    // Higher Order Function Med Interface
    private final ProductSearch productListSearch = (orderId, order) -> order.getId() == orderId;

    public DataHandler() {
        allCategoriesInDb = new ArrayList<>();
        allProductsInDb = new ArrayList<>();

        // hämtar alla Customers
        handleGetAllCustomersFromDb();

        // Hämtar products från db
        fetchAllProductsList();

        // Laddar in alla Categories
        fetchAllCategoriesList();

        // Sätter in rätt kategorilista i produkten
        handleCategoryMapping();

        // Hämtar alla orders in Db
        handleGetAllOrders();

        // Mappar upp alla Produkter rätt i alla ordrar
        handleProductOrderMapping();
    }

    // Android Async Task Körs i bakgrunden, ger resultat när den får till UI tråden, hämtar DB DATA
    private void fetchAllProductsList() {
        try {
            allProductsInDb = new FetchProductAsyncTask().execute().get();

        } catch (ExecutionException | InterruptedException e) {
            Log.d("webshopen", "Could not fetch Products From Db");
        }
    }

    // Hämtar alla Ordrar från DB
    private void handleGetAllOrders() {
        try {
            allOrdersInDb = new FetchOrderAsyncTask(allCustomersInDb, allProductsInDb).execute().get();

        } catch (ExecutionException | InterruptedException e) {
            Log.d("webshopen", "Could not fetch Orders From Db");
        }
    }

    private List<Category> fetchAllCategoriesList() {
        try {
            allCategoriesInDb = new FetchCategoryAsyncTask().execute().get();
            Log.d("webshopen", allCategoriesInDb.toString());
            Log.d("webshopen", String.valueOf(allCategoriesInDb.size()));

        } catch (ExecutionException | InterruptedException e) {
            Log.d("webshopen", "Could not fetch Categories From Db");
        }
        return allCategoriesInDb;
    }

    // Android Async Task Körs i bakgrunden, ger resultat när den får till UI tråden, hämtar DB DATA
    private void handleGetAllCustomersFromDb() {
        try {
            allCustomersInDb = new FetchCustomerAsyncTask().execute().get();
            Log.d("webshopen", allCustomersInDb.toString());

        } catch (ExecutionException | InterruptedException e) {
            Log.d("webshopen", "Could not fetch customers From Db");
        }
    }

    // Mappar upp korrekta listor med produkter på order + HIGHER ORDER FUNCTION
    private void handleProductOrderMapping() {
        // Higher Order Function som har en parameter (integer, som är orderId) returnerar En lista med Alla Produkter som detta Order Id Tillhör!
        // Function<InParam, Det Den Returnerar>, måste alltid return något annars använd Consumer som är VOID
        Function<Integer, List<Product>> fetchAllProductsContainingThisOrderId = orderId -> allOrdersInDb.stream()
                .filter(order -> order.getId() == orderId)
                .map(order -> order.getListOfProducts().get(0)) // Finns i början bara 1 Produkt / Order så detta funkar!
                .collect(Collectors.toList());

        for (Order order : allOrdersInDb) {
            // Här Appliceras Higher Order Function med .apply()
            //order.setListOfProducts(fetchAllProductsContainingThisOrderId.apply(order.getId()));
            order.setListOfProducts(searchProductsForOrderId(order.getId(), productListSearch));
        }

        // Efter jag mappat upp alla produkter på alla ordrar, vill jag få bort dubletter!
        // Vi behöver generera en HashCode och Equals i Orderklassen så vi kan använda distinct()!
        // Sedan sätter vi om listan till den som är sorterad och har distinct
        allOrdersInDb = allOrdersInDb.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    // HIGHER ORDER FUNCTION EXEMPEL 2
    private List<Product> searchProductsForOrderId(int orderId, ProductSearch productSearch) {
        return allOrdersInDb.stream()
                .filter(order -> productSearch.exists(orderId, order))
                .map(order -> order.getListOfProducts().get(0))
                .collect(Collectors.toList());
    }

    private void handleCategoryMapping() {
        for (Product product : allProductsInDb) {
            List<String> categoriesString = fetchAllCategoriesByProductName(product.getName());
            product.setCategory(createCategoryListForProduct(categoriesString));
        }
        // Logg för Alla produkter
        Log.d("webshopen", allProductsInDb.toString());
        Log.d("webshopen", String.valueOf(allProductsInDb.size()));
    }

    // Skapar en lista med aktuella kategorier för specifik produkt används i handleCategoryMapping()
    private List<Category> createCategoryListForProduct(List<String> categoryNames) {
        List<Category> allCat = new ArrayList<>();

        categoryNames.forEach(name -> {
            try {
                Category match = findCategoryByName(name);
                allCat.add(match);
            } catch (NoSuchElementException e) {
                Log.d("webshopen", e.getMessage());
            }
        });
        return allCat;
    }

    // Returnerar ett Category-object med id och namn
    private Category findCategoryByName(String name) {
        return allCategoriesInDb.stream()
                .filter(category -> category.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No such element"));
    }

    // Används i metoden handleCategoryMapping()
    private List<String> fetchAllCategoriesByProductName(String productName) {
        return allProductsInDb.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .map(product -> product.getCategory().get(0).getName()) // Alla object har bara 1 kategori hittils
                .distinct()
                .collect(Collectors.toList());
    }


    public List<Product> getAllProductsInDb() {
        return allProductsInDb;
    }

    public List<Order> getAllOrdersInDb() {
        return allOrdersInDb;
    }

    public List<Customer> getAllCustomersInDb() {
        return allCustomersInDb;
    }
}
