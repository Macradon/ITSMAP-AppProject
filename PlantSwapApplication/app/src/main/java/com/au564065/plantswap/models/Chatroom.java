package com.au564065.plantswap.models;

import java.util.List;

public class Chatroom {
    private String chatId;
    private String chatName;
    private String ownerUserId;
    private String offerUserId;
    private String swapId;
    private String offerId;
    private List<Message> messageList;

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

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
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

    public List<Message> getMessageList() {
        return messageList;
    }

    public void addMessageToMessageList(Message message) {
        this.messageList.add(message);
    }
}
