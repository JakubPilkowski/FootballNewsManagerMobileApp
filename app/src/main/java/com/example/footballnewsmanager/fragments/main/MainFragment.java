package com.example.footballnewsmanager.fragments.main;

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
import com.example.footballnewsmanager.databinding.MainFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class MainFragment extends BaseFragment<MainFragmentBinding, MainFragmentViewModel> implements Providers {


    public static final String TAG = "MainFragment";

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.main_fragment;
    }

    @Override
    public Class<MainFragmentViewModel> getViewModelClass() {
        return MainFragmentViewModel.class;
    }

    @Override
    public void bindData(MainFragmentBinding binding) {
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
        return ((MainActivity)getActivity()).getNavigator();
    }
}
