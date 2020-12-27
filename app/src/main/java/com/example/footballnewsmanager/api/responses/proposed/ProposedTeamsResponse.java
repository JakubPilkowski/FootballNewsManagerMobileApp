package com.example.footballnewsmanager.api.responses.proposed;

import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.User;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.List;

public class ProposedTeamsResponse {
    private List<UserTeam> teams;
    private int pages;

    public List<UserTeam> getTeams() {
        return teams;
    }

    public int getPages() {
        return pages;
    }
}
