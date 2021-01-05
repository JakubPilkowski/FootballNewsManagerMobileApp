package com.example.footballnewsmanager.fragments.manage_teams.main.selected;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.manage_teams.selected.SelectedTeamsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageSelectedTeamsViewModel extends BaseViewModel {

    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public SelectedTeamsAdapter selectedTeamsAdapter;
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;



    private ExtendedRecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    // TODO: Implement the ViewModel
    public void init(ExtendedRecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        this.tryAgainListener.set(listener);
        load();
    }

    public void load() {
        errorVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getFavouriteTeams(callback, token);
    }

    public void initTeamsView(TeamsResponse teamsResponse){
        selectedTeamsAdapter = new SelectedTeamsAdapter(getActivity());
        selectedTeamsAdapter.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
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
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                itemsVisibility.set(false);
                errorVisibility.set(true);
                status.set(error.getStatus());
            }
            else {
                itemsVisibility.set(true);
                selectedTeamsAdapter = new SelectedTeamsAdapter(getActivity());
                selectedTeamsAdapter.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
                adapterObservable.set(selectedTeamsAdapter);
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };
}
