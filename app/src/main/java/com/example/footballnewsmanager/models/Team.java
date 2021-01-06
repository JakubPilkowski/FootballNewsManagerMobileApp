package com.example.footballnewsmanager.models;

public class Team {

    private Long id;
    private String name;
    private String logoUrl;
    private Long clicks;
    private Long newsCount;

    public Long getId() {
        return id;
    }

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

}
