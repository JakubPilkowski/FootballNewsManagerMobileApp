package com.example.footballnewsmanager.api.responses.proposed;

public class ProposedTeamsAndSitesResponse {
    private ProposedTeamsResponse teamsResponse;
    private ProposedSitesResponse sitesResponse;

    public ProposedTeamsAndSitesResponse(ProposedTeamsResponse teamsResponse, ProposedSitesResponse sitesResponse) {
        this.teamsResponse = teamsResponse;
        this.sitesResponse = sitesResponse;
    }

    public ProposedTeamsResponse getTeamsResponse() {
        return teamsResponse;
    }

    public ProposedSitesResponse getSitesResponse() {
        return sitesResponse;
    }
}
