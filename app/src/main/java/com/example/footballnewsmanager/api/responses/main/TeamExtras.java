package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.models.Team;

import java.util.List;

public class TeamExtras extends BaseNewsAdjustment {
    private List<Team> teams;

    public TeamExtras(String title, NewsInfoType type, List<Team> teams) {
        super(title, type);
        this.teams = teams;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
