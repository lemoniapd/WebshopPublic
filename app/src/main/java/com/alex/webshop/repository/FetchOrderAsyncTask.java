package com.alex.webshop.repository;

import android.os.AsyncTask;
import android.util.Log;

import com.alex.webshop.model.Customer;
import com.alex.webshop.model.Order;
import com.alex.webshop.model.Product;
import com.alex.webshop.service.ConnectionEnum;

import java.util.ArrayList;
import java.util.List;

public class FetchOrderAsyncTask extends AsyncTask<Void, Void, List<Order>> {

    private List<Order> allOrdersList;

    private List<Customer> allCustomersList;

    private List<Product> allProductsList;

    public FetchOrderAsyncTask(List<Customer> allCustomersList, List<Product> allProductsList) {
        this.allCustomersList = allCustomersList;
        this.allProductsList = allProductsList;
    }

    @Override
    protected List<Order> doInBackground(Void... voids) {
        OrderRepo repo = new OrderRepo(ConnectionEnum.ALEX, allCustomersList, allProductsList);
        return repo.getAllOrders();
        // NO UI Stuff here, ONLY DATA Fetching From Repo (DB)!
    }

    @Override
    protected void onPostExecute(List<Order> result) {
        // UI THREAD
        if(!result.isEmpty()) {
            Log.d("webshopen", "Fetched orders from DB");
            allOrdersList = new ArrayList<>(result);
        }
    }

}
