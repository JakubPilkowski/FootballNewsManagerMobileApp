package com.example.footballnewsmanager.fragments.manage_teams.main.popular;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.manage_teams.popular.ManagePopularTeamsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ManagePopularTeamsFragmentBinding;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManagePopularTeamsViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    public ObservableField<RecyclerView.Adapter> recyclerViewAdapter = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);


    private RecyclerView recyclerView;
    public ManagePopularTeamsAdapter managePopularTeamsAdapter;
    private boolean isLastPage = false;
    private int currentPage = 0;
    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    public void init(RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        recyclerView = ((ManagePopularTeamsFragmentBinding) getBinding()).managePopularTeamsRecyclerView;
        this.recyclerViewItemsListener = recyclerViewItemsListener;
        load();
    }

    public void load() {
        currentPage = 0;
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedTeams(callback, token, currentPage);
    }

    private void initItemsView(ProposedTeamsResponse proposedTeamsResponse) {
        managePopularTeamsAdapter = new ManagePopularTeamsAdapter(getActivity());
        managePopularTeamsAdapter.setItems(proposedTeamsResponse.getTeams(), currentPage);
        managePopularTeamsAdapter.setRecyclerViewItemsListener(recyclerViewItemsListener);

        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                Log.d("News", "loadMoreItems");
                currentPage++;
                managePopularTeamsAdapter.setLoading(true);
                String token = UserPreferences.get().getAuthToken();
                Connection.get().proposedTeams(callback, token, currentPage);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return managePopularTeamsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        recyclerViewAdapter.set(managePopularTeamsAdapter);
    }


    private Callback<ProposedTeamsResponse> callback = new Callback<ProposedTeamsResponse>() {
        @Override
        public void onSuccess(ProposedTeamsResponse proposedTeamsResponse) {
            if (loadingVisibility.get()) {
                loadingVisibility.set(false);
                itemsVisibility.set(true);
                getActivity().runOnUiThread(() -> {
                    Log.d("News", "onSuccessFirst");
                    initItemsView(proposedTeamsResponse);
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    managePopularTeamsAdapter.setItems(proposedTeamsResponse.getTeams(), currentPage);
                    isLastPage = proposedTeamsResponse.getPages() <= currentPage;
                    managePopularTeamsAdapter.setLoading(false);
                });
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            getActivity().runOnUiThread(() -> {
                isLastPage = true;
                managePopularTeamsAdapter.setLoading(false);
            });
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super ProposedTeamsResponse> observer) {

        }
    };
}
