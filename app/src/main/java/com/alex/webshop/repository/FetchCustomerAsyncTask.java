package com.alex.webshop.repository;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.alex.webshop.model.Customer;
import com.alex.webshop.service.ConnectionEnum;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FetchCustomerAsyncTask extends AsyncTask<Void, Void, List<Customer>> {

    private WeakReference<View> viewRef; // Om vi vill ändra spec komponent (View)
    private List<Customer> listOfCustomers;

    public FetchCustomerAsyncTask() {
    }

    public FetchCustomerAsyncTask(View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    protected List<Customer> doInBackground(Void... voids) {
        CustomerRepo repo = new CustomerRepo(ConnectionEnum.ALEX);
        return repo.getAllCustomers();
        // NO UI Stuff here, ONLY DATA Fetching From Repo (DB)!
    }

    @Override
    protected void onPostExecute(List<Customer> result) {
        // UI THREAD
        if (!result.isEmpty()) {
            Log.d("webshopen", "Fetched customers from DB");
            listOfCustomers = new ArrayList<>(result);
        }
    }
}
