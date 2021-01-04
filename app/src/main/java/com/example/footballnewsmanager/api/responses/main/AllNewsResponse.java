package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.models.UserTeam;

import java.util.List;

public class AllNewsResponse extends NewsResponse {

    private List<UserTeam> proposedTeams;

    public List<UserTeam> getProposedTeams() {
        return proposedTeams;
    }
}
