package pl.android.footballnewsmanager.api.responses.sites;

import pl.android.footballnewsmanager.models.Site;

import java.util.List;

public class SitesResponse {

    private List<Site> sites;
    private Long pages;

    public List<Site> getSites() {
        return sites;
    }

    public Long getPages() {
        return pages;
    }
}
