package com.example.footballnewsmanager.api.responses.search;

import com.example.footballnewsmanager.models.SearchResult;

import java.util.List;

public class SearchResponse {
    private List<SearchResult> results;

    public List<SearchResult> getResults() {
        return results;
    }
}
