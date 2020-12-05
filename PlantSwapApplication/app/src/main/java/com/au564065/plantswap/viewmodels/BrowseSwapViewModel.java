package com.au564065.plantswap.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Swap;

import java.util.List;

public class BrowseSwapViewModel extends AndroidViewModel {

    private Repository repo;
    private Swap onClickedSwap;
    private List<Swap> currentSwapList;

    public BrowseSwapViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Swap>> getSwapList(){
        return repo.getSwapList();
    }

    public void saveAdapterList(List<Swap> list){
        currentSwapList = list;
    }

    public Swap getOnClickedSwap(int index) {
        onClickedSwap = currentSwapList.get(index);
        return onClickedSwap;
    }
}
