package com.au564065.plantswap.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.PlantSwapUser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundPlantSwapService extends LifecycleService {
    //Debug logging tag
    private static final String TAG = "BackgroundPlantService";

    //Auxiliary
    private ExecutorService execService;
    private Repository repo;

    //Variables
    private long sleepTime = 120000;

    //Constructor
    public BackgroundPlantSwapService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Creating Service");
        repo = Repository.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand: Starting Background Service");

        //repo.readUserWishList("s2AA3JjujIgupcsRB5bMGUBLLnq2");

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

                //repo.fetchPlantFromAPI("christmastree");

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
