package com.au564065.plantswap.models;

public class SwapOffer {
    private String swapId;
    private String swapOfferUserId;
    private String plantsOffered;

    public SwapOffer(String swapId, String plantsOffered) {
        this.swapId = swapId;
        this.plantsOffered = plantsOffered;
    }

    public String getSwapId() {
        return swapId;
    }

    public void setSwapId(String swapId) {
        this.swapId = swapId;
    }

    public String getSwapOfferUserId() {
        return swapOfferUserId;
    }

    public void setSwapOfferUserId(String swapOfferUserId) {
        this.swapOfferUserId = swapOfferUserId;
    }

    public String getPlantsOffered() {
        return plantsOffered;
    }

    public void setPlantsOffered(String plantsOffered) {
        this.plantsOffered = plantsOffered;
    }
}
