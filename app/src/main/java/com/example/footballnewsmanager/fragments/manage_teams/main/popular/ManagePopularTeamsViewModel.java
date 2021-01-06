package com.example.footballnewsmanager.fragments.manage_teams.main.popular;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.manage_teams.popular.ManagePopularTeamsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ManagePopularTeamsFragmentBinding;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManagePopularTeamsViewModel extends BaseViewModel {
    public ObservableField<RecyclerView.Adapter> recyclerViewAdapter = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    private RecyclerView recyclerView;
    public ManagePopularTeamsAdapter managePopularTeamsAdapter;
    private boolean isLastPage = false;
    private int currentPage = 0;
    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    public void init(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        recyclerView = ((ManagePopularTeamsFragmentBinding) getBinding()).managePopularTeamsRecyclerView;
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        tryAgainListener.set(listener);
        load();
    }

    public void load() {
        currentPage = 0;
        errorVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedTeams(callback, token, currentPage);
    }

    private void initItemsView(TeamsResponse teamsResponse) {
        managePopularTeamsAdapter = new ManagePopularTeamsAdapter(getActivity());
        managePopularTeamsAdapter.setItems(teamsResponse.getTeams(), currentPage);
        managePopularTeamsAdapter.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);

        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                currentPage++;
                paginationLoad();
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

    private void paginationLoad() {
        managePopularTeamsAdapter.setLoading(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedTeams(paginationCallback, token, currentPage);
    }


    private Callback<TeamsResponse> paginationCallback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                managePopularTeamsAdapter.setItems(teamsResponse.getTeams(), currentPage);
                isLastPage = teamsResponse.getPages() <= currentPage;
                managePopularTeamsAdapter.setLoading(false);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            getActivity().runOnUiThread(()-> {
                isLastPage = true;
                managePopularTeamsAdapter.setLoading(false);
            });
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                SnackbarHelper.getInfinitiveSnackBarFromStatus(recyclerView, error.getStatus())
                        .setAction(R.string.reload, v -> paginationLoad())
                        .show();
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };

    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            if (loadingVisibility.get()) {
                loadingVisibility.set(false);
                itemsVisibility.set(true);
                getActivity().runOnUiThread(() -> {
                    initItemsView(teamsResponse);
                });
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                itemsVisibility.set(false);
                errorVisibility.set(true);
                status.set(error.getStatus());
            } else {
                getActivity().runOnUiThread(() -> {
                    isLastPage = true;
                    managePopularTeamsAdapter.setLoading(false);
                });
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };
}
