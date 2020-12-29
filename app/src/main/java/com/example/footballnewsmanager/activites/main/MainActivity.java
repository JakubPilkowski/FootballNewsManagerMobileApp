package com.example.footballnewsmanager.activites.main;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityMainBinding;
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.fragments.main.news.NewsFragment;
import com.example.footballnewsmanager.fragments.main.news_info.NewsInfoFragment;
import com.example.footballnewsmanager.fragments.main.profile.ProfileFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityViewModel> implements Providers {


    public static final int RESULT_MANAGE_TEAMS = 1001;
    public static final int RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS = 1002;

    @Override
    protected void initActivity(ActivityMainBinding binding) {
        binding.setViewModel(viewModel);
        viewModel.setProviders(this);
        viewModel.init();
        navigator.attach(MainFragment.newInstance(), MainFragment.TAG);
    }

    @Override
    protected Class<MainActivityViewModel> getViewModel() {
        return MainActivityViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return R.id.main_container;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    public BaseActivity getActivity() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_MANAGE_TEAMS && resultCode == RESULT_OK) {
            reloadMainPage();
        }
        if (requestCode == RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS && resultCode == RESULT_OK) {
            reloadMainPage();
            reloadNewsInfoPage();
        }
    }

    private void reloadNewsInfoPage() {

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            Log.d("Main", "reloadNewsInfoPage: " + fragment.getTag());
            if (fragment instanceof NewsInfoFragment) {
                NewsInfoFragment newsInfoFragment = (NewsInfoFragment) fragment;
                if (newsInfoFragment.viewModel != null) {
                    newsInfoFragment.viewModel.load();
                }
            }
        }

    }

    public void reloadMainPage() {
        MainFragment fragment = (MainFragment) getSupportFragmentManager().getFragments().get(0);
        NewsFragment newsFragment = (NewsFragment) fragment.viewModel.fragments.get(0);
        ProfileFragment profileFragment = (ProfileFragment) fragment.viewModel.fragments.get(3);
        if (newsFragment.viewModel != null)
            newsFragment.viewModel.load();
        if (profileFragment.viewModel != null)
            profileFragment.viewModel.load();
    }
}
