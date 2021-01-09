package pl.android.footballnewsmanager.api.responses.search;

import pl.android.footballnewsmanager.models.SearchResult;

import java.util.List;

public class SearchResponse {
    private List<SearchResult> results;

    public List<SearchResult> getResults() {
        return results;
    }
}
