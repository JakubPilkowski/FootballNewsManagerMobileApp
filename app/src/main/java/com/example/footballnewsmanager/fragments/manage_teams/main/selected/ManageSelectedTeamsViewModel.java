package com.example.footballnewsmanager.fragments.manage_teams.main.selected;

import android.util.Log;

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
    public SelectedTeamsAdapter selectedTeamsAdapter;


    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    // TODO: Implement the ViewModel
    public void init(RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        this.recyclerViewItemsListener = recyclerViewItemsListener;
        String token = UserPreferences.get().getAuthToken();
        ProgressDialog.get().show();
        Connection.get().getFavouriteTeams(callback, token);
    }

    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                if (selectedTeamsAdapter == null) {
                    selectedTeamsAdapter = new SelectedTeamsAdapter(getActivity());
                    selectedTeamsAdapter.setRecyclerViewItemsListener(recyclerViewItemsListener);
                    adapterObservable.set(selectedTeamsAdapter);
                }
                selectedTeamsAdapter.setItems(teamsResponse.getTeams());
                ProgressDialog.get().dismiss();
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            ProgressDialog.get().dismiss();
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
