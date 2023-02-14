package com.alex.webshop.repository;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.alex.webshop.model.Product;
import com.alex.webshop.service.ConnectionEnum;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FetchProductAsyncTask extends AsyncTask<Void, Void, List<Product>> {

    private WeakReference<View> viewRef; // Om vi vill ändra spec komponent (View)

    private List<Product> listOfProducts;

    public FetchProductAsyncTask() {
    }

    public FetchProductAsyncTask(View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    protected List<Product> doInBackground(Void... voids) {
        ProductRepo repo = new ProductRepo(ConnectionEnum.ALEX);
        return repo.getAllProducts();
        // NO UI Stuff here, ONLY DATA Fetching From Repo (DB)!
    }

    @Override
    protected void onPostExecute(List<Product> result) {
        // UI THREAD
        if (!result.isEmpty()) {
            Log.d("webshopen", "Fetched products from DB");
            listOfProducts = new ArrayList<>(result);
        }
    }

}
