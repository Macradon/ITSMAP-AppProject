package com.au564065.plantswap.models;

import java.util.List;

public class SwapOffer {
    private String swapOfferUserID;
    private List<Plant> plantsOffered;

    public SwapOffer(String swapOfferUserID, List<Plant> plantsOffered) {
        this.swapOfferUserID = swapOfferUserID;
        this.plantsOffered = plantsOffered;
    }

    public String getSwapOfferUserID() {
        return swapOfferUserID;
    }

    public void setSwapOfferUserID(String swapOfferUserID) {
        this.swapOfferUserID = swapOfferUserID;
    }

    public List<Plant> getPlantsOffered() {
        return plantsOffered;
    }

    public void setPlantsOffered(List<Plant> plantsOffered) {
        this.plantsOffered = plantsOffered;
    }
}
