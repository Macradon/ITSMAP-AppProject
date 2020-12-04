package com.au564065.plantswap.models.gsonPlantModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Generated GSON Model from http://www.jsonschema2pojo.org/
public class Meta {

    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
