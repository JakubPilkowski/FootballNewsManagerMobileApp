package com.example.footballnewsmanager.fragments.manage_teams.all_teams;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ManageTeamsFromLeagueFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

public class ManageTeamsFromLeagueFragment extends BaseFragment<ManageTeamsFromLeagueFragmentBinding, ManageTeamsFromLeagueViewModel> implements Providers {


    public static final String TAG = "ManageTeamsFromLeagueFragment";

    private String toolbarName = "";
    private Long leagueId = 0L;
    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    public void setRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        this.recyclerViewItemsListener = recyclerViewItemsListener;
    }

    public void setToolbarName(String toolbar) {
        this.toolbarName = toolbar;
    }

    public static ManageTeamsFromLeagueFragment newInstance(String title, Long id, RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        ManageTeamsFromLeagueFragment fragment = new ManageTeamsFromLeagueFragment();
        fragment.setToolbarName(title);
        fragment.setLeagueId(id);
        fragment.setRecyclerViewItemsListener(recyclerViewItemsListener);
        return fragment;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public Navigator getNavigator() {
        return ((ManageTeamsActivity)getActivity()).navigator;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.manage_teams_from_league_fragment;
    }

    @Override
    public Class<ManageTeamsFromLeagueViewModel> getViewModelClass() {
        return ManageTeamsFromLeagueViewModel.class;
    }

    @Override
    public void bindData(ManageTeamsFromLeagueFragmentBinding binding) {
        ((ManageTeamsActivity)getActivity()).refreshToolbar();
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(leagueId, recyclerViewItemsListener);
    }

    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public String getToolbarName() {
        return toolbarName;
    }

    @Override
    public int getHomeIcon() {
        return R.drawable.ic_arrow_back;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }
}
