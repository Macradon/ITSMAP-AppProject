package com.au564065.plantswap.models.gsonModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Generated GSON Model from http://www.jsonschema2pojo.org/
public class GsonPlant {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("common_name")
    @Expose
    private String commonName;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("scientific_name")
    @Expose
    private String scientificName;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("bibliography")
    @Expose
    private String bibliography;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("family_common_name")
    @Expose
    private String familyCommonName;
    @SerializedName("genus_id")
    @Expose
    private Integer genusId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("synonyms")
    @Expose
    private List<String> synonyms = null;
    @SerializedName("genus")
    @Expose
    private String genus;
    @SerializedName("family")
    @Expose
    private String family;
    @SerializedName("links")
    @Expose
    private GsonLinks links;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBibliography() {
        return bibliography;
    }

    public void setBibliography(String bibliography) {
        this.bibliography = bibliography;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getFamilyCommonName() {
        return familyCommonName;
    }

    public void setFamilyCommonName(String familyCommonName) {
        this.familyCommonName = familyCommonName;
    }

    public Integer getGenusId() {
        return genusId;
    }

    public void setGenusId(Integer genusId) {
        this.genusId = genusId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public GsonLinks getLinks() {
        return links;
    }

    public void setLinks(GsonLinks links) {
        this.links = links;
    }

}
