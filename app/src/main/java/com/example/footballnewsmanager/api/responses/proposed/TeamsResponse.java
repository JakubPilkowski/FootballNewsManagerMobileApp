package com.example.footballnewsmanager.api.responses.proposed;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.List;

public class TeamsResponse {

    private List<UserTeam> teams;

    public List<UserTeam> getTeams() {
        return teams;
    }
}
