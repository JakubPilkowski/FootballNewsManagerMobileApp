package com.example.footballnewsmanager.fragments.manage_teams.main.all;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.manage_teams.all.ManageLeaguesAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.manage_teams.LeagueResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageAllTeamsViewModel extends BaseViewModel {

    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();

    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    public void init(RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener){
        Log.d("ManageAll", "init: ");
        this.recyclerViewItemsListener = recyclerViewItemsListener;
        String token = UserPreferences.get().getAuthToken();
        ProgressDialog.get().show();
        Connection.get().getLeagues(callback, token);
    }

    private Callback<LeagueResponse> callback = new Callback<LeagueResponse>() {
        @Override
        public void onSuccess(LeagueResponse leagueResponse) {
            getActivity().runOnUiThread(()->{
                ManageLeaguesAdapter manageLeaguesAdapter = new ManageLeaguesAdapter(getNavigator());
                adapterObservable.set(manageLeaguesAdapter);
                manageLeaguesAdapter.setRecyclerViewItemsListener(recyclerViewItemsListener);
                manageLeaguesAdapter.setItems(leagueResponse.getLeagues());
                ProgressDialog.get().dismiss();
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d("ManageTeams", "allTeams onSmthWrong: ");
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super LeagueResponse> observer) {

        }
    };

}
