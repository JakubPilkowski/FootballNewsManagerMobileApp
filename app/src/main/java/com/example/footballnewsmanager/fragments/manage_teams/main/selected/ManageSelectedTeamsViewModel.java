package com.example.footballnewsmanager.fragments.manage_teams.main.selected;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.manage_teams.all.ManageLeaguesAdapter;
import com.example.footballnewsmanager.adapters.manage_teams.selected.SelectedTeamsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.manage_teams.LeagueResponse;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageSelectedTeamsViewModel extends BaseViewModel {

    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public SelectedTeamsAdapter selectedTeamsAdapter;


    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    // TODO: Implement the ViewModel
    public void init(RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        this.recyclerViewItemsListener = recyclerViewItemsListener;
        load();
    }

    public void load() {
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getFavouriteTeams(callback, token);
    }

    public void initTeamsView(TeamsResponse teamsResponse){
        selectedTeamsAdapter = new SelectedTeamsAdapter(getActivity());
        selectedTeamsAdapter.setRecyclerViewItemsListener(recyclerViewItemsListener);
        adapterObservable.set(selectedTeamsAdapter);
        selectedTeamsAdapter.setItems(teamsResponse.getTeams());
    }

    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                if(loadingVisibility.get()){
                    loadingVisibility.set(false);
                    itemsVisibility.set(true);
                    getActivity().runOnUiThread(()->{
                        initTeamsView(teamsResponse);
                    });
                }
                else{
                    getActivity().runOnUiThread(()-> {
                        selectedTeamsAdapter.setItems(teamsResponse.getTeams());
                    });
                }
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            itemsVisibility.set(true);
            selectedTeamsAdapter = new SelectedTeamsAdapter(getActivity());
            selectedTeamsAdapter.setRecyclerViewItemsListener(recyclerViewItemsListener);
            adapterObservable.set(selectedTeamsAdapter);
            Log.d("ManageTeams", "allTeams onSmthWrong: ");
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };
}
