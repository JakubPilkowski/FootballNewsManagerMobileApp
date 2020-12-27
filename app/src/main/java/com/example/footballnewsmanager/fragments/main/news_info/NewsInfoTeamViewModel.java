package com.example.footballnewsmanager.fragments.main.news_info;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserTeam;

public class NewsInfoTeamViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> teamUrl = new ObservableField<>();

    private UserTeam userTeam;
    private Team team;
    private Activity activity;


    public void init(UserTeam userTeam, Activity activity) {
        this.activity = activity;
        this.userTeam = userTeam;
        team = userTeam.getTeam();
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
