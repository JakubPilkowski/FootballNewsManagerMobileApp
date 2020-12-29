package com.example.footballnewsmanager.adapters.manage_teams.all;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.fragments.manage_teams.all_teams.ManageTeamsFromLeagueFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.League;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.Observable;

public class ManageLeaguesAdapterViewModel extends BaseAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> img = new ObservableField<>();

    private League league;
    private Navigator navigator;
    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    @Override
    public void init(Object[] values) {
        league = (League) values[0];
        navigator = (Navigator) values[1];
        recyclerViewItemsListener = (RecyclerViewItemsListener<UserTeam>) values[2];
        name.set(league.getName());
        img.set(league.getLogoUrl());
    }

    public void onClick(){
        navigator.attach(ManageTeamsFromLeagueFragment.newInstance(league.getName(), league.getId(), recyclerViewItemsListener), ManageTeamsFromLeagueFragment.TAG);
    }
}
