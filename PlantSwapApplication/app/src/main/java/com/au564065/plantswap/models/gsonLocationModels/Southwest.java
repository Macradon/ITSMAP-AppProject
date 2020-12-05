
package com.au564065.plantswap.models.gsonLocationModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Generated GSON Model from http://www.jsonschema2pojo.org/
public class Southwest {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
