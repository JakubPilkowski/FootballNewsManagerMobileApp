package com.example.footballnewsmanager.adapters.news.additionalInfo.news;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.Team;

public class AdditionalNewsAdapterViewModel extends BaseAdapterViewModel {

    private Team team;

    public ObservableField<String> imageUrl = new ObservableField<>();

    @Override
    public void init(Object[] values) {
        team = (Team) values[0];
        imageUrl.set(team.getLogoUrl());
    }
}
