package com.au564065.plantswap.models;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public class Wish {
    private Plant wishPlant;

    private double radius;

    public Wish(Plant wishPlant, int radius) {
            this.wishPlant = wishPlant;
            this.radius = radius;
        }

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
}
