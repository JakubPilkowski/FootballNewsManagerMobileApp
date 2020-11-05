package com.example.footballnewsmanager.api.responses.proposed;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.Site;

import java.util.ArrayList;

public class ProposedSitesResponse extends BaseResponse {

    private ArrayList<Site> sites;

    public ArrayList<Site> getSites() {
        return sites;
    }
}
