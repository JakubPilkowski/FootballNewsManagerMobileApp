package pl.android.footballnewsmanager.api.responses.main;

import pl.android.footballnewsmanager.models.UserTeam;

import java.util.List;

public class AllNewsResponse extends NewsResponse {

    private List<UserTeam> proposedTeams;

    public List<UserTeam> getProposedTeams() {
        return proposedTeams;
    }
}
