package com.example.footballnewsmanager.models;

public class Site {

    private Long id;
    private String name;
    private String logoUrl;
    private String siteUrl;
    private String description;
    private String language;
    private boolean highlighted;
    private Long clicks;
    private Long newsCount;

    public Long getClicks() {
        return clicks;
    }

    public Long getNewsCount() {
        return newsCount;
    }

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

    public String getLanguage() {
        return language;
    }
}
