package com.au564065.plantswap.models;

public class Swap {

    //Status codes
    private enum statusCode {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }

    //Attributes
    private String swapId;
    private String ownerId;
    private String ownerAddressGpsCoordinates;
    private statusCode status;
    private String plantName;
    private String swapWishes;
    private String imageURL;

    //Constructor
    public Swap(String plantName, String swapWishes) {
        this.status = statusCode.OPEN;
        this.plantName = plantName;
        this.swapWishes = swapWishes;
        imageURL = "";
    }

    //Getters and setters
    public String getSwapId() {
        return swapId;
    }

    public void setSwapId(String swapId) {
        this.swapId = swapId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public void setStatus(String status) {
        switch (status) {
            case "OPEN":
                this.status = statusCode.OPEN;
                break;
            case "IN_PROGRESS":
                this.status = statusCode.IN_PROGRESS;
                break;
            case "CLOSED":
                this.status = statusCode.CLOSED;
                break;
        }
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getSwapWishes() {
        return swapWishes;
    }

    public void setSwapWishes(String swapWishes) {
        this.swapWishes = swapWishes;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
