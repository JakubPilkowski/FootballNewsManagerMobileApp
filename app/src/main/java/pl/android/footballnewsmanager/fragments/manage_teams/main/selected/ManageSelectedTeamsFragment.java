package pl.android.footballnewsmanager.fragments.manage_teams.main.selected;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ManageSelectedTeamsFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserTeam;

public class ManageSelectedTeamsFragment extends BaseFragment<ManageSelectedTeamsFragmentBinding, ManageSelectedTeamsViewModel> implements Providers {


    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    public void setExtendedRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
    }

    public static ManageSelectedTeamsFragment newInstance(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        ManageSelectedTeamsFragment manageSelectedTeamsFragment = new ManageSelectedTeamsFragment();
        manageSelectedTeamsFragment.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
        return manageSelectedTeamsFragment;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.manage_selected_teams_fragment;
    }

    @Override
    public Class<ManageSelectedTeamsViewModel> getViewModelClass() {
        return ManageSelectedTeamsViewModel.class;
    }

    @Override
    public void bindData(ManageSelectedTeamsFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(extendedRecyclerViewItemsListener);
    }

    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public String getToolbarName() {
        return "";
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
        return ((ManageTeamsActivity)getActivity()).navigator;
    }
}
