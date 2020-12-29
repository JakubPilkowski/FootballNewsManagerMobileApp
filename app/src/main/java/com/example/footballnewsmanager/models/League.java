package com.example.footballnewsmanager.models;

import java.util.List;

public class League {
    private Long id;
    private String name;
    private String logoUrl;
    private LeagueType type;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public LeagueType getType() {
        return type;
    }
}
