package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;

import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news.additionalInfo.teams.AdditionalTeamsAdapter;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamsBinding;
import com.example.footballnewsmanager.helpers.TeamsItemScrollListener;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.List;

public class NewsAdditionalInfoViewModel {


    public ObservableField<String> viewTitle = new ObservableField<>();


    public ObservableField<RecyclerView.LayoutManager> layoutManager = new ObservableField<>();
    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public List<UserTeam> teams;

    public void initForTeams(List<UserTeam> teams, Activity activity, AdditionalInfoTeamsBinding binding) {
        this.teams = teams;
        viewTitle.set(activity.getResources().getString(R.string.proposed_teams));
        LinearLayoutManager teamsLayoutManager = new LinearLayoutManager(binding.additionalInfoTeamsRecyclerview.getContext());
        teamsLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.set(teamsLayoutManager);
        binding.additionalInfoTeamsRecyclerview.addOnItemTouchListener(new TeamsItemScrollListener());
        AdditionalTeamsAdapter additionalTeamsAdapter = new AdditionalTeamsAdapter(activity);
        additionalTeamsAdapter.setItems(teams);
        adapterObservable.set(additionalTeamsAdapter);
    }


}
