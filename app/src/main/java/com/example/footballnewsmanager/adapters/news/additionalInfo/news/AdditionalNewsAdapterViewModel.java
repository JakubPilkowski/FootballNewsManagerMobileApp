package com.example.footballnewsmanager.adapters.news.additionalInfo.news;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserTeam;

public class AdditionalNewsAdapterViewModel extends BaseAdapterViewModel {

    private UserTeam team;

    public ObservableField<String> imageUrl = new ObservableField<>();

    @Override
    public void init(Object[] values) {
        team = (UserTeam) values[0];
        imageUrl.set(team.getTeam().getLogoUrl());
    }
}
