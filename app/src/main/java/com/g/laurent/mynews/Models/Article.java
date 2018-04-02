package com.g.laurent.mynews.Models;

public class Article {

    private String imageUrl;
    private String pubDate;
    private String title;
    private String section;
    private String subsection;
    private String webUrl;

    public Article(String imageUrl, String pubDate, String title, String section, String subsection, String webUrl) {

        this.imageUrl = imageUrl;
        this.pubDate = pubDate;
        this.title = title;
        this.section=section;
        this.subsection=subsection;
        this.webUrl = webUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String extract_date(){

        String year = pubDate.substring(0,4);
        String month = pubDate.substring(5,7);
        String day = pubDate.substring(8,10);

        return day + "/" + month + "/" + year;
    }

    public String extract_category(){

        if(section != null){
            if(subsection==null)
                return section;
            else
                return section + " > " + subsection;
        } else if (subsection!=null)
            return subsection;
        else
            return null;

    }
}
