package com.example.footballnewsmanager.fragments.main.news_info;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import com.example.footballnewsmanager.models.Team;

import java.util.Observable;

public class NewsInfoTeamViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> teamUrl = new ObservableField<>();

    private Team team;
    private Activity activity;


    public void init(Team team, Activity activity) {
        this.activity = activity;
        this.team = team;
        name.set(team.getName());
        teamUrl.set(team.getLogoUrl());
    }

    public void onClick(){
        Intent intent = new Intent(activity, NewsForTeamActivity.class);
        intent.putExtra("id", team.getId());
        intent.putExtra("name", team.getName());
        intent.putExtra("img", team.getLogoUrl());
        activity.startActivity(intent);
    }
}
