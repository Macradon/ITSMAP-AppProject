package com.au564065.plantswap.models;

public class Message {

    //Attributes
    private String name;
    private String message;
    private String senderId;

    //Constructor
    public Message(String name, String message, String senderId) {
        this.name = name;
        this.message = message;
        this.senderId = senderId;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
