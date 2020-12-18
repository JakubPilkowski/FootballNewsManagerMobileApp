package com.example.footballnewsmanager.models;

import java.util.List;

public class User {
    private String username;

    private List<FavouriteTeam> favouriteTeams;

    private List<UserSite> userSites;

    private String addedDate;

    private Language language;

    private boolean proposedNews;

    public String getUsername() {
        return username;
    }

    public List<FavouriteTeam> getFavouriteTeams() {
        return favouriteTeams;
    }

    public List<UserSite> getUserSites() {
        return userSites;
    }


    public Language getLanguage() {
        return language;
    }


    public boolean isProposedNews() {
        return proposedNews;
    }
}
