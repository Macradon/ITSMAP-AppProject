package com.au564065.plantswap.database;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
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
    private MutableLiveData<Wish> SpecificWish = new MutableLiveData<>();
    private MutableLiveData<List<Wish>> SwapWishList = new MutableLiveData<>();
    //Swaps
    private MutableLiveData<List<Swap>> SwapList = new MutableLiveData<>();
    private MutableLiveData<Swap> SpecificSwap = new MutableLiveData<>();
    private MutableLiveData<List<SwapOffer>> SwapOfferList = new MutableLiveData<>();
    private MutableLiveData<SwapOffer> SpecificSwapOffer = new MutableLiveData<>();
    //Search Results
    private MutableLiveData<List<Plant>> SearchResults = new MutableLiveData<>();
    private MutableLiveData<Plant> SpecificPlant = new MutableLiveData<>();
    //Current user
    private MutableLiveData<PlantSwapUser> currentUser = new MutableLiveData<>();

    //Auxiliary
    private final ExecutorService executor;
    private RequestQueue queue;
    private final Context applicationContext;
    private final String trefleURL = "https://trefle.io/api/v1/plants";
    private final String googleURL = "https://maps.googleapis.com/maps/api/geocode/json?&address=";
    private final FirebaseFirestore firebaseDatabase = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final StorageReference photoStorageReference = FirebaseStorage.getInstance().getReference();
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

    public  LiveData<List<Wish>> getWishList() {
        return WishList;
    }

    public LiveData<Wish> getWish() {
        return SpecificWish;
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

    public LiveData<List<SwapOffer>> getSwapOfferList() {
        return SwapOfferList;
    }

    public LiveData<SwapOffer> getSpecificSwapOffer() {
        return SpecificSwapOffer;
    }

    public LiveData<List<Plant>> getSearchResult() {
        return SearchResults;
    }

    public LiveData<Plant> getSpecificPlant() {
        return SpecificPlant;
    }

    public LiveData<PlantSwapUser> getCurrentUser() {
        return currentUser;
    }
    //endregion

    /**
     *
     * FIREBASE METHODS
     * THIS SECTION HANDLES THE DIFFERENT FIREBASE-RELATED
     * CALLS THAT IS NEEDED BY THE APPLICATION
     *
     */
    /**
     *  User Methods
     */
    //region User Methods
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
                                newCurrentUser.setUserId(userID);
                                newCurrentUser.setAddressCoordinates(document.get("addressGpsCoordinates").toString());
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

    //Helper method to execute volley user requests
    private void executeUserVolleyQueue(PlantSwapUser plantSwapUserObject, Map<String, Object> mapUserObject, String userID) {
        Log.d(TAG, "executeUserVolleyQueue: Executing Volley Queue request");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "executeUserVolleyQueue run: Sending GET request");
                if (queue == null) {
                    Log.d(TAG, "executeUserVolleyQueue: Instantiate new Volley Request Queue");
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
     * DELETE USER CHAIN
     */
    //region Delete-User Chain
    public void deleteUserInCloudDatabase(String userId) {
        deleteAllConversationsWithUser(userId);
    }

    private void deleteAllConversationsWithUser(String userId) {
        //TODO USE DELETE ALL CONVERSATIONS FROM USER WHEN IMPLEMENTED
        deleteAllSwapOffersFromUser(userId);
    }

    private void deleteAllSwapOffersFromUser(String userId) {
        deleteAllUserSwapOffers(userId);
    }

    private void deleteAllSwapsFromUser(String userId) {
        deleteAllUserSwaps(userId);
    }

    private void deleteWishListFromUser(String userId) {
        //TODO USE DELETE ALL WISH LIST ELEMENTS FROM USER WHEN IMPLEMENTED
        deletePlantSwapUser(userId);
    }

    private void deletePlantSwapUser(String userId) {
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: DocumentSnapshot successfully deleted.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error deleting document", e);
                    }
                });
    }
    //endregion
    //endregion

    /**
     *
     *  WISH METHODS
     *
     */
    //region Wish Methods
    //Method to create wish in a user's wish list
    public void addWishToUserWishList(Wish newWish) {
        Log.d(TAG, "addWishToUserWishList: Adding new wish to user's wish list");
        Map<String, Object> wishData = new HashMap<>();
        wishData.put("wishPlant",  newWish.getWishPlant());
        wishData.put("radius", newWish.getRadius());

        Log.d(TAG, "addWishToUserWishList: Adding with");
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(currentUser.getValue().getUserId())
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
    public void readUserWishList(String userID) {
        Log.d(TAG, "readUserWishList: Reading wishlist from user: " + userID);
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userID)
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
                                 Wish wishListWish = new Wish(newWishPlant,
                                         new Double(docRef.get("radius").toString()));
                                 wishListWish.setWishId(document.getId().toString());
                                 //Add it to the temporary holder list
                                 wishListHolder.add(wishListWish);
                             }

                             if (swap) {
                                SwapWishList.postValue(wishListHolder);
                             } else {
                                WishList.postValue(wishListHolder);
                             }
                        }
                        else {
                            Log.d(TAG, "Error getting wishes: ", task.getException());
                        }
                    }

                });
    }

    //Method to find a specific wish from a user's wish list
    public void readUserWish(String wishID) {
        Log.d(TAG, "readUserWish: Reading a specific wish from user's wishlist");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userId)
                .collection(DatabaseConstants.WishCollection).document(wishID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            SpecificWish.postValue(parseWishDocument(document));
                        } else {
                            Log.d(TAG, "onComplete: Error getting documents", task.getException());
                        }
                    }
                });
    }

    //Method to delete wish from a user's wish list
    public void deleteWishFromUserList(String wishID) {
        Log.d(TAG, "deleteWishFromUserWishList: Deleting a wish from user's wish list");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userId)
                .collection(DatabaseConstants.WishCollection).document(wishID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: DocumentSnapshot successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Errore deleting document", e);
                    }
                });
    }

    //Method to update a wish from a user's wish list
    public void updateWishFromUserList(String wishID, Wish updatedWish) {
        Log.d(TAG, "updateWishFromUserWishList: Updating a wish from user's wish list");
        Map<String, Object> wishData = new HashMap<>();
        wishData.put("radius", updatedWish.getRadius());
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(currentUser.getValue().getUserId())
                .collection(DatabaseConstants.WishCollection).document()
                .update(wishData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: DocumentSnapshot successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error deleting document", e);
                    }
                });
    }

    //Method to delete all wishes from a user's wish list
    //TODO IMPLEMENT THIS

    //endregion

    /**
     *
     *  SWAP METHODS
     *
     */
    //region Swap Methods
    //Method to create a swap in the database
    public void createNewSwap(Swap newSwap, Uri uri) {
        Map<String, Object> swapData = new HashMap<>();
        swapData.put("ownerID", currentUser.getValue().getUserId());
        swapData.put("ownerAddressGpsCoordinates", currentUser.getValue().getAddressCoordinates());
        swapData.put("status", newSwap.getStatus());
        swapData.put("plantName", newSwap.getPlantName());
        swapData.put("swapWishes", newSwap.getSwapWishes());

        Log.d(TAG, "onSuccess: Uploading images to Storage");
        StorageReference photoRef = photoStorageReference.child("photos/" + uri.getLastPathSegment());
        photoRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        swapData.put("photo", uri.toString());
                        firebaseDatabase.collection(DatabaseConstants.SwapCollection).document()
                                .set(swapData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Swap added");
                                        readAllSwapsFromUser();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "onFailure: Error writing document", e);
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(applicationContext, "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
    public void readAllSwapsFromUser() {
        firebaseDatabase.collection(DatabaseConstants.SwapCollection)
                .whereEqualTo("ownerID", currentUser.getValue().getUserId())
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

    //Method to update swap in the database
    public void updateSwap(Swap swapObject) {
        Map<String, Object> swapData = new HashMap<>();
        swapData.put("plantName", swapObject.getPlantName());
        swapData.put("swapWishes", swapObject.getSwapWishes());

        firebaseDatabase.collection(DatabaseConstants.SwapCollection).document(swapObject.getSwapId())
                .update(swapData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Swap successfully updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error updating document", e);
                    }
                });
    }

    //Method to delete swap from the database
    public void deleteSwap(String swapId) {
        firebaseDatabase.collection(DatabaseConstants.SwapCollection).document(swapId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: DocumentSnapshot successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Errore deleting document", e);
                    }
                });
    }

    //Method to delete all swaps associated with a user
    public void deleteAllUserSwaps(String userId) {
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
                            deleteListOfSwaps(swapHolder);
                            deleteWishListFromUser(userId);
                        } else {
                            Log.d(TAG, "onComplete: Error getting documents", task.getException());
                        }
                    }
                });
    }

    //Helper method to delete a list of swaps
    private void deleteListOfSwaps(List<Swap> swapHolder) {
        Log.d(TAG, "deleteListOfSwaps: Swaps to be deleted: " + swapHolder.size());
        for (int i=0; i<swapHolder.size(); i++) {
            deleteSwap(swapHolder.get(i).getSwapId());
        }
    }

    //Helper method to parse swap document
    private Swap parseSwapDocument(DocumentSnapshot document) {
        Swap getSwap = new Swap(
                document.get("plantName").toString(),
                document.get("swapWishes").toString()
        );
        getSwap.setStatus(document.get("status").toString());
        getSwap.setImageURL(document.get("photo").toString());
        getSwap.setSwapId(document.getId());
        getSwap.setOwnerAddressGpsCoordinates(document.get("ownerAddressGpsCoordinates").toString());

        return getSwap;
    }
    //endregion

    //Helper method to parse wish document
    private Wish parseWishDocument(DocumentSnapshot document) {
        String[] wishPlant = document.get("wishPlant").toString()
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
        Wish getWish = new Wish(
                newWishPlant,
                new Double(document.get("radius").toString())
        //Add it to the temporary holder list
        );
        getWish.setWishId(document.getId());

        return getWish;
    }
    //Method to create an offer to a swap
    public void createNewOfferToSwap(SwapOffer newSwapOfferObject, String swapId) {
        Map<String, Object> newOffer = new HashMap<>();
        newOffer.put("swapId", swapId);
        newOffer.put("swapOfferUserId", newSwapOfferObject.getSwapOfferUserId());
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
    public void readSpecificSwapOffer(String offerIi) {
        firebaseDatabase.collection(DatabaseConstants.OfferCollection).document(offerIi)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            SwapOffer getOffer = new SwapOffer(document.get("swapID").toString(),
                                    document.get("plantsOffered").toString());
                            getOffer.setSwapOfferUserId(document.get("swapOfferUserID").toString());
                            SpecificSwapOffer.postValue(getOffer);
                        }
                    }
                });
    }

    //Method to delete swap offer from a swap
    public void deleteSwapOffer(String offerId) {
        firebaseDatabase.collection(DatabaseConstants.OfferCollection).document(offerId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: DocumentSnapshot successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Errore deleting document", e);
                    }
                });
    }

    //Method to delete all swap offers associated to a swap
    private void deleteAllSwapSwapOffers(String swapId) {
        firebaseDatabase.collection(DatabaseConstants.OfferCollection)
                .whereEqualTo("swapId", swapId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> swapOfferIdHolder = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                swapOfferIdHolder.add(document.getId());
                            }
                            deleteListOfSwapOffers(swapOfferIdHolder);
                        }
                    }
                });
    }

    private void deleteListOfSwapOffers(List<String> swapOfferIdHolder) {
        Log.d(TAG, "deleteListOfSwapOffers: Swap Offers to be deleted: " + swapOfferIdHolder.size());
        for (int i=0; i<swapOfferIdHolder.size(); i++) {
            deleteSwapOffer(swapOfferIdHolder.get(i));
        }
    }

    //Method to delete all swap offers associated with a user
    private void deleteAllUserSwapOffers(String userId) {
        firebaseDatabase.collection(DatabaseConstants.OfferCollection)
                .whereEqualTo("swapOfferUserId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> swapOfferIdHolder = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                swapOfferIdHolder.add(document.getId());
                            }
                            deleteListOfSwapOffers(swapOfferIdHolder);
                            deleteAllSwapsFromUser(userId);
                        } else {
                            Log.d(TAG, "onComplete: Error getting documents", task.getException());
                        }
                    }
                });
    }
    //endregion

    /**
     *
     *  MESSAGE METHODS
     *
     */
    //region Message Methods
    //TODO Implement this
    //TODO Implement this
    //TODO Implement this
    //TODO Implement this
    //endregion

    /**
     *
     * TREFLE API METHODS
     * THIS SECTION HANDLES THE DIFFERENT API-RELATED
     * CALLS THAT IS NEEDED BY THE APPLICATION
     *
     */
    //region Trefle API Methods
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
    //endregion
}
