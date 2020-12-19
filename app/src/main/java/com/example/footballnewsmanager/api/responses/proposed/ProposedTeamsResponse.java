package com.example.footballnewsmanager.api.responses.proposed;

import com.example.footballnewsmanager.models.Team;

import java.util.List;

public class ProposedTeamsResponse {
    private List<Team> teams;
    private int pages;

    public List<Team> getTeams() {
        return teams;
    }

    public int getPages() {
        return pages;
    }
}
