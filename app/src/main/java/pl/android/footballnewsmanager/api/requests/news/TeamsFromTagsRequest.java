package pl.android.footballnewsmanager.api.requests.news;

import java.util.List;

import pl.android.footballnewsmanager.models.TeamNews;

public class TeamsFromTagsRequest {
    private List<TeamNews> teamNews;

    public TeamsFromTagsRequest(List<TeamNews> teamNews) {
        this.teamNews = teamNews;
    }
}
