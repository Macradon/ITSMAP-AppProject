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
    private String ownerAddressGpsCoordinates;
    private statusCode status;
    private String plantName;

    //Constructor
    public Swap(String userID, String ownerCoordinates, String plantName) {
        this.status = statusCode.OPEN;
        this.plantName = plantName;
    }

    //Getters and setters
    public String getSwapID() {
        return swapID;
    }

    public void setSwapID(String swapID) {
        this.swapID = swapID;
    }

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


}
