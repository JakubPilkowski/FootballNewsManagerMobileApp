package com.example.footballnewsmanager.activites.liked_news;

import androidx.databinding.ViewDataBinding;

import android.app.Activity;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityFavouriteNewsBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class LikedNewsActivity extends BaseActivity<ActivityFavouriteNewsBinding, LikedNewsActivityViewModel> implements Providers {

    @Override
    protected void initActivity(ActivityFavouriteNewsBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init();
    }

    @Override
    protected Class<LikedNewsActivityViewModel> getViewModel() {
        return LikedNewsActivityViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_favourite_news;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public BaseFragment getFragment() {
        return null;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }
}
