package com.example.footballnewsmanager.api.responses.sites;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.Site;

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
