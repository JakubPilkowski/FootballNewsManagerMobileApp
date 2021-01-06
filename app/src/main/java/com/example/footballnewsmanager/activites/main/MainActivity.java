package com.example.footballnewsmanager.activites.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityMainBinding;
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.fragments.main.all_news.AllNewsFragment;
import com.example.footballnewsmanager.fragments.main.news.NewsFragment;
import com.example.footballnewsmanager.fragments.main.news_info.NewsInfoFragment;
import com.example.footballnewsmanager.fragments.main.profile.ProfileFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.Providers;
import com.example.footballnewsmanager.models.NewsView;
import com.example.footballnewsmanager.models.UserNews;

import java.util.Locale;

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
            boolean proposed = UserPreferences.get().getProposed();
            if(proposed)
                reloadAllNews();
        }
        if (requestCode == RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS && resultCode == RESULT_OK) {
            reloadMainPage();
            reloadNewsInfoPage();
            boolean proposed = UserPreferences.get().getProposed();
            if(proposed)
                reloadAllNews();
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

    public MainFragment getMainFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof MainFragment) {
                return (MainFragment) fragment;
            }
        }
        return MainFragment.newInstance();
    }

    public void reloadMainPage() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof NewsFragment) {
                NewsFragment newsFragment = (NewsFragment) fragment;
                if (newsFragment.viewModel != null) {
                    newsFragment.viewModel.load();
                }
            }
            if (fragment instanceof ProfileFragment) {
                ProfileFragment profileFragment = (ProfileFragment) fragment;
                if (profileFragment.viewModel != null)
                    profileFragment.viewModel.load();
            }
        }
    }

    public void reloadAllNews() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof AllNewsFragment) {
                AllNewsFragment allNewsFragment = (AllNewsFragment) fragment;
                if (allNewsFragment.viewModel != null)
                    allNewsFragment.viewModel.load();
            }
        }
    }

    public void changeLanguage(Locale locale) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    public void reloadProfile() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof ProfileFragment) {
                ProfileFragment allNewsFragment = (ProfileFragment) fragment;
                if (allNewsFragment.viewModel != null)
                    allNewsFragment.viewModel.load();
            }
        }
    }

    public void updateNews(UserNews oldUserNews, UserNews newUserNews, NewsView newsView){
        switch (newsView){
            case SELECTED:
                for (Fragment fragment: getSupportFragmentManager().getFragments()) {
                    if(fragment instanceof NewsFragment){
                        NewsFragment newsFragment = (NewsFragment) fragment;
                        if(newsFragment.viewModel != null)
                            if (newsFragment.viewModel.newsAdapter != null)
                                newsFragment.viewModel.newsAdapter.changeIfExists(oldUserNews, newUserNews);
                    }
                }
            case ALL:
                for (Fragment fragment: getSupportFragmentManager().getFragments()) {
                    if(fragment instanceof AllNewsFragment){
                        AllNewsFragment allNewsFragment = (AllNewsFragment) fragment;
                        if(allNewsFragment.viewModel != null)
                            if (allNewsFragment.viewModel.allNewsAdapter != null)
                                allNewsFragment.viewModel.allNewsAdapter.changeIfExists(oldUserNews, newUserNews);
                    }
                }
        }
    }
}
