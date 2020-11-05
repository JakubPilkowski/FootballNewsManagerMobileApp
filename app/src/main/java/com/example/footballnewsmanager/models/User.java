package com.example.footballnewsmanager.models;

import java.util.List;

public class User {
    private String username;

    private List<FavouriteTeam> favouriteTeams;

    private List<UserSite> userSites;

//    private List<UserNewsLike> userLikes;

    private boolean darkMode;

    private Language language;

    private boolean notification;

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

    public boolean isDarkMode() {
        return darkMode;
    }

    public Language getLanguage() {
        return language;
    }

    public boolean isNotification() {
        return notification;
    }

    public boolean isProposedNews() {
        return proposedNews;
    }
}
