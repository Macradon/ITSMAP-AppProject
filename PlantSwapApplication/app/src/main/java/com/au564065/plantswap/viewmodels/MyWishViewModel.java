package com.au564065.plantswap.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.ui.recyclerview.MyWishAdapter;

import java.util.List;

public class MyWishViewModel extends AndroidViewModel {

    public Repository repo;
    public List<Wish> getClickedWish;
    public Wish onClickedWish;
    public MyWishAdapter listAdapter;
    public androidx.recyclerview.widget.LinearLayoutManager layoutManager;

    public MyWishViewModel(@NonNull Application application) {
        super(application);
        repo = Repository.getInstance(application.getApplicationContext());
    }

    //get all
    public LiveData<List<Wish>> getAllWishes() {

        return repo.getWishList();

    }


    public void saveAdapterList(List<Wish> wishes){
        getClickedWish = wishes;
    }

    //enter edit view
    public void OnClicked(int index){
        onClickedWish = getClickedWish.get(index);
    }
}
