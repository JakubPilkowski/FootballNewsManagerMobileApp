package com.example.footballnewsmanager.fragments.main.profile;

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
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ProfileFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ProfileFragment extends BaseFragment<ProfileFragmentBinding, ProfileFragmentViewModel> implements Providers {


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.profile_fragment;
    }

    @Override
    public Class<ProfileFragmentViewModel> getViewModelClass() {
        return ProfileFragmentViewModel.class;
    }

    @Override
    public void bindData(ProfileFragmentBinding binding) {
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
        return ((MainActivity)getActivity()).navigator;
    }
}
