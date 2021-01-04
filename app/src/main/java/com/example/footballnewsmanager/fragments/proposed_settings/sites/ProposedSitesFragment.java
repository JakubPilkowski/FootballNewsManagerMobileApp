package com.example.footballnewsmanager.fragments.proposed_settings.sites;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.register.ProposedSettingsActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ProposedSitesFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ProposedSitesFragment extends BaseFragment<ProposedSitesFragmentBinding, ProposedSitesFragmentViewModel> implements Providers {

    public static ProposedSitesFragment newInstance() {
        return new ProposedSitesFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.proposed_sites_fragment;
    }

    @Override
    public Class<ProposedSitesFragmentViewModel> getViewModelClass() {
        return ProposedSitesFragmentViewModel.class;
    }

    @Override
    public void bindData(ProposedSitesFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init();
    }



    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public String getToolbarName() {
        return null;
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
        return ((ProposedSettingsActivity)getActivity()).getNavigator();
    }
}
