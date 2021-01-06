package com.example.footballnewsmanager.fragments.manage_teams.main.popular;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ManagePopularTeamsFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

public class ManagePopularTeamsFragment extends BaseFragment<ManagePopularTeamsFragmentBinding, ManagePopularTeamsViewModel> implements Providers {


    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    public void setExtendedRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
    }

    public static ManagePopularTeamsFragment newInstance(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        ManagePopularTeamsFragment managePopularTeamsFragment = new ManagePopularTeamsFragment();
        managePopularTeamsFragment.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
        return managePopularTeamsFragment;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.manage_popular_teams_fragment;
    }

    @Override
    public Class<ManagePopularTeamsViewModel> getViewModelClass() {
        return ManagePopularTeamsViewModel.class;
    }

    @Override
    public void bindData(ManagePopularTeamsFragmentBinding binding) {
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
        return ((ManageTeamsActivity)getActivity()).navigator;
    }
}
