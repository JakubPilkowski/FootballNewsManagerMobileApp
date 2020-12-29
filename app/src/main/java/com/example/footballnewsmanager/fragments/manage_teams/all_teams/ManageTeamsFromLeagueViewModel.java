package com.example.footballnewsmanager.fragments.manage_teams.all_teams;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.manage_teams.teams.ManageTeamsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.User;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageTeamsFromLeagueViewModel extends BaseViewModel {


    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();

    private ManageTeamsAdapter manageTeamsAdapter;
    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;
    private Long id;

    public void init(Long id, RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        this.id = id;
        this.recyclerViewItemsListener = recyclerViewItemsListener;
        load();
    }

    public void load(){
        String token = UserPreferences.get().getAuthToken();
//        ProgressDialog.get().show();
        Connection.get().getTeamsFromLeague(callback, token, id, 0);
    }


    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                if(manageTeamsAdapter == null){
                    manageTeamsAdapter = new ManageTeamsAdapter(getActivity());
                    manageTeamsAdapter.setRecyclerViewItemsListener(recyclerViewItemsListener);
                    adapterObservable.set(manageTeamsAdapter);
                }
                manageTeamsAdapter.setItems(teamsResponse.getTeams());
//                ProgressDialog.get().dismiss();
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d("ManageTeams", "onSmthWrong: ");
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };
}
