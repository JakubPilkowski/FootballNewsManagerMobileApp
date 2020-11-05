package com.example.footballnewsmanager.models;

public class Site {

    private Long id;
    private String name;
    private String logoUrl;
    private String description;
    private boolean highlighted;
    private Long popularity;
    private Long clicks;
    private Long newsCount;
    private Long chosenAmount;


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

    public Long getPopularity() {
        return popularity;
    }

    public Long getClicks() {
        return clicks;
    }

    public Long getNewsCount() {
        return newsCount;
    }

    public Long getChosenAmount() {
        return chosenAmount;
    }
}
