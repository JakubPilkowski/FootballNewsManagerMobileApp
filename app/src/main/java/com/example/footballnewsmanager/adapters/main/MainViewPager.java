package com.example.footballnewsmanager.adapters.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footballnewsmanager.base.BaseFragment;

import java.util.List;

public class MainViewPager extends FragmentStateAdapter {

    private List<BaseFragment> fragments;

    public MainViewPager(@NonNull FragmentActivity fragmentActivity, List<BaseFragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return fragments.get(1);
            case 2:
                return fragments.get(2);
            case 3:
                return fragments.get(3);
            case 0:
            default:
                return fragments.get(0);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
