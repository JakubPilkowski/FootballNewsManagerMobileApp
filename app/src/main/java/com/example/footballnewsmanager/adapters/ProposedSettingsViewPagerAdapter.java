package com.example.footballnewsmanager.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.fragments.proposed_settings.others.ProposedOthersFragment;
import com.example.footballnewsmanager.fragments.proposed_settings.sites.ProposedSitesFragment;
import com.example.footballnewsmanager.fragments.proposed_settings.teams.ProposedTeamsFragment;

public class ProposedSettingsViewPagerAdapter extends FragmentStateAdapter {


    public ProposedSettingsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = ProposedTeamsFragment.newInstance();
                break;
            case 1:
                fragment = ProposedSitesFragment.newInstance();
                break;
            case 2:
                fragment = ProposedOthersFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
