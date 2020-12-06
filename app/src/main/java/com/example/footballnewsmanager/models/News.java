package com.example.footballnewsmanager.models;

import java.util.ArrayList;

public class News {

    private Long siteId;
    private Long id;
    private String title;
    private String newsUrl;
    private String imageUrl;
    private String date;
    private Site site;
    private ArrayList<NewsTag> tags;
    private double popularity;
    private Long clicks;
    private Long likes;
    private Long dislikes;
    private boolean highlighted;


    public Long getSiteId() {
        return siteId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    public double getPopularity() {
        return popularity;
    }

    public Long getLikes() {
        return likes;
    }

    public Long getDislikes() {
        return dislikes;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public Site getSite() {
        return site;
    }

    public ArrayList<NewsTag> getTags() {
        return tags;
    }

    public Long getClicks() {
        return clicks;
    }
}
