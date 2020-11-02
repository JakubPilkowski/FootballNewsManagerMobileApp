package com.example.footballnewsmanager.models;

public class Team {

    private String name;
    private String logoUrl;
    private Long clicks;
    private Long newsCount;
    private Long chosenAmount;

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
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
