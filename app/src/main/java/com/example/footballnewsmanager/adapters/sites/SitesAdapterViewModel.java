package com.example.footballnewsmanager.adapters.sites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.Site;

public class SitesAdapterViewModel extends BaseAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<String> logoUrl = new ObservableField<>();
    public ObservableField<String> clicks = new ObservableField<>();
    public ObservableField<String> language = new ObservableField<>();
    public ObservableField<String> newsCount = new ObservableField<>();

    private Site site;
    private Activity activity;

    @Override
    public void init(Object[] values) {
        site = (Site) values[0];
        activity = (Activity) values[1];
        name.set(site.getName());
        description.set(activity.getResources().getString(R.string.description) +site.getDescription());
        logoUrl.set(site.getLogoUrl());
        clicks.set(String.valueOf(site.getClicks()));
        language.set(site.getLanguage());
        newsCount.set(String.valueOf(site.getNewsCount()));
    }

    public void onSiteClick(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(site.getSiteUrl()));
        activity.startActivity(intent);
    }
}
