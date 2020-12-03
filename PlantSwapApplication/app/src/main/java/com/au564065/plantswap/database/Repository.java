package com.au564065.plantswap.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.au564065.plantswap.BuildConfig;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.models.PlantSwapUser;
import com.au564065.plantswap.models.gsonModels.ApiJsonResponse;
import com.au564065.plantswap.models.gsonModels.GsonPlant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Singleton repository code adapted from lecture 7 Tracker application
public class Repository {
    //Debug logging tag
    private static final String TAG = "Repository";

    //List of plants in WishList
    private LiveData<List<Plant>> WishList;
    //List of plants for swap
    private LiveData<List<Swap>> SwapList;
    //Search History
    private LiveData<List<Plant>> SearchHistory;
    //Search Results
    private MutableLiveData<List<Plant>> SearchResults = new MutableLiveData<>();
    //Current user
    private MutableLiveData<PlantSwapUser> currentUser = new MutableLiveData<>();

    //Auxiliary
    private PlantDatabase db;
    private ExecutorService executor;
    private RequestQueue queue;
    private Context applicationContext;
    private String baseURL = "https://trefle.io/api/v1/plants";
    private FirebaseFirestore firebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static Repository INSTANCE = null;

    //Empty Repository constructor that will only get called once
    public Repository(Context context) {
        executor = Executors.newSingleThreadExecutor();
        applicationContext = context;

    }

    //Method returns an instance of Repository and lazy loads the instance on first call.
    //Currently unavailable because of hte dependency on Application Context for Volley
    public static Repository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(context);
        }
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

    public LiveData<PlantSwapUser> getCurrentUser() {
        return currentUser;
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
    public void addNewUserToCloudDatabase(PlantSwapUser plantSwapUserObject, String userID) {
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("name", plantSwapUserObject.getName());
        newUser.put("address", plantSwapUserObject.getAddress());
        newUser.put("zipCode", plantSwapUserObject.getZipCode());
        newUser.put("city", plantSwapUserObject.getCity());
        newUser.put("email", plantSwapUserObject.getEmail());
        newUser.put("phoneNumber", plantSwapUserObject.getPhoneNumber());

        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userID)
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: DocumentSnapshot set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error adding document", e);
                    }
                });
    }

    public void setCurrentUser(String userID) {
        Log.d(TAG, "setCurrentUser: Setting current user");
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "onComplete: Document loaded " + document.get("name").toString());
                                PlantSwapUser newCurrentUser = new PlantSwapUser(document.get("name").toString(),
                                        document.get("address").toString(),
                                        document.get("zipCode").toString(),
                                        document.get("city").toString(),
                                        document.get("email").toString(),
                                        document.get("phoneNumber").toString());
                                currentUser.postValue(newCurrentUser);
                            } else {
                                Log.d(TAG, "onComplete: No document found");
                            }
                        } else {
                            Log.d(TAG, "onComplete: failed with", task.getException());
                        }
                    }
                });
    }

    public void createNewSwap(Swap newSwap) {
        Map<String, Object> swapData = new HashMap<>();

        swapData.put("status", newSwap.getStatus());
        swapData.put("plantName", newSwap.getPlantName());
        swapData.put("plantPhotos", newSwap.getPlantPhotos());
        swapData.put("wishPlants", newSwap.getWishPlants());
        firebaseDatabase.collection(DatabaseConstants.SwapCollection).document()
                .set(swapData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: Swapp added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error writing document", e);
                    }
                });
    }

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
