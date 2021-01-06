package com.example.footballnewsmanager.fragments.manage_teams.main.all;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ManageAllTeamsFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

public class ManageAllTeamsFragment extends BaseFragment<ManageAllTeamsFragmentBinding, ManageAllTeamsViewModel> implements Providers {


    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    public void setExtendedRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
    }

    public static ManageAllTeamsFragment newInstance(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        ManageAllTeamsFragment manageAllTeamsFragment = new ManageAllTeamsFragment();
        manageAllTeamsFragment.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
        return manageAllTeamsFragment;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.manage_all_teams_fragment;
    }

    @Override
    public Class<ManageAllTeamsViewModel> getViewModelClass() {
        return ManageAllTeamsViewModel.class;
    }

    @Override
    public void bindData(ManageAllTeamsFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(extendedRecyclerViewItemsListener);
    }

    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public String getToolbarName() {
        return "";
    }

    @Override
    public int getHomeIcon() {
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
        return ((ManageTeamsActivity) getActivity()).navigator;
    }
}
