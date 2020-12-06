package com.au564065.plantswap.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Swap;

import java.util.List;

public class SwapEditViewModel extends AndroidViewModel {
    private Repository repo;

    public Swap swap;
    public String photoPath = "";
    public Uri photoURI = null;
    public Boolean isNew = false;


    public SwapEditViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application);

    }

    public void setSwap(String swapId) {
        //TODO need repo update
        this.swap = null;
    }

    public void saveSwap() {}

    public void deleteSwap() {}
}