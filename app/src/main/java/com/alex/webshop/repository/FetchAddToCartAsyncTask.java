package com.alex.webshop.repository;

import android.os.AsyncTask;

import com.alex.webshop.model.Customer;
import com.alex.webshop.model.Product;
import com.alex.webshop.service.ConnectionEnum;
import com.alex.webshop.service.Response;

public class FetchAddToCartAsyncTask extends AsyncTask<Void, Void, Response> {
    private Customer customer;
    private int orderId;
    private Product product;
    private Response result;

    public FetchAddToCartAsyncTask(Customer customer, int orderId, Product product) {
        this.customer = customer;
        this.orderId = orderId;
        this.product = product;
    }

    @Override
    protected Response doInBackground(Void... voids) {
        CustomerRepo repo = new CustomerRepo(ConnectionEnum.ALEX);
        return repo.addToCart(customer, orderId, product);
        // NO UI Stuff here, ONLY DATA Fetching From Repo (DB)!
    }

    @Override
    protected void onPostExecute(Response response) {
        // UI THREAD
        result = response;
    }

    public Response getResult() {
        return result;
    }
}
