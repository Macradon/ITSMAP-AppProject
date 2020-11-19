package com.au564065.plantswap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.au564065.plantswap.R;
import com.au564065.plantswap.services.BackgroundPlantSwapService;

public class MainActivity extends AppCompatActivity {
    //Debug logging tag
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartBackgroundService();
    }

    //Methods to start and stop the BackgroundService class
    private void StartBackgroundService() {
        Log.d(TAG, "StartBackgroundService: Starting service from Main");
        Intent backgroundIntent = new Intent( this, BackgroundPlantSwapService.class);
        startService(backgroundIntent);
    }

    private void StopBackgroundService() {
        Intent backgroundIntent = new Intent( this, BackgroundPlantSwapService.class);
        stopService(backgroundIntent);
    }
}