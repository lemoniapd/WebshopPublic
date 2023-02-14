package com.alex.webshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alex.webshop.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

public class DashBoardActivity extends AppCompatActivity {

    private final String[] menuChoices = {"All customers that have bought specific product", "Amount of orders per Customer", "Total spend per Customer", "Total spend per City", "Top 3 selling Products"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        loadMenuCards();
        tabListener();
    }

    private void loadMenuCards() {
        Arrays.stream(menuChoices)
                .forEach(menuDesc -> addMenuCards(menuDesc));
    }

    private void addMenuCards(String menuChoiceDescription) {
        // Fetch the grid object
        GridLayout theGrid = findViewById(R.id.gridDashboard);
        //TextView
        TextView textView = new TextView(this);
        textView.setTextSize(20F);
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsText.setMargins(30, 30, 30, 30);
        textView.setText(menuChoiceDescription); // här blir text
        textView.setLayoutParams(layoutParamsText);

        // CardView
        LinearLayout.LayoutParams layoutParamsCard = new LinearLayout.LayoutParams(800, 200);
        layoutParamsCard.gravity = Gravity.CENTER;
        CardView cardView = new CardView(this);
        cardView.setCardBackgroundColor(Color.parseColor("#ECF9FF"));
        cardView.setLayoutParams(layoutParamsCard);
        cardView.setRadius(30);
        layoutParamsCard.setMargins(100, 30, 100, 30);

        // onClick listener på varje CardView
        cardView.setOnClickListener(view -> openSelectedMenuChoice(menuChoiceDescription));

        // Lägg in TextView I CardView
        cardView.addView(textView, layoutParamsText);

        // Lägg in allt i GridView
        theGrid.addView(cardView, layoutParamsCard);
    }

    // Opens new Page for selected menu choice
    private void openSelectedMenuChoice(String menuChoiceDesc) {
        Intent i = new Intent(this, StatisticsActivity.class);
        i.putExtra("menuChoice", menuChoiceDesc); // anv för en switch sats i StatisticsActivity för att ladda korrekt GUI
        Bundle animationBundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(i, animationBundle);

    }


    private void tabListener() {
        TabLayout tabLayout = findViewById(R.id.tabMenu);
        tabLayout.selectTab(tabLayout.getTabAt(1)); // Sätter att Dashboard Tab är selected
        Intent home = new Intent(this, MainActivity.class);

        // Lyssnare på Tablayout längst ner
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition(); // Vilken den står på just nu
                if (index == 0) startActivity(home);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}