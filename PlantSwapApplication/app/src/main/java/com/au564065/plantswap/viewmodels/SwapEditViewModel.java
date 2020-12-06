package com.au564065.plantswap.viewmodels;

import android.app.Application;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.au564065.plantswap.activities.myswap.SwapEditActivity;
import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.models.Wish;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwapEditViewModel extends AndroidViewModel {
    private final Repository repo;

    public Swap swap;
    public String photoPath = "";
    public Uri photoURI = null;
    public Boolean isNew = false;
    public List<String> wishOptions = null;

    public SwapEditViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application);
        swap = new Swap("", "");
    }

    public LiveData<Swap> getSwap(String swapId) {
        repo.readSpecificSwap(swapId);
        return repo.getSwap();
    }

    public void saveSwap() {
        if(isNew) {
            repo.createNewSwap(swap, photoURI);
        } else {
            repo.updateSwap(swap);
        }
    }



    public void deleteSwap() {
        repo.deleteSwap(swap.getSwapId());
    }

    public LiveData<List<Wish>> getWishes() {
            repo.readUserWishList(repo.getCurrentUser().getValue().getUserId(), false);
            return  repo.getWishList();
    }
}