package com.example.footballnewsmanager.fragments.proposed_settings.others;

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
import com.example.footballnewsmanager.databinding.ProposedOthersFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ProposedOthersFragment extends BaseFragment<ProposedOthersFragmentBinding, ProposedOthersViewModel> implements Providers {


    public static ProposedOthersFragment newInstance() {
        return new ProposedOthersFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.proposed_others_fragment;
    }

    @Override
    public Class<ProposedOthersViewModel> getViewModelClass() {
        return ProposedOthersViewModel.class;
    }

    @Override
    public void bindData(ProposedOthersFragmentBinding binding) {
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
        return ((ProposedSettingsActivity)getActivity()).getNavigator();
    }
}
