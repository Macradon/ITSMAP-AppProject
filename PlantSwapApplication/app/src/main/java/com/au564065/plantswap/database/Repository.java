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
import com.au564065.plantswap.models.SwapOffer;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.models.gsonLocationModels.LocationResult;
import com.au564065.plantswap.models.gsonLocationModels.Result;
import com.au564065.plantswap.models.gsonPlantModels.ApiJsonResponse;
import com.au564065.plantswap.models.gsonPlantModels.GsonPlant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    //WishList
    private MutableLiveData<List<Wish>> WishList = new MutableLiveData<>();
    private MutableLiveData<List<Wish>> SwapWishList = new MutableLiveData<>();
    //Swaps
    private MutableLiveData<List<Swap>> SwapList = new MutableLiveData<>();
    private MutableLiveData<Swap> SpecificSwap = new MutableLiveData<>();
    //Search History
    //private MutableLiveData<List<Plant>> SearchHistory = new MutableLiveData<>();
    //Search Results
    private MutableLiveData<List<Plant>> SearchResults = new MutableLiveData<>();
    private MutableLiveData<Plant> SpecificPlant = new MutableLiveData<>();
    //Current user
    private MutableLiveData<PlantSwapUser> currentUser = new MutableLiveData<>();

    //Auxiliary
    private PlantDatabase db;
    private ExecutorService executor;
    private RequestQueue queue;
    private Context applicationContext;
    private String trefleURL = "https://trefle.io/api/v1/plants";
    private String googleURL = "https://maps.googleapis.com/maps/api/geocode/json?&address=";
    private FirebaseFirestore firebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static Repository INSTANCE = null;

    //Repository constructor that will only get called once
    public Repository(Context context) {
        executor = Executors.newSingleThreadExecutor();
        applicationContext = context;
    }

    //Method returns an instance of Repository and lazy loads the instance on first call.
    public static Repository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(context);
        }
        return INSTANCE;
    }

    public LiveData<List<Wish>> getCurrentUserWishList() {
        return WishList;
    }

    public LiveData<List<Wish>> getSwapOwnerWishList() {
        return SwapWishList;
    }

    public LiveData<List<Swap>> getAllSwaps() {
        return SwapList;
    }

    public LiveData<Swap> getSwap() {
        return SpecificSwap;
    }

    /**public LiveData<List<Plant>> getSearchHistory() {
        return SearchHistory;
    }**/

    public LiveData<List<Plant>> getSearchResult() {
        return SearchResults;
    }

    public LiveData<Plant> getSpecificPlant() {
        return SpecificPlant;
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
    /**
     *  User Methods
     */
    //Method to create a new user to the database
    public void addNewUserToCloudDatabase(PlantSwapUser plantSwapUserObject, String userID) {
        Log.d(TAG, "addNewUserToCloudDatabase: Adding new user to the database");
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("name", plantSwapUserObject.getName());
        newUser.put("address", plantSwapUserObject.getAddress());
        newUser.put("zipCode", plantSwapUserObject.getZipCode());
        newUser.put("city", plantSwapUserObject.getCity());
        newUser.put("email", plantSwapUserObject.getEmail());
        newUser.put("phoneNumber", plantSwapUserObject.getPhoneNumber());

        executeUserVolleyQueue(plantSwapUserObject, newUser, userID);
    }

    //Method to read the current user from the database
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

    //Method to update user in the database
    public void updateUserInCloudDatabase(PlantSwapUser plantSwapUserObject, String userID) {
        Map<String, Object> updateUser = new HashMap<>();
        updateUser.put("name", plantSwapUserObject.getName());
        updateUser.put("address", plantSwapUserObject.getAddress());
        updateUser.put("zipCode", plantSwapUserObject.getZipCode());
        updateUser.put("city", plantSwapUserObject.getCity());
        updateUser.put("email", plantSwapUserObject.getEmail());
        updateUser.put("phoneNumber", plantSwapUserObject.getPhoneNumber());

        executeUserVolleyQueue(plantSwapUserObject, updateUser, userID);
    }

    //Method to delete user from the database
    public void deleteUserInCloudDatabase(String userId) {
        //TODO Implement this
    }

    //Helper method to execute volley user requests
    private void executeUserVolleyQueue(PlantSwapUser plantSwapUserObject, Map<String, Object> mapUserObject, String userID) {
        Log.d(TAG, "executeUserVolleyQueue: Executing Volley Queue request");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateUserInCloudDatabase run: Sending GET request");
                if (queue == null) {
                    Log.d(TAG, "updateUserInCloudDatabase: Instantiate new Volley Request Queue");
                    queue = Volley.newRequestQueue(applicationContext);
                }

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        googleURL + plantSwapUserObject.getAddress().replaceAll(" ", "%20") + "%20"
                                + plantSwapUserObject.getZipCode() + "%20" + plantSwapUserObject.getCity()
                                + "&key=" + BuildConfig.GOOGLE_KEY,
                        (response) -> {
                            handleFetchLocationFromAPI(response, mapUserObject, userID);
                        }, (error) -> {
                    Log.d(TAG, "run: This did not work", error);
                });

                queue.add(stringRequest);
            }
        });
    }

    //Helper method to retrieve address coordinates
    private void handleFetchLocationFromAPI(String response, Map<String, Object> mapUserObject, String userID) {
        Gson gson = new GsonBuilder().create();
        LocationResult locationResponse = gson.fromJson(response, LocationResult.class);
        List<Result> results = locationResponse.getResults();

        if (results!=null) {
            Log.d(TAG, "handleFetchLocationFromAPI: Response is not null");
            String latitude = results.get(0).getGeometry().getLocation().getLat().toString();
            String longitude = results.get(0).getGeometry().getLocation().getLng().toString();
            mapUserObject.put("addressGpsCoordinates", latitude + ";" + longitude);

            postUserFirebaseRequest(mapUserObject, userID);
        }
    }

    //Helper method to post user data to the firebase database
    private void postUserFirebaseRequest(Map<String, Object> newUser, String userID) {
        Log.d(TAG, "postUserFirebaseRequest: Posting to Firebase");
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userID)
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: DocumentSnapshot set");
                        setCurrentUser(userID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error adding document", e);
                    }
                });
    }

    /**
     *  Wish Methods
     */
    //Method to create wish in a user's wish list
    public void addWishToUserWishList(Wish newWish) {
        Log.d(TAG, "addWishToUserWishList: Adding new wish to user's wish list");
        Map<String, Object> wishData = new HashMap<>();
        wishData.put("wishPlant",  newWish.getWishPlant());
        wishData.put("radius", newWish.getRadius());

        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(firebaseAuth.getUid())
                .collection(DatabaseConstants.WishCollection).document()
                .set(wishData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Wish added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Error  writing document", e);
                    }
                });
    }

    //Method to read a user's wishes
    public void readUserWishList(String userId, Boolean swap) {
        Log.d(TAG, "readUserWishList: Reading wishlist from user: " + userId);
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userId)
                .collection(DatabaseConstants.WishCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Wish> wishListHolder = new ArrayList();
                             for (QueryDocumentSnapshot document : task.getResult()) {
                                 Map<String, Object> docRef = document.getData();

                                 //Remove object artifacts and make comma separated string array
                                 String[] wishPlant = docRef.get("wishPlant").toString()
                                         .replaceAll("\\{", "")
                                         .replaceAll("\\}", "")
                                         .replaceAll(" ", "")
                                         .split(",");

                                 //For each element, split it and use it to map to a new map
                                 Map<String, String> wishPlantMap = new HashMap<>();
                                 for (int i=0; i<wishPlant.length;i++) {
                                     String[] wishPlantSplit = wishPlant[i].split("=");
                                     wishPlantMap.put(wishPlantSplit[0],wishPlantSplit[1]);
                                 }

                                 //Take the new map and use all of its data to create the plant object
                                 Plant newWishPlant = new Plant(
                                         wishPlantMap.get("scientificName"),
                                         wishPlantMap.get("commonName"),
                                         wishPlantMap.get("imageURL"),
                                         wishPlantMap.get("genus"),
                                         wishPlantMap.get("family")
                                 );
                                 Log.d(TAG, "onComplete: Plant from Wish List: " + newWishPlant.getScientificName());

                                 //Make new wish with the plant object and document radius
                                 Wish wishListWish = new Wish(newWishPlant,Integer.parseInt(docRef.get("radius").toString()));
                                 //Add it to the temporary holder list
                                 wishListHolder.add(wishListWish);
                             }
                             if (swap) {
                                 SwapWishList.postValue(wishListHolder);
                             } else {
                                 WishList.postValue(wishListHolder);
                             }
                        }
                    }
                });
    }

    //Method to delete wish from a user's wish list
    public void deleteWishFromUserWishList() {
        //TODO Implement this
    }

    //Method to delete all wishes from a user's wish list
    //TODO IMPLEMENT THIS

    /**
     *  Swap Methods
     */
    //Method to create a swap in the database
    public void createNewSwap(Swap newSwap) {
        Map<String, Object> swapData = new HashMap<>();
        swapData.put("ownerID", firebaseAuth.getUid());
        swapData.put("ownerAddressGpsCoordinates", newSwap.getOwnerAddressGpsCoordinates());
        swapData.put("status", newSwap.getStatus());
        swapData.put("plantName", newSwap.getPlantName());

        getSwapCoordinates(swapData)

        firebaseDatabase.collection(DatabaseConstants.SwapCollection).document()
                .set(swapData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Swap added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error writing document", e);
                    }
                });
    }

    //Helper method to get location coordinates form address
    private void

    //Method to read all swaps
    public void readAllSwaps() {
        firebaseDatabase.collection(DatabaseConstants.SwapCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Swap> swapHolder = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                swapHolder.add(parseSwapDocument(document));
                            }
                            SwapList.postValue(swapHolder);
                        } else {
                            Log.d(TAG, "onComplete: Error getting documents", task.getException());
                        }
                    }
                });
    }

    //Method to read specific swap
    public void readSpecificSwap(String swapID) {
        firebaseDatabase.collection(DatabaseConstants.SwapCollection).document(swapID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            SpecificSwap.postValue(parseSwapDocument(document));
                        } else {
                            Log.d(TAG, "onComplete: Error getting documents", task.getException());
                        }
                    }
                });
    }

    //Method to read all user's swaps
    public void readAllSwapsFromUser(String userId) {
        firebaseDatabase.collection(DatabaseConstants.SwapCollection)
                .whereEqualTo("ownerID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Swap> swapHolder = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                swapHolder.add(parseSwapDocument(document));
                            }
                            SwapList.postValue(swapHolder);

                        } else {
                            Log.d(TAG, "onComplete: Error getting documents", task.getException());
                        }
                    }
                });
    }

    //Method to delete swap from the database
    //TODO Implement this

    //Helper method to parse swap document
    private Swap parseSwapDocument(DocumentSnapshot document) {
        Swap getSwap = new Swap(
                document.get("ownerID").toString(),
                document.get("ownerAddressGpsCoordinates").toString(),
                document.get("plantName").toString()
        );
        getSwap.setStatus(document.get("status").toString());
        getSwap.setSwapID(document.getId());

        return getSwap;
    }

    //Method to create an offer to a swap
    public void addOfferToSwap(SwapOffer newSwapOfferObject, String swapID) {
        Map<String, Object> newOffer = new HashMap<>();
        newOffer.put("swapID", swapID);
        newOffer.put("swapOfferUserID", newSwapOfferObject.getSwapOfferUserID());
        newOffer.put("plantsOffered", newSwapOfferObject.getPlantsOffered());

        firebaseDatabase.collection(DatabaseConstants.OfferCollection).document()
                .set(newOffer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Swap Offer added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error writing document", e);
                    }
                });
    }

    //Method to read offers to a swap
    //TODO IMPLEMENT THIS

    //Method to delete swap offer from a swap
    //TODO Implement this

    /**
     *  Message Methods
     */
    //TODO Implement this
    //TODO Implement this
    //TODO Implement this
    //TODO Implement this

    /**
     * TEIFLE API METHODS
     * THIS SECTION HANDLES THE DIFFERENT API-RELATED
     * CALLS THAT IS NEEDED BY THE APPLICATION
     */
    //Method to fetch all plants that associate with tree name.
    //Tree name can be of common name, scientific name, and for specific plants use slug.
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
                        trefleURL + "/search?token=" + BuildConfig.CONSUMER_KEY + "&q=" + treeName,
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

    //Helper method to handle API response
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

    //Central method to convert scientific name to slug
    public String convertScientificNameToSlug(String scientificName) {
        return scientificName.toLowerCase().replaceAll(" ", "-");
    }
}
