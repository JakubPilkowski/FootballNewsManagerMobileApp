package com.example.footballnewsmanager.fragments.manage_teams.all_teams;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.manage_teams.popular.ManagePopularTeamsAdapter;
import com.example.footballnewsmanager.adapters.manage_teams.teams.ManageTeamsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ManagePopularTeamsFragmentBinding;
import com.example.footballnewsmanager.databinding.ManageTeamsFromLeagueFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.User;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageTeamsFromLeagueViewModel extends BaseViewModel {


    public ObservableField<RecyclerView.Adapter> recyclerViewAdapter = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public RecyclerView recyclerView;
    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();

    private ManageTeamsAdapter manageTeamsAdapter;
    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;
    private Long id;
    private boolean isLastPage = false;
    private int currentPage = 0;

    public void init(Long id, RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        recyclerView = ((ManageTeamsFromLeagueFragmentBinding) getBinding()).manageTeamsFromLeagueRecyclerView;
        this.id = id;
        this.recyclerViewItemsListener = recyclerViewItemsListener;
        load();
    }

    public void load(){
        currentPage=0;
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getTeamsFromLeague(callback, token, id, currentPage);
    }


    private void initTeamsView(TeamsResponse teamsResponse){
        manageTeamsAdapter = new ManageTeamsAdapter(getActivity());
        manageTeamsAdapter.setRecyclerViewItemsListener(recyclerViewItemsListener);
        manageTeamsAdapter.setItems(teamsResponse.getTeams());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                Log.d("News", "loadMoreItems");
                if(id != 7)
                    return;
                currentPage++;
                manageTeamsAdapter.setLoading(true);
                String token = UserPreferences.get().getAuthToken();
                Connection.get().getTeamsFromLeague(callback, token,id, currentPage);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return manageTeamsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapterObservable.set(manageTeamsAdapter);
    }


    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                if(loadingVisibility.get()){
                    loadingVisibility.set(false);
                    itemsVisibility.set(true);
                    getActivity().runOnUiThread(()-> initTeamsView(teamsResponse));
                }
                else{
                    getActivity().runOnUiThread(() -> {
                        manageTeamsAdapter.setItems(teamsResponse.getTeams());
                        if(id==7){
                            isLastPage = 7 <= currentPage;
                        }
                        manageTeamsAdapter.setLoading(false);
                    });
                }
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d("ManageTeams", "onSmthWrong: ");
            getActivity().runOnUiThread(() -> {
                isLastPage = true;
                manageTeamsAdapter.setLoading(false);
            });
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };
}
