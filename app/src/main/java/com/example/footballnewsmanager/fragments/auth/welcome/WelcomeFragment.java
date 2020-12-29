package com.example.footballnewsmanager.fragments.auth.welcome;

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
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.WelcomeFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class WelcomeFragment extends BaseFragment<WelcomeFragmentBinding, WelcomeViewModel>  implements Providers {


    public static final String TAG = "WelcomeFragmentTag";

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.welcome_fragment;
    }

    @Override
    public Class<WelcomeViewModel> getViewModelClass() {
        return WelcomeViewModel.class;
    }

    @Override
    public void bindData(WelcomeFragmentBinding binding) {
        binding.setViewModel(viewModel);
        viewModel.setProviders(this);
        viewModel.init();
    }

    @Override
    public int getBackPressType() {
        return 1;
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
        return ((AuthActivity) getActivity()).getNavigator();
    }
}
