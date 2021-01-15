package pl.android.footballnewsmanager.fragments.manage_teams.main;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.ManageTeamsMainFragmentBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import pl.android.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import pl.android.footballnewsmanager.adapters.manage_teams.ManageTeamsViewPagerAdapter;
import pl.android.footballnewsmanager.base.BaseFragment;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.fragments.manage_teams.main.all.ManageAllTeamsFragment;
import pl.android.footballnewsmanager.fragments.manage_teams.main.popular.ManagePopularTeamsFragment;
import pl.android.footballnewsmanager.fragments.manage_teams.main.popular.ManagePopularTeamsViewModel;
import pl.android.footballnewsmanager.fragments.manage_teams.main.selected.ManageSelectedTeamsFragment;
import pl.android.footballnewsmanager.fragments.manage_teams.main.selected.ManageSelectedTeamsViewModel;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserTeam;

public class ManageTeamsMainViewModelExtended extends BaseViewModel implements RecyclerViewItemsListener<UserTeam> {
    private ViewPager2 viewPager2;
    public ObservableBoolean userInputEnabled = new ObservableBoolean(false);

    private List<BaseFragment> fragments = new ArrayList<>();

    public void init() {
        TabLayout tabLayout = ((ManageTeamsMainFragmentBinding) getBinding()).manageTeamsTabLayout;
        viewPager2 = ((ManageTeamsMainFragmentBinding) getBinding()).manageTeamsViewPager;

        fragments.add(ManageSelectedTeamsFragment.newInstance(this));
        fragments.add(ManageAllTeamsFragment.newInstance(this));
        fragments.add(ManagePopularTeamsFragment.newInstance(this));

        ManageTeamsViewPagerAdapter manageTeamsViewPagerAdapter = new ManageTeamsViewPagerAdapter((FragmentActivity) getActivity(), fragments);
        viewPager2.setAdapter(manageTeamsViewPagerAdapter);

        Resources resources = getActivity().getResources();

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, (tab, position) -> {
            TextView customTab = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_item, null, false);
            if (position == 0) {
                customTab.setText(resources.getString(R.string.added_de));
            } else if (position == 1) {
                customTab.setText(resources.getString(R.string.all));
            } else {
                customTab.setText(resources.getString(R.string.popular));
            }
            tab.setCustomView(customTab);
        });
        tabLayoutMediator.attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onChangeItem(UserTeam oldItem, UserTeam newItem) {
        getActivity().runOnUiThread(() -> {
            ((ManageTeamsActivity)getActivity()).reload = true;
            for (BaseFragment fragment : fragments) {
                if (fragment instanceof ManageSelectedTeamsFragment) {
                    ManageSelectedTeamsViewModel viewModel = (ManageSelectedTeamsViewModel) fragment.viewModel;
                    if (viewModel != null)
                    {
                        viewModel.placeholderVisibility.set(false);
                        viewModel.selectedTeamsAdapter.onChange(oldItem, newItem);
                        if(viewModel.selectedTeamsAdapter.getItems().size()==0){
                            viewModel.placeholderVisibility.set(true);
                        }
                    }
                } else if (fragment instanceof ManagePopularTeamsFragment) {
                    ManagePopularTeamsViewModel viewModel = (ManagePopularTeamsViewModel) fragment.viewModel;
                    if (viewModel != null)
                        viewModel.managePopularTeamsAdapter.onChange(oldItem, newItem);
                }
            }
        });
    }

}
