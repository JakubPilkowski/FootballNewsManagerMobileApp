package com.example.footballnewsmanager.fragments.proposed_settings.teams;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.proposed_teams.ProposedTeamsAdapter;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.models.LayoutManager;
import com.example.footballnewsmanager.models.Team;

import java.util.List;

public class ProposedTeamsViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel

    public ObservableField<LayoutManager> layoutManager = new ObservableField<>(LayoutManager.LINEAR);
    public ObservableField<RecyclerView.Adapter> recyclerViewAdapter = new ObservableField<>();

    public void init(List<Team> teams){

        ProposedTeamsAdapter proposedTeamsAdapter = new ProposedTeamsAdapter();
        proposedTeamsAdapter.setItems(teams);
        recyclerViewAdapter.set(proposedTeamsAdapter);

    }
}
