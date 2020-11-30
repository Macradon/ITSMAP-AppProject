package com.au564065.plantswap.models;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public class Wish {

    @PrimaryKey
    @NonNull
    private String plantName;

    private int radius;

    //Constructor
    public Wish(@NonNull String plantName, int radius) {
        this.plantName = plantName;
        this.radius = radius;
    }


    //Getters and setters
    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
