package com.au564065.plantswap.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import com.au564065.plantswap.database.Repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundPlantSwapService extends LifecycleService {
    //Debug logging tag
    private static final String TAG = "BackgroundPlantService";

    //Auxiliary
    private ExecutorService execService;
    private Repository repos;

    //Variables
    private long sleepTime = 60000;

    //Constructor
    public BackgroundPlantSwapService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Creating Service");
        repos = new Repository(true, this.getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand: Starting Background Service");

        recursiveUpdate();

        return START_STICKY;
    }

    private void recursiveUpdate() {
        Log.d(TAG, "recursiveUpdate: Starting Recursive updater");
        if (execService == null) {
            execService = Executors.newSingleThreadExecutor();
        }

        execService.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: Updating");

                repos.fetchChristmastrees();

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                recursiveUpdate();
            }
        });
    }
}
