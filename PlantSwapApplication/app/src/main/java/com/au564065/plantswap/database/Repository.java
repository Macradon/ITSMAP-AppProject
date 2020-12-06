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
import com.au564065.plantswap.models.Chatroom;
import com.au564065.plantswap.models.Message;
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
    //Chat data
    private MutableLiveData<List<Chatroom>> chatRoomList = new MutableLiveData<>();
    private MutableLiveData<Chatroom> specificChatRoom = new MutableLiveData<>();
    private MutableLiveData<List<Message>> messageList = new MutableLiveData<>();
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

    //region Getters
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

    public LiveData<List<Chatroom>> getChatRoomList() {
        return chatRoomList;
    }

    public LiveData<Chatroom> getSpecificChatRoom() {
        return specificChatRoom;
    }

    public LiveData<List<Message>> getMessageList() {
        return messageList;
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
        Log.d(TAG, "deleteUserInCloudDatabase: Initiating delete user");
        deleteAllChatRoomsAssociatedWithUserOwner(userId);
    }

    private void deletePlantSwapUser(String userId) {
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: User data successfully deleted.");

                        FirebaseAuth.getInstance().getCurrentUser().delete();
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
                                 wishListWish.setWishId(document.getId());
                                 //Add it to the temporary holder list
                                 wishListHolder.add(wishListWish);
                             }

                             WishList.postValue(wishListHolder);
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
    public void deleteWishFromUserList(String wishID, boolean userDelete) {
        Log.d(TAG, "deleteWishFromUserWishList: Deleting a wish from user's wish list");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userId)
                .collection(DatabaseConstants.WishCollection).document(wishID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Wish successfully deleted");
                        if (userDelete) {
                            deletePlantSwapUser(currentUser.getValue().getUserId());
                        }
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
    public void deleteAllUserWishes(String userId) {
        Log.d(TAG, "deleteAllUserWishes: deleting all wishes from user " + userId);
        firebaseDatabase.collection(DatabaseConstants.UserCollection).document(userId)
                .collection(DatabaseConstants.WishCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                deleteWishFromUserList(document.getId(), true);
                            }
                        }
                    }
                });
    }

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
                        readAllSwapsFromUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error updating document", e);
                    }
                });
    }

    //Method to delete swap from the database, calls delete swap offers
    public void deleteSwap(String swapId) {
        deleteAllSwapSwapOffers(swapId);
    }

    /**
     * Chain used when deleting user
     * Delete all Swaps associated with user
     */
    //region Delete all Swaps associated with user
    //Method to delete all swaps associated with a user
    public void deleteAllUserSwaps(String userId) {
        Log.d(TAG, "deleteAllUserSwaps: deleting all swaps from " + userId);
        deleteAllUserSwapOffers(userId);
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
        deleteAllUserWishes(currentUser.getValue().getUserId());
    }
    //endregion

    //Helper method to parse swap document
    private Swap parseSwapDocument(DocumentSnapshot document) {
        Swap getSwap = new Swap(
                document.get("plantName").toString(),
                document.get("swapWishes").toString()
        );
        getSwap.setStatus(document.get("status").toString());
        getSwap.setImageURL(document.get("photo").toString());
        getSwap.setSwapId(document.getId());
        getSwap.setOwnerId(document.get("ownerID").toString());
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
    public void createNewOfferToSwap(SwapOffer newSwapOfferObject, String swapId, String swapOwnerId) {
        Map<String, Object> newOffer = new HashMap<>();
        newOffer.put("swapId", swapId);
        newOffer.put("swapOwnerId", swapOwnerId);
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

    //Method to read a specific offer
    public void readSpecificSwapOffer(String offerId) {
        firebaseDatabase.collection(DatabaseConstants.OfferCollection).document(offerId)
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

    //Method to read all  offers to a swap
    public void readAllSwapSwapOffers(String swapId) {
        firebaseDatabase.collection(DatabaseConstants.OfferCollection)
                .whereEqualTo("swapId", swapId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<SwapOffer> swapOfferHolder = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SwapOffer getSwapOffer = new SwapOffer(document.get("swapId").toString(),
                                        document.get("plantsOffered").toString());
                                getSwapOffer.setSwapOfferUserId(document.get("swapOfferUserId").toString());
                                getSwapOffer.setSwapOfferId(document.getId());
                            }
                        }
                    }
                });
    }

    //Method to update swap offer
    public void updateSpecificSwapOffer(SwapOffer swapOfferObject) {
        firebaseDatabase.collection(DatabaseConstants.OfferCollection).document(swapOfferObject.getSwapOfferId())
                .update("plantsOffered", swapOfferObject.getPlantsOffered())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Swap offer updated");
                            readAllSwapSwapOffers(swapOfferObject.getSwapId());
                        }
                    }
                });
    }

    /**
     * Chain used when deleting swap
     * Delete all Swap Offers from Swap
     * Finalize delete Swap
     */
    //region Delete all Swap Offers from Swap
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
                            deleteListOfSwapOffers(swapId, swapOfferIdHolder);
                        }
                    }
                });
    }
    //Helper method to delete a list of swap offers, finalize by deleting the swap
    private void deleteListOfSwapOffers(String swapId, List<String> swapOfferIdHolder) {
        Log.d(TAG, "deleteListOfSwapOffers: Swap Offers to be deleted: " + swapOfferIdHolder.size());
        for (int i=0; i<swapOfferIdHolder.size(); i++) {
            deleteSwapOffer(swapOfferIdHolder.get(i));
        }
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
                        Log.w(TAG, "onFailure: Error deleting document", e);
                    }
                });
    }
    //endregion

    /**
     * Chain used when deleting user
     * Delete all Swap Offers from User
     */
    //region Delete All User Swap Offers
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
                            deleteListOfSwapOffersFromUser(userId, swapOfferIdHolder);
                        } else {
                            Log.d(TAG, "onComplete: Error getting documents", task.getException());
                        }
                    }
                });
    }
    //Method to delete all swap offers in a list associated with a user
    private void deleteListOfSwapOffersFromUser(String userId, List<String> swapOfferIdHolder) {
        for (int i=0; i<swapOfferIdHolder.size(); i++) {
            deleteSwapOffer(swapOfferIdHolder.get(i));
        }
    }
    //endregion

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
    //endregion

    /**
     *
     *  MESSAGE METHODS
     *
     */
    //region Chat rooms
    //Method to create a new chat room
    public void createNewChatRoom(SwapOffer swapOfferObject) {
        Map<String, Object> newChatRoom = new HashMap<>();
        newChatRoom.put("ownerUserId", swapOfferObject.getSwapOwnerId());
        newChatRoom.put("offerUserId", swapOfferObject.getSwapOfferUserId());
        newChatRoom.put("swapId", swapOfferObject.getSwapId());
        newChatRoom.put("offerId", swapOfferObject.getSwapOfferId());

        firebaseDatabase.collection(DatabaseConstants.ChatCollection).document()
                .set(newChatRoom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Chat Room created");
                        readAllChatRoomsAssociatedWithUserAsOwner();
                    }
                });
    }

    //Method to read a specific chat room
    public void readSpecificChatRoom(String chatRoomId) {
        firebaseDatabase.collection(DatabaseConstants.ChatCollection).document(chatRoomId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Chatroom specificRoom = new Chatroom(
                                    document.get("ownerUserId").toString(),
                                    document.get("offerUserId").toString(),
                                    document.get("swapId").toString(),
                                    document.get("offerId").toString()
                            );
                            specificRoom.setChatId(document.getId());

                            specificChatRoom.postValue(specificRoom);

                            readAllChatMessages(document.getId());
                        }
                    }
                });
    }

    //Method to read all chat rooms associated with the user as owner
    //this method also calls the next method to also get all the rooms
    //associated with the user as offer
    public void readAllChatRoomsAssociatedWithUserAsOwner() {
        List<Chatroom> chatRoomListHolder = new ArrayList<>();
        firebaseDatabase.collection(DatabaseConstants.ChatCollection)
                .whereEqualTo("ownerId", currentUser.getValue().getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Chatroom getChatRoom = new Chatroom(
                                        document.get("ownerUserId").toString(),
                                        document.get("offerUserId").toString(),
                                        document.get("swapId").toString(),
                                        document.get("offerId").toString()
                                );
                                getChatRoom.setChatId(document.getId());

                                chatRoomListHolder.add(getChatRoom);
                            }
                            readAllChatRoomsAssociatedWithUserAsOffer(chatRoomListHolder);
                        }
                    }
                });
    }
    private void readAllChatRoomsAssociatedWithUserAsOffer(List<Chatroom> chatRoomListHolder) {
        List<Chatroom> chatRoomListHolderElectricBoogaloo = chatRoomListHolder;
        firebaseDatabase.collection(DatabaseConstants.ChatCollection)
                .whereEqualTo("offerId", currentUser.getValue().getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Chatroom getChatRoom = new Chatroom(
                                        document.get("ownerUserId").toString(),
                                        document.get("offerUserId").toString(),
                                        document.get("swapId").toString(),
                                        document.get("offerId").toString()
                                );
                                getChatRoom.setChatId(document.getId());

                                chatRoomListHolder.add(getChatRoom);
                            }
                            chatRoomList.postValue(chatRoomListHolderElectricBoogaloo);
                        }
                    }
                });
    }

    //Method to delete a specific chat room
    public void deleteSpecificChatRoom(String chatRoomId) {
        Log.d(TAG, "deleteSpecificChatRoom: Initiating delete all messages in chat");
        deleteAllMessagesInChatRoom(chatRoomId);
    }

    private void deleteChatRoom(String chatRoomId) {
        Log.d(TAG, "deleteChatRoom: Deleting chat room");
        firebaseDatabase.collection(DatabaseConstants.ChatCollection).document(chatRoomId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Chat Room successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Error deleting document", e);
                    }
                });
    }

    //Method to delete all chat rooms associated with Swap
    private void deleteAllChatRoomsAssociatedWithSwap(String swapId) {
        firebaseDatabase.collection(DatabaseConstants.ChatCollection)
                .whereEqualTo("swapId", swapId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Deleting chat rooms");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                deleteSpecificChatRoom(document.getId());
                            }
                        }
                    }
                });
    }

    //Method to delete all chat rooms associated with User
    private void deleteAllChatRoomsAssociatedWithUserOwner(String userId) {
        Log.d(TAG, "deleteAllChatRoomsAssociatedWithUserOwner: Deleting all chatrooms and their messages");
        List<Chatroom> chatRoomListHolder = new ArrayList<>();
        firebaseDatabase.collection(DatabaseConstants.ChatCollection)
                .whereEqualTo("ownerId", currentUser.getValue().getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Chatroom getChatRoom = new Chatroom(
                                        document.get("ownerUserId").toString(),
                                        document.get("offerUserId").toString(),
                                        document.get("swapId").toString(),
                                        document.get("offerId").toString()
                                );
                                getChatRoom.setChatId(document.getId());

                                chatRoomListHolder.add(getChatRoom);
                            }
                            deleteAllChatRoomsAssociatedWithUserOffer(chatRoomListHolder);
                        }
                    }
                });
    }

    private void deleteAllChatRoomsAssociatedWithUserOffer(List<Chatroom> chatRoomListHolder) {
        Log.d(TAG, "deleteAllChatRoomsAssociatedWithUserOffer: Deleting all chatrooms and their messages");
        List<Chatroom> chatRoomListHolderElectricBoogaloo = chatRoomListHolder;
        firebaseDatabase.collection(DatabaseConstants.ChatCollection)
                .whereEqualTo("offerId", currentUser.getValue().getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Chatroom getChatRoom = new Chatroom(
                                        document.get("ownerUserId").toString(),
                                        document.get("offerUserId").toString(),
                                        document.get("swapId").toString(),
                                        document.get("offerId").toString()
                                );
                                getChatRoom.setChatId(document.getId());

                                chatRoomListHolderElectricBoogaloo.add(getChatRoom);
                            }

                            //deleting all chat rooms in chatRoomListHolderElectricBoogaloo
                            for (int i=0; i<chatRoomListHolderElectricBoogaloo.size(); i++) {
                                deleteSpecificChatRoom(chatRoomListHolderElectricBoogaloo.get(i).getChatId());
                            }
                            deleteAllUserSwaps(currentUser.getValue().getUserId());
                        }
                    }
                });
    }
    //endregion

    //region Messages
    //Method to create a new message
    public void createNewMessage(String chatRoomId, Message messageObject) {
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("name", messageObject.getName());
        newMessage.put("senderId", messageObject.getSenderId());
        newMessage.put("message", messageObject.getMessage());

        firebaseDatabase.collection(DatabaseConstants.ChatCollection).document(chatRoomId)
                .collection(DatabaseConstants.MessageCollection).document()
                .set(newMessage)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Message added");
                        readAllChatMessages(chatRoomId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Error writing document");
                    }
                });
    }

    //Method to read all messages in a chat room
    public void readAllChatMessages(String chatRoomId) {
        firebaseDatabase.collection(DatabaseConstants.ChatCollection).document(chatRoomId)
                .collection(DatabaseConstants.MessageCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Message> messageListHolder = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Message getMessage = new Message(
                                        document.get("name").toString(),
                                        document.get("senderId").toString(),
                                        document.get("message").toString()
                                );
                                messageListHolder.add(getMessage);
                            }
                            messageList.postValue(messageListHolder);
                        }
                    }
                });
    }

    private void deleteSpecificMessage(String chatRoomId, String messageId) {
        Log.d(TAG, "deleteSpecificMessage: Deleting message " + messageId);
        firebaseDatabase.collection(DatabaseConstants.ChatCollection).document(chatRoomId)
                .collection(DatabaseConstants.MessageCollection).document(messageId)
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
                        Log.d(TAG, "onFailure: Error deleting document", e);
                    }
                });
    }

    //Method to delete all messages in chat room
    private void deleteAllMessagesInChatRoom(String chatRoomId) {
        Log.d(TAG, "deleteAllMessagesInChatRoom: deleting all messages from room " + chatRoomId);
        firebaseDatabase.collection(DatabaseConstants.ChatCollection).document(chatRoomId)
                .collection(DatabaseConstants.MessageCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                deleteSpecificMessage(chatRoomId, document.getId());
                            }
                            deleteChatRoom(chatRoomId);
                        }
                    }
                });
    }
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
