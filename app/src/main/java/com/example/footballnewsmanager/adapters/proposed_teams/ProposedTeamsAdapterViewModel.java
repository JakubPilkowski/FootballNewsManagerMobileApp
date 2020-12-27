package com.example.footballnewsmanager.adapters.proposed_teams;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserTeam;

public class ProposedTeamsAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableInt background  = new ObservableInt();
    public ObservableInt visibility = new ObservableInt();


    private boolean chosen = false;

    private Team team;
    private UserTeam userTeam;

    public void init(UserTeam userTeam) {
        updateBackground();
        this.userTeam = userTeam;
        team = userTeam.getTeam();
        name.set(team.getName());
        imageUrl.set(team.getLogoUrl());
    }

    public void onClick(){
        chosen = !chosen;
        updateBackground();
    }

    private void updateBackground(){
        background.set(chosen ? R.drawable.proposed_item_active : R.drawable.proposed_item);
        visibility.set(chosen ? View.VISIBLE: View.INVISIBLE);
    }

    public boolean isChosen() {
        return chosen;
    }

    public Team getTeam() {
        return team;
    }
}
