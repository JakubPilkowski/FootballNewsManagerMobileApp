package com.example.footballnewsmanager.fragments.manage_teams.main;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ManageTeamsMainFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ManageTeamsMainFragment extends BaseFragment<ManageTeamsMainFragmentBinding, ManageTeamsMainViewModel> implements Providers {

    public static final String TAG = "ManageTeamsMainFragment";

    public static ManageTeamsMainFragment newInstance() {
        return new ManageTeamsMainFragment();
    }


    @Override
    public String getToolbarName() {
        return getString(R.string.teams_management);
    }

    @Override
    public int getHomeIcon() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.manage_teams_main_fragment;
    }

    @Override
    public Class<ManageTeamsMainViewModel> getViewModelClass() {
        return ManageTeamsMainViewModel.class;
    }


    @Override
    public void bindData(ManageTeamsMainFragmentBinding binding) {
        ((ManageTeamsActivity)getActivity()).refreshToolbar();
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
        return ((ManageTeamsActivity)getActivity()).navigator;
    }
}
