package com.example.footballnewsmanager.api.responses.proposed;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.Team;

import java.util.List;

public class ProposedTeamsResponse extends BaseResponse {

    private List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }
}
