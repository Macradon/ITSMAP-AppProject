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
import com.au564065.plantswap.models.Wish;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class PlantViewModel extends AndroidViewModel {
    //insert repo
    //LiveData<List<Plant>> plantLive; //en repo funktion
    private Repository repo;
    private List<Plant> getClickedPlant;
    private Plant onClickedPlant;

    public PlantViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Plant>> getPlants() {

       return repo.getSearchResult();

    }

    public void saveAdapterList(List<Plant> plants){
        getClickedPlant = plants;
    }


    public void SearchPlant(String searchText){

        repo.fetchPlantFromAPI(searchText);

    }

    public Plant getOnClickedPlant() {
        return onClickedPlant;
    }

    public List<Plant> GetClickedPlant() {
        return getClickedPlant;
    }

    public void addToWish(int radius){
        Wish wish = new Wish(onClickedPlant, radius);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String user = firebaseAuth.getCurrentUser().getUid();

        repo.addWishToUserWishList(wish, user);
    }

    public void setOnClickedPlant(int index){
        onClickedPlant = getClickedPlant.get(index);
    }


}
