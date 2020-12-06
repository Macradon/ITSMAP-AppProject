package com.au564065.plantswap.viewmodels;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.Plant;
import com.au564065.plantswap.models.PlantSwapUser;
import com.au564065.plantswap.models.Wish;
import com.au564065.plantswap.ui.recyclerview.MyWishAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

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

    /*//get all
    public LiveData<List<Wish>> getAllWishes() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        repo.readUserWishList(userId);
        return repo.getWishList();
    }*/

    public LiveData<Wish> getWish()
    {
        repo.readUserWish(onClickedWish.getWishId());
        return repo.getWish();
    }

    public void saveList(List<Wish> wishes)
    {
        getClickedWish = wishes;
    }

    public void updateWish(String wishId, Wish wish)
    {
        repo.updateWishFromUserList(wishId, wish);
    }

    public void addWish(Plant plant, double radius){
        Wish wish = new Wish(plant, radius);
        repo.addWishToUserWishList(wish);
    }

    public void deleteWish(String wishId)
    {
        repo.deleteWishFromUserList(wishId);
    }

    //enter edit view
    public void OnClicked(int index)
    {
        onClickedWish = getClickedWish.get(index);
        repo.readUserWish(onClickedWish.getWishId());
    }
}
