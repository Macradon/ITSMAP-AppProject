package com.au564065.plantswap.models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.au564065.plantswap.database.Converters;

@Entity(tableName = "wish_table")
public class Wish {
    private String wishId;
    private String ownerId;
    private Plant wishPlant;
    private double radius;

    public Wish(Plant wishPlant, double radius) {
            this.wishPlant = wishPlant;
            this.radius = radius;
    }

    public String getWishId() {
        return wishId;
    }

    public void setWishId(String wishId) {
        this.wishId = wishId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
