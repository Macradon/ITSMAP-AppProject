package com.au564065.plantswap.models.gsonModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Generated GSON Model from http://www.jsonschema2pojo.org/
public class ApiJsonResponse {

    @SerializedName("data")
    @Expose
    private List<GsonPlant> data = null;
    @SerializedName("links")
    @Expose
    private GsonLinks links;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<GsonPlant> getData() {
        return data;
    }

    public void setData(List<GsonPlant> data) {
        this.data = data;
    }

    public GsonLinks getLinks() {
        return links;
    }

    public void setLinks(GsonLinks links) {
        this.links = links;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
