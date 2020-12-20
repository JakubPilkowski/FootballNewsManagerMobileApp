package com.example.footballnewsmanager.fragments.proposed_settings.teams;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.register.ProposedSettingsActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ProposedTeamsFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.Providers;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProposedTeamsFragment extends BaseFragment<ProposedTeamsFragmentBinding, ProposedTeamsViewModel> implements Providers {



    public static ProposedTeamsFragment newInstance() {
        return new ProposedTeamsFragment();
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
        viewModel.init();
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
