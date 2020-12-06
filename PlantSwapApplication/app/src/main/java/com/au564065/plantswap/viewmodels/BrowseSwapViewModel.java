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

    private void getWishes(){
        List<String> temp = Arrays.asList(onClickedSwap.getSwapWishes().split(", "));
        String temp2 = "Select Plant";
        for(int i = 0; i < temp.size() ; i++){
            if(temp.get(i) == temp2){
                temp.remove(i);
            }
        }
        wishes = temp;
    }

    private void getCommaSep(){
        List<String> temp = wishes;
        StringBuilder sb = new StringBuilder();
        for(String s: temp){
            sb.append(s).append(",");
        }
        String result = sb.deleteCharAt(sb.length()-1).toString();
        commaSep = result;
    }

    public String comma(){
        return commaSep;
    }

    public List<String> Wishes(){
        return wishes;
    }
    public void addMoreSpinners(){
        count.setValue(count.getValue()+1);
    }

    public void deleteSpinner(){
        count.setValue(count.getValue()-1);
    }
}
