package com.example.footballnewsmanager.fragments.main.news_info;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserTeam;

public class NewsInfoTeamViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> teamUrl = new ObservableField<>();
    public ObservableInt isFavouriteDrawable = new ObservableInt();
    public ObservableField<String> isFavouriteText = new ObservableField<>();


    private UserTeam userTeam;
    private Team team;
    private Activity activity;


    public void init(UserTeam userTeam, Activity activity) {
        this.activity = activity;
        this.userTeam = userTeam;
        team = userTeam.getTeam();
        name.set(team.getName());
        teamUrl.set(team.getLogoUrl());
        updateFavourite(userTeam);
    }


    public void updateFavourite(UserTeam userTeam){
        isFavouriteDrawable.set(userTeam.isFavourite() ? R.drawable.ic_star : R.drawable.transparent_background);
        isFavouriteText.set(userTeam.isFavourite() ? "W ulubionych" : "");
    }

    public void onClick(){
        Intent intent = new Intent(activity, NewsForTeamActivity.class);
        intent.putExtra("id", team.getId());
        intent.putExtra("name", team.getName());
        intent.putExtra("img", team.getLogoUrl());
        intent.putExtra("favourite", userTeam.isFavourite());
        activity.startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS);
    }
}
