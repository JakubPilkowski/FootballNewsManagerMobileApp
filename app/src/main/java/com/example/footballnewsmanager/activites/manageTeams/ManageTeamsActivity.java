package com.example.footballnewsmanager.activites.manageTeams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import android.app.Activity;
import android.os.Bundle;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityManageTeamsBinding;
import com.example.footballnewsmanager.fragments.manage_teams.main.ManageTeamsMainFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ManageTeamsActivity extends BaseActivity<ActivityManageTeamsBinding, ManageTeamsViewModel> implements Providers {


    @Override
    protected void initActivity(ActivityManageTeamsBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        navigator.attach(ManageTeamsMainFragment.newInstance(), ManageTeamsMainFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()<=1){
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
}
