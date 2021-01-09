package pl.android.footballnewsmanager.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pl.android.footballnewsmanager.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ProposedSettingsViewPagerAdapter extends FragmentStateAdapter {

    private List<BaseFragment> fragments = new ArrayList<>();


    public ProposedSettingsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<BaseFragment> fragmentList) {
        super(fragmentActivity);
        fragments.clear();
        fragments.addAll(fragmentList);
    }

    public List<BaseFragment> getFragments() {
        return fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = fragments.get(0);
                break;
            case 1:
                fragment = fragments.get(1);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
