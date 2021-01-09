package pl.android.footballnewsmanager.fragments.manage_teams.main;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ManageTeamsMainFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class ManageTeamsMainFragment extends BaseFragment<ManageTeamsMainFragmentBinding, ManageTeamsMainViewModelExtended> implements Providers {

    public static final String TAG = "ManageTeamsMainFragment";

    public static ManageTeamsMainFragment newInstance() {
        return new ManageTeamsMainFragment();
    }


    @Override
    public String getToolbarName() {
        return getString(R.string.teams_management);
    }

    @Override
    public int getHomeIcon() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.manage_teams_main_fragment;
    }

    @Override
    public Class<ManageTeamsMainViewModelExtended> getViewModelClass() {
        return ManageTeamsMainViewModelExtended.class;
    }


    @Override
    public void bindData(ManageTeamsMainFragmentBinding binding) {
        ((ManageTeamsActivity)getActivity()).refreshToolbar();
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init();
    }

    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public Navigator getNavigator() {
        return ((ManageTeamsActivity)getActivity()).navigator;
    }
}
