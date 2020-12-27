package com.example.footballnewsmanager.activites.manageTeams;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityManageTeamsBinding;
import com.google.android.material.tabs.TabLayout;

public class ManageTeamsViewModel extends BaseViewModel {


    public ObservableField<String> title = new ObservableField<>();

    public void refreshToolbar() {
        BaseFragment fragment = ((ManageTeamsActivity) getActivity()).getCurrentFragment();
        ManageTeamsActivity activity = ((ManageTeamsActivity) getActivity());

        ActivityManageTeamsBinding binding = (ActivityManageTeamsBinding) getBinding();
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(fragment.getHomeIcon() != 0){
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(fragment.getHomeIcon());
        }
        else{
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        title.set(fragment.getToolbarName());
    }

}
