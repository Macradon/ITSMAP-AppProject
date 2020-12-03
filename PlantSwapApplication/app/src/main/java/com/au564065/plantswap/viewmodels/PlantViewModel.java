package com.au564065.plantswap.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModel;
import com.au564065.plantswap.models.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantViewModel extends ViewModel{
    //insert repo
    //LiveData<List<Plant>> plantLive; //en repo funktion
    MutableLiveData<List<Plant>> plantMut;

    public LiveData<List<Plant>> getPlants() {
        return plantMut;
    }

    public void addPlant(String searchText){

        //get plant List back with searchText skulle gerne ske async
        List temp = new ArrayList<>();
        plantMut.setValue(temp);

    }


}
