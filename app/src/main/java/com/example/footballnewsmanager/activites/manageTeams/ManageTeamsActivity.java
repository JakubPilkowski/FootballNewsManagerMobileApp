package com.example.footballnewsmanager.activites.manageTeams;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityManageTeamsBinding;
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.fragments.manage_teams.all_teams.ManageTeamsFromLeagueFragment;
import com.example.footballnewsmanager.fragments.manage_teams.all_teams.ManageTeamsFromLeagueViewModel;
import com.example.footballnewsmanager.fragments.manage_teams.main.ManageTeamsMainFragment;
import com.example.footballnewsmanager.fragments.manage_teams.main.popular.ManagePopularTeamsFragment;
import com.example.footballnewsmanager.fragments.manage_teams.main.popular.ManagePopularTeamsViewModel;
import com.example.footballnewsmanager.fragments.manage_teams.main.selected.ManageSelectedTeamsFragment;
import com.example.footballnewsmanager.fragments.manage_teams.main.selected.ManageSelectedTeamsViewModel;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ManageTeamsActivity extends BaseActivity<ActivityManageTeamsBinding, ManageTeamsViewModel> implements Providers {

    public boolean reload = false;

    @Override
    protected void initActivity(ActivityManageTeamsBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        navigator.attach(ManageTeamsMainFragment.newInstance(), ManageTeamsMainFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        Log.d("ManageTeams", "onBackPressed: "+reload);
        if(getSupportFragmentManager().getBackStackEntryCount()<=1 && reload){
            Log.d("ManageTeams", "onBackPressed: ");
            setResult(Activity.RESULT_OK);
        }
        super.onBackPressed();
        refreshToolbar();
    }

    public void refreshToolbar() {
        viewModel.refreshToolbar();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected Class<ManageTeamsViewModel> getViewModel() {
        return ManageTeamsViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return R.id.manage_teams_container;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_manage_teams;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.d("ManageTeams", "onActivityResult: "+requestCode + " result"+resultCode);
        if(requestCode == MainActivity.RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS && resultCode == RESULT_OK){
            reload = true;
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                Log.d("ManageTeams", "onActivityResult: "+fragment.getTag());
                    if (fragment instanceof ManageSelectedTeamsFragment) {
                        ManageSelectedTeamsFragment manageSelectedTeamsFragment = (ManageSelectedTeamsFragment) fragment;
                        ManageSelectedTeamsViewModel viewModel = manageSelectedTeamsFragment.viewModel;
                        if (viewModel != null)
                            viewModel.load();
                    } else if (fragment instanceof ManagePopularTeamsFragment) {
                        ManagePopularTeamsFragment managePopularTeamsFragment = (ManagePopularTeamsFragment) fragment;
                        ManagePopularTeamsViewModel viewModel =  managePopularTeamsFragment.viewModel;
                        if (viewModel != null)
                            viewModel.load();
                    } else if (fragment instanceof ManageTeamsFromLeagueFragment) {
                        ManageTeamsFromLeagueFragment teamsFromLeagueFragment = (ManageTeamsFromLeagueFragment) fragment;
                        ManageTeamsFromLeagueViewModel viewModel = teamsFromLeagueFragment.viewModel;
                        if (viewModel != null)
                            viewModel.load();
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
