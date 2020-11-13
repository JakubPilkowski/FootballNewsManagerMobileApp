package com.example.footballnewsmanager.adapters.news.additionalInfo;

import android.app.Activity;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.Team;

public class AdditionalTeamsAdapterViewModel extends BaseAdapterViewModel {


    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> logoUrl = new ObservableField<>();
    public ObservableInt addButtonVisibility = new ObservableInt(View.VISIBLE);
    public ObservableInt removeButtonVisibility = new ObservableInt(View.INVISIBLE);

    private Team team;

    @Override
    public void init(Object[] values) {
        team = (Team) values[0];
        name.set(team.getName());
        logoUrl.set(team.getLogoUrl());
    }

    public void addToggle(){
        addButtonVisibility.set(View.INVISIBLE);
        removeButtonVisibility.set(View.VISIBLE);
    }

    public void removeToggle(){
        addButtonVisibility.set(View.VISIBLE);
        removeButtonVisibility.set(View.INVISIBLE);
    }

}
