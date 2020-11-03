package com.example.footballnewsmanager.adapters.propsed_sites;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.Site;

public class ProposedSitesAdapterViewModel extends BaseAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    private Site site;

    @Override
    public void init(Object[] values) {
        site = (Site) values[0];
        name.set(site.getName());
        imageUrl.set(site.getLogoUrl());
    }
}
