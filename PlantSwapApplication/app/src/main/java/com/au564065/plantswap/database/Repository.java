package com.au564065.plantswap.database;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.au564065.plantswap.BuildConfig;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.models.gsonModels.ApiJsonResponse;
import com.au564065.plantswap.models.gsonModels.GsonPlant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//Singleton repository code adapted from lecture 7 Tracker application
public class Repository {
    //Debug logging tag
    private static final String TAG = "Repository";

    //Singleton Repository instance
    private static Repository INSTANCE = null;

    //Cached list of plants in WishList
    private LiveData<List<Plant>> WishList;
    //Cached list of plants for swap
    private LiveData<List<Swap>> SwapList;
    //Cached Search History
    private LiveData<List<Plant>> SearchHistory;
    //Cached Search Results
    private MutableLiveData<List<Plant>> SearchResults  = new MutableLiveData<>();

    //Auxiliary
    private PlantDatabase db;
    private ExecutorService executor;
    private RequestQueue queue;
    private Context applicationContext;
    private String baseURL = "https://trefle.io/api/v1/plants";

    //Empty Repository constructor that will only get called once
    public Repository(Context context){
        executor = Executors.newSingleThreadExecutor();
        applicationContext = context;
    }

    //Method returns an instance of Repository and lazy loads the instance on first call.
    //Currently unavailable because of hte dependency on Application Context for Volley
    public static Repository getInstance() {
        return INSTANCE;
    }

    public LiveData<List<Plant>> getWishList() {
        return WishList;
    }

    public LiveData<List<Swap>> getSwapList() {
        return SwapList;
    }

    public LiveData<List<Plant>> getSearchHistory() {
        return SearchHistory;
    }



    /**
     * DATABASE METHODS
     * THIS SECTION HANDLES THE DIFFERENT DATABASE-RELATED
     * CALLS THAT IS NEEDED BY THE APPLICATION
     */

    /**
     * FIREBASE METHODS
     * THIS SECTION HANDLES THE DIFFERENT FIREBASE-RELATED
     * CALLS THAT IS NEEDED BY THE APPLICATION
     */

    /**
     * API METHODS
     * THIS SECTION HANDLES THE DIFFERENT API-RELATED
     * CALLS THAT IS NEEDED BY THE APPLICATION
     */
    //METHOD TO TEST THE API
    public void fetchChristmastrees() {
        Log.d(TAG, "fetchChristmastrees: Fetching Christmastrees");
        if (queue == null) {
            Log.d(TAG, "fetchChristmastrees: Instantiate new Volley Request Queue");
            queue = Volley.newRequestQueue(applicationContext);
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "fetchChristmastrees: Sending new String Request");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        baseURL + "/search?token=" + BuildConfig.CONSUMER_KEY + "&q=christmastree",
                        (response) -> {
                            Log.d(TAG, "fetchChristmastrees: Lambda Response");
                            parseChristmasJsonResponse(response);
                        }, (error) -> {
                    Log.d(TAG, "run: This did not work", error);
                });

                queue.add(stringRequest);
            }
        });
    }

    private void parseChristmasJsonResponse(String response) {
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

    public void fetchPlantFromAPI(String treeName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "fetchPlantFromAPI: Sending GET request");
                if (queue == null) {
                    Log.d(TAG, "fetchPlantFromAPI: Instantiate new Volley Request Queue");
                    queue = Volley.newRequestQueue(applicationContext);
                }

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        baseURL + "/search?token=" + BuildConfig.CONSUMER_KEY + "&q=" + treeName,
                        (response) -> {
                            Log.d(TAG, "fetchPlantFromAPI: handling response");
                            handleFetchPlantFromAPI(response);
                        }, (error) -> {
                    Log.d(TAG, "run: This did not work", error);
                });
                
                queue.add(stringRequest);
            }
        });
    }

    private void handleFetchPlantFromAPI(String response) {
        Gson gson = new GsonBuilder().create();
        ApiJsonResponse jsonResponse = gson.fromJson(response, ApiJsonResponse.class);
        List<GsonPlant> jsonPlants = jsonResponse.getData();
        List<Plant> plantHolder = new ArrayList();

        if (jsonPlants!=null) {
            Log.d(TAG, "handleFetchPlantFromAPI: Response is not null");
            for (int i = 0; i < jsonPlants.size(); i++) {
                Log.d(TAG, "handleFetchPlantFromAPI: Adding " + jsonPlants.get(i).getScientificName() + " to the search results");
                Plant searchResultPlant = new Plant(jsonPlants.get(i));
                plantHolder.add(searchResultPlant);
            }
            SearchResults.postValue(plantHolder);
        }

    }
}
