package pl.android.footballnewsmanager.api.requests.news;

import pl.android.footballnewsmanager.models.Tag;

import java.util.List;

public class TeamsFromTagsRequest {
    private List<Tag> tags;

    public TeamsFromTagsRequest(List<Tag> tags) {
        this.tags = tags;
    }
}
