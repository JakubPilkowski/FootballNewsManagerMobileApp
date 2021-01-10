package pl.android.footballnewsmanager.api.requests.proposed;

import pl.android.footballnewsmanager.models.Site;
import pl.android.footballnewsmanager.models.Team;

import java.util.List;

public class UserSettingsRequest {

    private List<Team> favouriteTeams;
    private List<Site> chosenSites;

    public UserSettingsRequest(List<Team> favouriteTeams, List<Site> chosenSites) {
        this.favouriteTeams = favouriteTeams;
        this.chosenSites = chosenSites;
    }
}
