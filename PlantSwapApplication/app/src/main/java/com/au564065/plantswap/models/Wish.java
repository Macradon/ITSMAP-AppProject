package com.au564065.plantswap.models;

public class Wish {

    //Attributes
    private Plant wishPlant;
    private double radius;
    private String wishId;

    //Constructor
    public Wish(Plant wishPlant, double radius) {
            this.wishPlant = wishPlant;
            this.radius = radius;
    }

    //Getters and setters
    public Plant getWishPlant() {
            return wishPlant;
        }

    public void setWishPlant(Plant wishPlant) {
            this.wishPlant = wishPlant;
        }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getWishId() {
        return wishId;
    }

    public void setWishId(String wishId) {
        this.wishId = wishId;
    }
}
