package com.au564065.plantswap.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.au564065.plantswap.GlobalConstants;
import com.au564065.plantswap.R;
import com.au564065.plantswap.activities.browseswap.BrowseSwapsActivity;
import com.au564065.plantswap.activities.login.LoginActivity;
import com.au564065.plantswap.activities.myswap.MySwapActivity;
import com.au564065.plantswap.activities.mywish.MyWishActivity;
import com.au564065.plantswap.activities.browseplant.BrowsePlantActivity;
import com.au564065.plantswap.activities.profile.ProfileActivity;
import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.services.BackgroundPlantSwapService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initializeButtons();
        startBackgroundActivity();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            navigateToLogin();
        } else {
            Repository.getInstance(getApplicationContext()).setCurrentUser(user.getUid());
        }
    }

    private void startBackgroundActivity() {
        Intent backgroundIntent = new Intent(this, BackgroundPlantSwapService.class);
        startService(backgroundIntent);
    }

    private void initializeButtons(){
        Button my_swaps, my_wishes, profile, browse_swaps, browse_plants, exit;

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
            Intent intent = new Intent(MainMenuActivity.this, ProfileActivity.class);
            startActivity(intent);

        });

        browse_swaps = findViewById(R.id.btn_mm_browse_swaps);
        browse_swaps.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, BrowseSwapsActivity.class);
            startActivity(intent);

        });

        browse_plants = findViewById(R.id.btn_mm_browse_plants);
        browse_plants.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, BrowsePlantActivity.class);
            startActivity(intent);

        });

        exit = findViewById(R.id.btn_mm_exit);
        exit.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            finish();
        });
    }

    private void navigateToLogin() {
        Intent notLoggedIn = new Intent(this, LoginActivity.class);
        startActivityForResult(notLoggedIn, GlobalConstants.LoginRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            navigateToLogin();
        }

        switch(requestCode) {
            case GlobalConstants.LoginRequestCode:
                if (resultCode == RESULT_OK) {

                }
        }
    }
}