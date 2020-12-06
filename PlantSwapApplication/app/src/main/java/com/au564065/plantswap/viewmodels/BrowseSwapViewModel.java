package com.au564065.plantswap.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Swap;

import java.util.Arrays;
import java.util.List;

public class BrowseSwapViewModel extends AndroidViewModel {

    private Repository repo;
    private Swap onClickedSwap;
    private List<Swap> currentSwapList;
    private LiveData<List<Swap>> allSwaps;
    private MutableLiveData<Integer> count;


    public BrowseSwapViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application.getApplicationContext());
        repo.readAllSwaps();
        allSwaps = repo.getAllSwaps();
    }

    public Swap getSwap(){
        return  onClickedSwap;
    }

    public LiveData<List<Swap>> getSwapList(){
        return allSwaps;
    }

    public void saveAdapterList(List<Swap> list){
        currentSwapList = list;
    }

    public Swap getOnClickedSwap(int index) {
        onClickedSwap = currentSwapList.get(index);
        return onClickedSwap;
    }

    public LiveData<Integer> getCount(){
        if(count == null){
            count = new MutableLiveData<Integer>();
            count.setValue(1);
        }
        return count;
    }

    public void addMoreSpinners(){
        count.setValue(count.getValue()+1);
    }

    public void deleteSpinner(){
        count.setValue(count.getValue()-1);
    }
}
