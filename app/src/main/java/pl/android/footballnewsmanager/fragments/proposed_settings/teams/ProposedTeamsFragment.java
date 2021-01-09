package pl.android.footballnewsmanager.fragments.proposed_settings.teams;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.register.ProposedSettingsActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ProposedTeamsFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class ProposedTeamsFragment extends BaseFragment<ProposedTeamsFragmentBinding, ProposedTeamsViewItemsModelExtended> implements Providers {



    public static ProposedTeamsFragment newInstance() {
        return new ProposedTeamsFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.proposed_teams_fragment;
    }

    @Override
    public Class<ProposedTeamsViewItemsModelExtended> getViewModelClass() {
        return ProposedTeamsViewItemsModelExtended.class;
    }

    @Override
    public void bindData(ProposedTeamsFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init();
    }

    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public String getToolbarName() {
        return null;
    }

    @Override
    public int getHomeIcon() {
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
        return ((ProposedSettingsActivity) getActivity()).getNavigator();
    }
}
