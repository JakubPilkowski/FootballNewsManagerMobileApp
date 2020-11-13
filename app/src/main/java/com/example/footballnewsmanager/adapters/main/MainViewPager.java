package com.example.footballnewsmanager.adapters.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footballnewsmanager.fragments.main.all_news.AllNewsFragment;
import com.example.footballnewsmanager.fragments.main.news.NewsFragment;
import com.example.footballnewsmanager.fragments.main.profile.ProfileFragment;
import com.example.footballnewsmanager.fragments.main.sites.SitesFragment;

public class MainViewPager extends FragmentStateAdapter {


    public MainViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;
        switch (position){
            case 1:
                fragment = AllNewsFragment.newInstance();
                break;
            case 2:
                fragment = SitesFragment.newInstance();
                break;
            case 3:
                fragment = ProfileFragment.newInstance();
                break;
            case 0:
            default:
                fragment = NewsFragment.newInstance();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
