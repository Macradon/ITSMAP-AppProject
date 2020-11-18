package com.au564065.plantswap.database;

import androidx.lifecycle.LiveData;

import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.Swap;

import java.util.List;

//Singleton repository code adapted from lecture 7 Tracker application
public class Repository {

    //Singleton Repository instance
    private static Repository INSTANCE = null;

    //Cached list of plants in WishList
    private static LiveData<List<Plant>> WishList;
    //Cached list of plants for swap
    private static LiveData<List<Swap>> SwapList;
    //Cached search history
    private static LiveData<List<Plant>> SearchHistory;

    //Empty Repository constructor that will never be called
    private Repository(){

    }

    //Method returns an instance of Repository and lazyloads the instance on first call.
    public static Repository getInstance() {
        if (INSTANCE==null) INSTANCE = new Repository();
        return INSTANCE;
    }
}
