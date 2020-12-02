package com.au564065.plantswap.models;

import java.util.List;

public class Swap {

    //Status codes
    private enum statusCode {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }

    //Attributes
    private String swapID;
    private String ownerID;
    private statusCode status;
    private String plantName;
    private List<Photo> plantPhotos;
    private List<Plant> wishPlants;

    //Constructor
    public Swap(PlantSwapUser userObject, String plantName, List<Photo> plantPhotos, List<Plant> plantWishes) {
        this.swapID = plantName + "_" + userObject.getName() + "_" + userObject.getPlantSwaps().size();
        this.ownerID = userObject.getEmail();
        this.status = statusCode.OPEN;
        this.plantName = plantName;
        this.plantPhotos = plantPhotos;
        this.wishPlants = plantWishes;
    }

    //Getters and setters
    public String getSwapID() {
        return swapID;
    }

    public void setSwapID(String swapID) {
        this.swapID = swapID;
    }

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
