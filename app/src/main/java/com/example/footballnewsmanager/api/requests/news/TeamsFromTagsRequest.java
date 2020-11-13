package com.example.footballnewsmanager.api.requests.news;

import com.example.footballnewsmanager.models.Tag;

import java.util.List;

public class TeamsFromTagsRequest {
    private List<Tag> tags;

    public TeamsFromTagsRequest(List<Tag> tags) {
        this.tags = tags;
    }
}
