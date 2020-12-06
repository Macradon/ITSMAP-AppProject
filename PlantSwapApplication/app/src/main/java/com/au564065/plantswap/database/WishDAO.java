package com.au564065.plantswap.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.au564065.plantswap.models.Wish;

import java.util.List;

@Dao
public interface WishDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addWish(Wish wish);

    @Delete
    void deleteWish(Wish wish);

    @Update
    void updateWish(Wish wish);
}
