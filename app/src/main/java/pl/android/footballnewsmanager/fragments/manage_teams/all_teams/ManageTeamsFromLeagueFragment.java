package pl.android.footballnewsmanager.fragments.manage_teams.all_teams;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ManageTeamsFromLeagueFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserTeam;

public class ManageTeamsFromLeagueFragment extends BaseFragment<ManageTeamsFromLeagueFragmentBinding, ManageTeamsFromLeagueViewModel> implements Providers {


    public static final String TAG = "ManageTeamsFromLeagueFragment";

    private String toolbarName = "";
    private Long leagueId = 0L;
    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    public void setExtendedRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
    }

    private void setToolbarName(String toolbar) {
        this.toolbarName = toolbar;
    }

    public static ManageTeamsFromLeagueFragment newInstance(String title, Long id, RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        ManageTeamsFromLeagueFragment fragment = new ManageTeamsFromLeagueFragment();
        fragment.setToolbarName(title);
        fragment.setLeagueId(id);
        fragment.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
        return fragment;
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

    @Override
    public int getLayoutRes() {
        return R.layout.manage_teams_from_league_fragment;
    }

    @Override
    public Class<ManageTeamsFromLeagueViewModel> getViewModelClass() {
        return ManageTeamsFromLeagueViewModel.class;
    }

    @Override
    public void bindData(ManageTeamsFromLeagueFragmentBinding binding) {
        ((ManageTeamsActivity)getActivity()).refreshToolbar();
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(leagueId, extendedRecyclerViewItemsListener);
    }

    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public String getToolbarName() {
        return toolbarName;
    }

    @Override
    public int getHomeIcon() {
        return R.drawable.ic_arrow_back;
    }

    private void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }
}
