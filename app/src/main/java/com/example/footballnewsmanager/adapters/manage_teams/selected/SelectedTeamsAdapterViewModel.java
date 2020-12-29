package com.example.footballnewsmanager.adapters.manage_teams.selected;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.models.Team;

public class SelectedTeamsAdapterViewModel {
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> img = new ObservableField<>();
    public ObservableInt toggleImg = new ObservableInt();

    private Team team;
    boolean added;

    public void init(Team team) {
        this.team = team;
        name.set(team.getName());
        img.set(team.getLogoUrl());
        added = team.getId() % 2 == 0;
        toggleImg.set(added ? R.drawable.ic_add : R.drawable.ic_add_empty);
    }

    public void onTeamClick() {

    }

    public void toggleTeam() {
        added = !added;
        toggleImg.set(added ? R.drawable.ic_add : R.drawable.ic_add_empty);
        //updateLists
    }
}
