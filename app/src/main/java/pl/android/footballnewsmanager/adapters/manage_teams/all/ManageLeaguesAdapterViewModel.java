package pl.android.footballnewsmanager.adapters.manage_teams.all;

import androidx.databinding.ObservableField;

import pl.android.footballnewsmanager.base.BaseAdapterViewModel;
import pl.android.footballnewsmanager.fragments.manage_teams.all_teams.ManageTeamsFromLeagueFragment;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.League;
import pl.android.footballnewsmanager.models.UserTeam;

public class ManageLeaguesAdapterViewModel extends BaseAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> img = new ObservableField<>();

    private League league;
    private Navigator navigator;
    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    @Override
    public void init(Object[] values) {
        league = (League) values[0];
        navigator = (Navigator) values[1];
        extendedRecyclerViewItemsListener = (RecyclerViewItemsListener<UserTeam>) values[2];
        name.set(league.getName());
        img.set(league.getLogoUrl());
    }

    public void onClick(){
        navigator.attach(ManageTeamsFromLeagueFragment.newInstance(league.getName(), league.getId(), extendedRecyclerViewItemsListener), ManageTeamsFromLeagueFragment.TAG);
    }
}
