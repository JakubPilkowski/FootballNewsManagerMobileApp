package com.example.footballnewsmanager.api.requests.proposed;

import com.example.footballnewsmanager.models.Language;
import com.example.footballnewsmanager.models.Site;
import com.example.footballnewsmanager.models.Team;

import java.util.List;

public class UserSettingsRequest {

    private List<Team> favouriteTeams;
    private List<Site> chosenSites;

    private boolean darkMode;
    private boolean notifications;
    private boolean proposedNews;
    private Language language;

    public UserSettingsRequest(List<Team> favouriteTeams, List<Site> chosenSites, boolean darkMode,
                               boolean notifications, boolean proposedNews, Language language) {
        this.favouriteTeams = favouriteTeams;
        this.chosenSites = chosenSites;
        this.darkMode = darkMode;
        this.notifications = notifications;
        this.proposedNews = proposedNews;
        this.language = language;
    }
}
