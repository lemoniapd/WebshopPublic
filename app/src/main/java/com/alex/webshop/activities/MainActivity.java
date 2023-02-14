package com.alex.webshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.webshop.R;
import com.alex.webshop.model.Customer;
import com.alex.webshop.service.DataHandler;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity {

    private List<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabListener();

    }

    private void tabListener() {
        TabLayout tabLayout = findViewById(R.id.tabMenu);
        Intent dashboard = new Intent(this, DashBoardActivity.class);

        // Lyssnare på Tablayout längst ner
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition(); // Vilken den står på just nu
                if (index == 1) startActivity(dashboard);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void fetchAllCustomers() {
        DataHandler dataHandler = new DataHandler();
        customers = dataHandler.getAllCustomersInDb();
    }

    @SuppressLint("SetTextI18n")
    public void loginHandler(View view) {
        // Hämtar email & lösen
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        // Login
        if (!(emailString.isEmpty() || passwordString.isEmpty())) {
            // Bara logga in om båda fältet inskrivna
            fetchAllCustomers();


            // Kolla med Streams filter om de finns match
            try {
                Customer match = loginMatch(emailString, passwordString);
                Log.d("webshopen", match.toString()); // Debugging Log

                // Intent --> Skicka Customer Object till ny ProfilePage (Den man är inloggad med) + Animation
                Intent customerProfile = new Intent(this, ProfilePageActivity.class);
                customerProfile.putExtra("customerProfile", match);
                Bundle animationBundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                startActivity(customerProfile, animationBundle);

            } catch (NoSuchElementException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Fill in both fields", Toast.LENGTH_SHORT).show();
        }
    }

    // Java Stream API för att hitta en login match
    private Customer loginMatch(String email, String password) throws NoSuchElementException {
        return customers.stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email) && customer.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Wrong Email / Password"));
    }
}