package com.au564065.plantswap.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plant_table")
public class Plant {

    //Attributes
    @PrimaryKey
    @NonNull
    private String scientificName;

    private String commonName;
    private String imageURL;

    //Constructor
    public Plant(@NonNull String scientificName, String commonName, String imageURL) {
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.imageURL = imageURL;
    }

    //Getters and setters
    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
