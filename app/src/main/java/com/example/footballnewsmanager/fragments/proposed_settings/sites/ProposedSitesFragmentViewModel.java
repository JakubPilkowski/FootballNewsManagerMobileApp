package com.example.footballnewsmanager.fragments.proposed_settings.sites;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapter;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.models.LayoutManager;
import com.example.footballnewsmanager.models.Site;

import java.util.List;
import java.util.Observable;

public class ProposedSitesFragmentViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    public ObservableField<LayoutManager> layoutManager = new ObservableField<>(LayoutManager.LINEAR);
    public ObservableField<ProposedSitesAdapter> recyclerViewAdapter = new ObservableField<>();


    public void init(List<Site> sites){

        ProposedSitesAdapter proposedSitesAdapter = new ProposedSitesAdapter();
        proposedSitesAdapter.setItems(sites);
        recyclerViewAdapter.set(proposedSitesAdapter);

    }
}
