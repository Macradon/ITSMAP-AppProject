package com.au564065.plantswap.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.PlantSwapUser;



public class ProfileViewModel extends AndroidViewModel {
    //insert repo
    private LiveData<PlantSwapUser> userLiveData;//skal indeholde user data skal nok bruge en viewmodelfactory til at lave en viewmodel med parametere
    private Repository repo;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repo = 
    }

    public LiveData<PlantSwapUser> getData() {
        return userLiveData; //få en user fra firebase som man kan observe
    }

    public void UpdateUserData(){
        //fire base: update user til firebase
    }

    public void DeleteUserData(){
        //fire base: delete user fra firebase
    }
}
