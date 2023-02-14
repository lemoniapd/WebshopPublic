package com.alex.webshop.repository;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.alex.webshop.model.Category;
import com.alex.webshop.service.ConnectionEnum;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FetchCategoryAsyncTask extends AsyncTask<Void, Void, List<Category>> {
    private WeakReference<View> viewRef; // Om vi vill ändra spec komponent (View)
    private List<Category> listOfCategories;

    public FetchCategoryAsyncTask() {
    }

    public FetchCategoryAsyncTask(View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    protected List<Category> doInBackground(Void... voids) {
        CategoryRepo repo = new CategoryRepo(ConnectionEnum.ALEX);
        return repo.getAllCategories();
        // NO UI Stuff here, ONLY DATA Fetching From Repo (DB)!
    }

    @Override
    protected void onPostExecute(List<Category> result) {
        // UI THREAD
        if (!result.isEmpty()) {
            Log.d("webshopen", "Fetched categories from DB");
            listOfCategories = new ArrayList<>(result);
        }
    }
}
