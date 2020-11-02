package com.example.footballnewsmanager.adapters.proposed_teams;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.Team;

public class ProposedTeamsAdapterViewModel extends BaseAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();

    private Team team;

    @Override
    public void init(Object[] values) {
        team = (Team) values[0];
        name.set(team.getName());
        imageUrl.set(team.getLogoUrl());
    }

    public void onClick(){

    }
}
