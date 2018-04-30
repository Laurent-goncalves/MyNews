package com.g.laurent.mynews.Models;

public class Search_request {

    private String type_search;
    private String query;
    private String filterq;
    private String begindate;
    private String enddate;

    public Search_request(String type_search, String query, String filterq, String begindate, String enddate) {

        this.type_search=type_search;
        this.query=query;
        this.begindate=begindate;
        this.filterq=filterq;
        this.enddate=enddate;
    }

    public String getType_search() {
        return type_search;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFilterq() {
        return filterq;
    }

    public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
