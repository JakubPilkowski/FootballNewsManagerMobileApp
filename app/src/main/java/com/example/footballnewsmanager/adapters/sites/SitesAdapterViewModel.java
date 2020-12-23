package com.example.footballnewsmanager.adapters.sites;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.Site;

public class SitesAdapterViewModel extends BaseAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<String> logoUrl = new ObservableField<>();
    public ObservableField<String> clicks = new ObservableField<>();
    public ObservableField<String> newsCount = new ObservableField<>();

    private Site site;

    @Override
    public void init(Object[] values) {
        site = (Site) values[0];
        name.set(site.getName());
        description.set(site.getDescription());
        logoUrl.set(site.getLogoUrl());
        clicks.set(String.valueOf(site.getClicks()));
        newsCount.set(String.valueOf(site.getNewsCount()));
    }

    public void onSiteClick(){

    }
}
