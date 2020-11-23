package com.au564065.plantswap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.myswap.MySwapActivity;
import com.au564065.plantswap.activities.mywish.MyWishActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initializeButtons();
    }

    private void initializeButtons(){
        Button my_swaps, my_wishes, profile, browse_swaps, browse_plants;

        my_swaps = findViewById(R.id.btn_mm_my_swaps);
        my_swaps.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, MySwapActivity.class);
            startActivity(intent);
        });

        my_wishes = findViewById(R.id.btn_mm_my_wishes);
        my_wishes.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, MyWishActivity.class);
            startActivity(intent);

        });

        profile = findViewById(R.id.btn_mm_my_profile);
        profile.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, MySwapActivity.class);
            startActivity(intent);

        });

        browse_swaps = findViewById(R.id.btn_mm_browse_swaps);
        browse_swaps.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, MySwapActivity.class);
            startActivity(intent);

        });

        browse_plants = findViewById(R.id.btn_mm_browse_plants);
        browse_plants.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, MySwapActivity.class);
            startActivity(intent);

        });
    }
}