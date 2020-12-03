package com.au564065.plantswap.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantViewModel extends AndroidViewModel {
    //insert repo
    //LiveData<List<Plant>> plantLive; //en repo funktion
    Repository repo;

    public PlantViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Plant>> getPlants() {

       return repo.getSearchResult();

    }

    public void SearchPlant(String searchText){

        repo.fetchPlantFromAPI(searchText);

    }


}
