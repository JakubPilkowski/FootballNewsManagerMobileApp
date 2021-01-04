package com.example.footballnewsmanager.api.requests.proposed;

import com.example.footballnewsmanager.models.Language;
import com.example.footballnewsmanager.models.Site;
import com.example.footballnewsmanager.models.Team;

import java.util.List;

public class UserSettingsRequest {

    private List<Team> favouriteTeams;
    private List<Site> chosenSites;

    public UserSettingsRequest(List<Team> favouriteTeams, List<Site> chosenSites) {
        this.favouriteTeams = favouriteTeams;
        this.chosenSites = chosenSites;
    }
}
