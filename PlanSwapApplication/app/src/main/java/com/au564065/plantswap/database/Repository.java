package com.au564065.plantswap.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.au564065.plantswap.BuildConfig;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.models.gsonModels.ApiJsonResponse;
import com.au564065.plantswap.models.gsonModels.GsonPlant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Singleton repository code adapted from lecture 7 Tracker application
public class Repository {
    //Debug logging tag
    private static final String TAG = "Repository";

    //Singleton Repository instance
    private static Repository INSTANCE = null;

    //Cached list of plants in WishList
    private static LiveData<List<Plant>> WishList;
    //Cached list of plants for swap
    private static LiveData<List<Swap>> SwapList;
    //Cached search history
    private static LiveData<List<Plant>> SearchHistory;

    //Auxiliary
    private PlantDatabase db;
    private ExecutorService executor;
    private RequestQueue queue;
    private Context applicationContext;
    private String baseURL = "https://trefle.io/api/v1/plants";
    private boolean mainRepos;

    //Empty Repository constructor that will only get called once
    public Repository(boolean main, Context context){
        mainRepos = main;
        executor = Executors.newSingleThreadExecutor();
        applicationContext = context;
    }

    //Method returns an instance of Repository and lazy loads the instance on first call.
    public static Repository getInstance() {
        return INSTANCE;
    }

    //METHOD TO TEST THE APP AND THE API
    public void fetchChristmastrees() {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "fetchChristmastrees: Fetching Christmastrees");
                if (queue == null) {
                    Log.d(TAG, "fetchChristmastrees: Instantiate new Volley Request Queue");
                    queue = Volley.newRequestQueue(applicationContext);
                }

                Log.d(TAG, "fetchChristmastrees: Sending new String Request");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        baseURL + "/search?token=" + BuildConfig.CONSUMER_KEY + "&q=christmastree",
                        (response) -> {
                            Log.d(TAG, "fetchChristmastrees: Lambda Response");
                            parseJsonResponse(response);
                        }, (error) -> {
                    Log.d(TAG, "run: This did not work", error);
                });

                queue.add(stringRequest);
            }
        });
    }

    private void parseJsonResponse(String response) {
        Log.d(TAG, "parseJsonResponse: Parsing JSON Response");
        Gson gson = new GsonBuilder().create();
        ApiJsonResponse jsonResponse = gson.fromJson(response, ApiJsonResponse.class);
        List<GsonPlant> responsePlants = jsonResponse.getData();

        if (responsePlants != null) {
            Log.d(TAG, "parseJsonResponse: Resposne is not null. Size: " + responsePlants.size());
            for (int i = 0; i < responsePlants.size(); i++) {
                int plantIndex = i;
                Log.d(TAG, "parseJsonResponse: " + responsePlants.get(i).getScientificName());
            }
        }
    }
}
