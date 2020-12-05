package com.au564065.plantswap.models;

import java.util.List;

public class PlantSwapUser {

    //Attributes
    private String userID;
    private String name;
    private String address;
    private String zipCode;
    private String city;
    private String email;
    private String phoneNumber;
    private String addressCoordinates;

    //Constructor
    public PlantSwapUser(String name, String address, String zipCode, String city, String email, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    //Getters and setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressCoordinates() {
        return addressCoordinates;
    }

    public void setAddressCoordinates(String addressCoordinates) {
        this.addressCoordinates = addressCoordinates;
    }
}
