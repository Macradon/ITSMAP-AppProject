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
    private String ownerID;
    private String ownerAddressGpsCoordinates;
    private statusCode status;
    private String plantName;
    private List<Photo> plantPhotos;
    private List<Plant> ownerWishes;

    //Constructor
    public Swap(PlantSwapUser userObject, String ownerCoordinates, String plantName, List<Photo> plantPhotos, List<Plant> plantWishes) {
        this.ownerID = userObject.getEmail();
        this.ownerAddressGpsCoordinates = ownerCoordinates;
        this.status = statusCode.OPEN;
        this.plantName = plantName;
        this.plantPhotos = plantPhotos;
        this.ownerWishes = plantWishes;
    }

    //Getters and setters
    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerAddressGpsCoordinates() {
        return ownerAddressGpsCoordinates;
    }

    public void setOwnerAddressGpsCoordinates(String ownerAddressGpsCoordinates) {
        this.ownerAddressGpsCoordinates = ownerAddressGpsCoordinates;
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
        return ownerWishes;
    }

    public void setWishPlants(List<Plant> wishPlants) {
        this.ownerWishes = wishPlants;
    }

    public List<Plant> getOwnerWishes() {
        return ownerWishes;
    }

    public void setOwnerWishes(List<Plant> ownerWishes) {
        this.ownerWishes = ownerWishes;
    }
}
