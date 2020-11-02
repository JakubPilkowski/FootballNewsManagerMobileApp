package com.example.footballnewsmanager.fragments.proposed_settings.teams;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.register.ProposedSettingsActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ProposedTeamsFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;
import com.example.footballnewsmanager.models.Team;

import java.util.List;

public class ProposedTeamsFragment extends BaseFragment<ProposedTeamsFragmentBinding, ProposedTeamsViewModel> implements Providers {


    private List<Team> teams;


    public static ProposedTeamsFragment newInstance(List<Team> teams) {
        ProposedTeamsFragment proposedTeamsFragment = new ProposedTeamsFragment();
        proposedTeamsFragment.setTeams(teams);
        return proposedTeamsFragment;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.proposed_teams_fragment;
    }

    @Override
    public Class<ProposedTeamsViewModel> getViewModelClass() {
        return ProposedTeamsViewModel.class;
    }

    @Override
    public void bindData(ProposedTeamsFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(teams);
    }

    @Override
    public int getBackPressType() {
        return 0;
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
        return ((ProposedSettingsActivity) getActivity()).getNavigator();
    }
}
