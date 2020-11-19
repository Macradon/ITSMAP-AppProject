package com.au564065.plantswap.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.au564065.plantswap.models.Plant;

//DAO implementation adapted from course lecture and https://google-developer-training.github.io/android-developer-advanced-course-practicals/unit-6-working-with-architecture-components/lesson-14-room,-livedata,-viewmodel/14-1-a-room-livedata-viewmodel/14-1-a-room-livedata-viewmodel.html#appintro
@Dao
public interface PlantDAO {

    //Add a plant to the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addPlant(Plant plant);

    //Get a specific plant
    //TODO

    //Get a list of all the plants in alphabetical order
    //TODO

    //Delete a specific plant
    @Delete
    void deletePlant(Plant plant);

    //Get the size of the database
    @Query("SELECT COUNT(*) FROM plant_table")
    int getDataCount();

    //Delete all plants from the database
    @Query("DELETE FROM plant_table")
    void deleteAll();
}
