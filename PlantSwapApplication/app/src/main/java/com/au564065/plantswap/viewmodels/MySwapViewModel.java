package com.au564065.plantswap.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Swap;
import com.au564065.plantswap.ui.recyclerview.MySwapAdapter;

import java.util.List;

public class MySwapViewModel extends AndroidViewModel {
    private Repository repo;

    public LiveData<List<Swap>> swapList;


    public MySwapViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application);
        repo.readAllSwapsFromUser();
        swapList = repo.getAllSwaps();
    }
}

