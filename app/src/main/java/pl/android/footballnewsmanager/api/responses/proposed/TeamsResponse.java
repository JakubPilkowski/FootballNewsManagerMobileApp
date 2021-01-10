package pl.android.footballnewsmanager.api.responses.proposed;

import pl.android.footballnewsmanager.models.UserTeam;

import java.util.List;

public class TeamsResponse {

    private int pages;
    private List<UserTeam> teams;

    public List<UserTeam> getTeams() {
        return teams;
    }

    public int getPages() {
        return pages;
    }
}
