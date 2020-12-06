package com.au564065.plantswap.models;

import java.util.List;

public class SwapOffer {
    private String swapOfferUserID;
    private List<String> plantsOffered;

    public SwapOffer(String swapOfferUserID, List<String> plantsOffered) {
        this.swapOfferUserID = swapOfferUserID;
        this.plantsOffered = plantsOffered;
    }

    public String getSwapOfferUserID() {
        return swapOfferUserID;
    }

    public void setSwapOfferUserID(String swapOfferUserID) {
        this.swapOfferUserID = swapOfferUserID;
    }

    public List<String> getPlantsOffered() {
        return plantsOffered;
    }

    public void setPlantsOffered(List<String> plantsOffered) {
        this.plantsOffered = plantsOffered;
    }
}
