package com.au564065.plantswap.models;

import java.util.List;

public class Swap {

    //Status codes
    private enum statusCode {
        OPEN,
        INPROGRESS,
        CLOSED
    }

    //Attributes
    private statusCode status;
    private String plantName;
    private List<Photo> plantPhotos;
    private List<Plant> wishPlants;

    //Constructor
    public Swap(String plantName, List<Photo> plantPhotos, List<Plant> wishPlants) {
        this.status = statusCode.OPEN;
        this.plantName = plantName;
        this.plantPhotos = plantPhotos;
        this.wishPlants = wishPlants;
    }

    //Getters and setters
    public statusCode getStatus() {
        return status;
    }

    public void setStatus(statusCode status) {
        this.status = status;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public List<Photo> getPlantPhotos() {
        return plantPhotos;
    }

    public void setPlantPhotos(List<Photo> plantPhotos) {
        this.plantPhotos = plantPhotos;
    }

    public List<Plant> getWishPlants() {
        return wishPlants;
    }

    public void setWishPlants(List<Plant> wishPlants) {
        this.wishPlants = wishPlants;
    }
}
