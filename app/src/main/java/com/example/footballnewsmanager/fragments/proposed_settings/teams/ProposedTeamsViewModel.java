package com.example.footballnewsmanager.fragments.proposed_settings.teams;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.proposed_teams.ProposedTeamsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ProposedTeamsFragmentBinding;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;
import com.example.footballnewsmanager.models.UserNews;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProposedTeamsViewModel extends BaseViewModel implements NewsRecyclerViewListener {
    // TODO: Implement the ViewModel

    public ObservableField<RecyclerView.Adapter> recyclerViewAdapter = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);


    private RecyclerView recyclerView;
    private ProposedTeamsAdapter proposedTeamsAdapter;
    private boolean isLastPage = false;
    private int currentPage = 0;
    public void init(){
        recyclerView = ((ProposedTeamsFragmentBinding)getBinding()).proposedTeamsRecyclerView;
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedTeams(callback, token, currentPage);
    }



    private void initItemsView(ProposedTeamsResponse proposedTeamsResponse) {
        proposedTeamsAdapter = new ProposedTeamsAdapter();
        proposedTeamsAdapter.setItems(proposedTeamsResponse.getTeams());
        proposedTeamsAdapter.setNewsRecyclerViewListener(this);

        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                Log.d("News", "loadMoreItems");
                currentPage++;
                proposedTeamsAdapter.isLoading = true;
                String token = UserPreferences.get().getAuthToken();
                Connection.get().proposedTeams(callback, token, currentPage);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return proposedTeamsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        recyclerViewAdapter.set(proposedTeamsAdapter);
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
                    proposedTeamsAdapter.setItems(proposedTeamsResponse.getTeams());
                    isLastPage = proposedTeamsResponse.getPages() <= currentPage;
                    proposedTeamsAdapter.isLoading = false;
                });
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            isLastPage = true;
            proposedTeamsAdapter.isLoading = false;
            postRunnable.set(placeHolderAttachRunnable);
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super ProposedTeamsResponse> observer) {

        }
    };

    private Runnable placeHolderAttachRunnable = () ->{
        proposedTeamsAdapter.setPlaceholder(true);
        recyclerView.smoothScrollToPosition(proposedTeamsAdapter.getItemCount() - 1);
    };

    private Runnable placeHolderDetachRunnable = () -> {
        isLastPage = false;
        proposedTeamsAdapter.setPlaceholder(false);
    };

    @Override
    public void onDetached() {
        postRunnable.set(placeHolderDetachRunnable);
    }

    @Override
    public void backToFront() {

    }

    @Override
    public void onChangeItem(UserNews oldNews, UserNews newNews) {

    }

    @Override
    public void onChangeItems() {

    }
}
