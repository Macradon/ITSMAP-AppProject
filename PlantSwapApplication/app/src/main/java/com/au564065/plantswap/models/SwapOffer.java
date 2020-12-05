package com.au564065.plantswap.models;

import java.util.List;

public class SwapOffer {
    private String swapID;
    private String swapOfferUserID;
    private String plantsOffered;

    public SwapOffer(String swapID, String plantsOffered) {
        this.swapID = swapID;
        this.plantsOffered = plantsOffered;
    }

    public String getSwapID() {
        return swapID;
    }

    public void setSwapID(String swapID) {
        this.swapID = swapID;
    }

    public String getSwapOfferUserID() {
        return swapOfferUserID;
    }

    public void setSwapOfferUserID(String swapOfferUserID) {
        this.swapOfferUserID = swapOfferUserID;
    }

    public String getPlantsOffered() {
        return plantsOffered;
    }

    public void setPlantsOffered(String plantsOffered) {
        this.plantsOffered = plantsOffered;
    }
}
