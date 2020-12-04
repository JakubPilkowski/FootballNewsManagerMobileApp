package com.example.footballnewsmanager.fragments.main.news_info;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.models.Team;

import java.util.Observable;

public class NewsInfoTeamViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> teamUrl = new ObservableField<>();

    private Team team;

    public void init(Team team) {
        this.team = team;
        name.set(team.getName());
        teamUrl.set(team.getLogoUrl());
    }
}
