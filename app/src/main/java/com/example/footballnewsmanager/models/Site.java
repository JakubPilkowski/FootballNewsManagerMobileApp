package com.example.footballnewsmanager.models;

public class Site {

    private Long id;
    private String name;
    private String logoUrl;
    private String siteUrl;
    private String description;
    private boolean highlighted;


    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public String getSiteUrl() {
        return siteUrl;
    }
}
