package com.example.footballnewsmanager.fragments.main.news;

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
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class NewsFragment extends BaseFragment<NewsFragmentBinding, NewsFragmentViewModel> implements Providers {


    public static NewsFragment newInstance() {
        return new NewsFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.news_fragment;
    }

    @Override
    public Class<NewsFragmentViewModel> getViewModelClass() {
        return NewsFragmentViewModel.class;
    }

    @Override
    public void bindData(NewsFragmentBinding binding) {
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
