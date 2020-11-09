package com.example.footballnewsmanager.fragments.main.all_news;

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
import com.example.footballnewsmanager.databinding.AllNewsFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class AllNewsFragment extends BaseFragment<AllNewsFragmentBinding,AllNewsFragmentViewModel> implements Providers {


    public static AllNewsFragment newInstance() {
        return new AllNewsFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.all_news_fragment;
    }

    @Override
    public Class<AllNewsFragmentViewModel> getViewModelClass() {
        return AllNewsFragmentViewModel.class;
    }

    @Override
    public void bindData(AllNewsFragmentBinding binding) {
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