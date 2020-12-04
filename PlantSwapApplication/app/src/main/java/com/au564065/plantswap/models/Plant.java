package com.au564065.plantswap.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.au564065.plantswap.models.gsonPlantModels.GsonPlant;

@Entity(tableName = "plant_table")
public class Plant {

    //Attributes
    @PrimaryKey
    @NonNull
    private String scientificName;

    private String commonName;
    private String imageURL;
    private String genus;
    private String family;

    //Constructor
    public Plant(@NonNull String scientificName, String commonName, String imageURL) {
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.imageURL = imageURL;
    }

    //Constructor for GsonPlant to Plant object
    public Plant(GsonPlant gsonPlantObject) {
        this.scientificName = gsonPlantObject.getScientificName();
        this.commonName = gsonPlantObject.getCommonName();
        this.imageURL = gsonPlantObject.getImageUrl();
        this.genus = gsonPlantObject.getGenus();
        this.family = gsonPlantObject.getFamily();
    }

    //Getters and setters
    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}
