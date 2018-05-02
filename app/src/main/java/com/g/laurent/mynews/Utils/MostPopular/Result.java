
package com.g.laurent.mynews.Utils.MostPopular;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

public class Result {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("adx_keywords")
    @Expose
    private transient String adxKeywords;
    @SerializedName("column")
    @Expose
    private transient Object column;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("byline")
    @Expose
    private transient String byline;
    @SerializedName("type")
    @Expose
    private transient String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("abstract")
    @Expose
    private transient String _abstract;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("source")
    @Expose
    private transient String source;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("asset_id")
    @Expose
    private transient String assetId;
    @SerializedName("views")
    @Expose
    private transient Integer views;
    @SerializedName("des_facet")
    @Expose
    private transient  ArrayList<String> desFacet;
    @SerializedName("org_facet")
    @Expose
    private transient  ArrayList<String> orgFacet = null;
    @SerializedName("per_facet")
    @Expose
    private transient  String perFacet = null;
    @SerializedName("geo_facet")
    @Expose
    private transient  ArrayList<String> geoFacet = null;
    @SerializedName("media")
    @Expose
    private List<Medium> media = null;

  /*  private String mMedium_string;
    private Medium mMedium;
    private List<Medium> mMedialist;

    public List<Medium> getMedialist() {
        return medialist;
    }

    public void setMedialist(List<Medium> medialist) {
        this.medialist = medialist;
    }

    @SerializedName("media")
    @Expose
    private List<Medium> medialist;*/


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdxKeywords() {
        return adxKeywords;
    }

    public void setAdxKeywords(String adxKeywords) {
        this.adxKeywords = adxKeywords;
    }

    public Object getColumn() {
        return column;
    }

    public void setColumn(Object column) {
        this.column = column;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public ArrayList<String> getDesFacet() {
        return desFacet;
    }

    public void setDesFacet(ArrayList<String> desFacet) {
        this.desFacet = desFacet;
    }

    public ArrayList<String> getOrgFacet() {
        return orgFacet;
    }

    public void setOrgFacet(ArrayList<String> orgFacet) {
        this.orgFacet = orgFacet;
    }

    public String getPerFacet() {
        return perFacet;
    }

    public void setPerFacet(String perFacet) {
        this.perFacet = perFacet;
    }

    public ArrayList<String> getGeoFacet() {
        return geoFacet;
    }

    public void setGeoFacet(ArrayList<String> geoFacet) {
        this.geoFacet = geoFacet;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium>  media) {
        this.media = media;
    }

}
