package com.example.footballnewsmanager.adapters.manage_teams;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.fragments.manage_teams.main.all.ManageAllTeamsFragment;
import com.example.footballnewsmanager.fragments.manage_teams.main.popular.ManagePopularTeamsFragment;
import com.example.footballnewsmanager.fragments.manage_teams.main.selected.ManageSelectedTeamsFragment;

import java.util.ArrayList;
import java.util.List;

public class ManageTeamsViewPagerAdapter extends FragmentStateAdapter {



    List<BaseFragment> fragments = new ArrayList<>();

    public ManageTeamsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<BaseFragment> fragments) {
        super(fragmentActivity);
        this.fragments.addAll(fragments);
    }
//
//    public void setFragments(List<BaseFragment> fragments) {
//        this.fragments.addAll(fragments);
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return fragments.get(1);
            case 2:
                return fragments.get(2);
            case 0:
            default:
                return fragments.get(0);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}