package com.au564065.plantswap.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.PlantSwapUser;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileViewModel extends AndroidViewModel {
    //insert repo
    private LiveData<PlantSwapUser> userLiveData;//skal indeholde user data skal nok bruge en viewmodelfactory til at lave en viewmodel med parametere
    private Repository repo;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application.getApplicationContext());
    }

    public LiveData<PlantSwapUser> getData() {
        return repo.getCurrentUser(); //få en user fra firebase som man kan observe
    }

    public void UpdateUserData(PlantSwapUser swapUser){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String user = firebaseAuth.getCurrentUser().getUid();
        repo.updateUserInCloudDatabase(swapUser,user);
    }

    public void DeleteUserData(){
        //fire base: delete user fra firebase
    }
}
