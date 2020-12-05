package com.au564065.plantswap.models.gsonPlantModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Generated GSON Model from http://www.jsonschema2pojo.org/
public class GsonLinks {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("plant")
    @Expose
    private String plant;
    @SerializedName("genus")
    @Expose
    private String genus;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }
}
