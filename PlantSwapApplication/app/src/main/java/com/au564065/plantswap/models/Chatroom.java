package com.au564065.plantswap.models;

import java.util.List;

public class Chatroom {
    private String chatId;
    private String ownerUserId;
    private String offerUserId;
    private String swapId;
    private String offerId;

    public Chatroom(String ownerUserId, String offerUserId, String swapId, String offerId) {
        this.ownerUserId = ownerUserId;
        this.offerUserId = offerUserId;
        this.swapId = swapId;
        this.offerId = offerId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getOfferUserId() {
        return offerUserId;
    }

    public void setOfferUserId(String offerUserId) {
        this.offerUserId = offerUserId;
    }

    public String getSwapId() {
        return swapId;
    }

    public void setSwapId(String swapId) {
        this.swapId = swapId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}
