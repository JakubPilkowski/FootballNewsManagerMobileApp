package com.example.footballnewsmanager.fragments.main.news;

import android.content.Intent;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.adapters.news.NewsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.NewsPlaceholder;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserNews;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsFragmentViewModel extends BaseViewModel implements RecyclerViewItemsListener<UserNews> {
    // TODO: Implement the ViewModel

    public ObservableField<NewsAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableField<SwipeRefreshLayout.OnRefreshListener> swipeRefreshListenerObservable = new ObservableField<>();
    public ObservableInt swipeRefreshColor = new ObservableInt(R.color.colorPrimary);
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);


    private boolean isLastPage;
    private int currentPage = 0;
    private NewsAdapter newsAdapter;
    private RecyclerView recyclerView;
    private BadgeListener badgeListener;
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;


    public void init(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
        tryAgainListener.set(listener);
        swipeRefreshListenerObservable.set(this::updateNews);
        initPlaceholder();
        recyclerView = ((NewsFragmentBinding) getBinding()).newsRecyclerView;
        load();
    }

    public void load() {
        errorVisibility.set(false);
        placeholderVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().news(callback, token, currentPage);
    }

    private void initPlaceholder() {
        NewsPlaceholder newsPlaceholder = ((NewsFragmentBinding) getBinding()).newsPlaceholderView;
        newsPlaceholder.setOnAddTeamsInterface(() -> {
            //przejście do dodawania drużyn
            Intent intent = new Intent(getActivity(), ManageTeamsActivity.class);
            getActivity().startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS);
        });
    }


    private void initItemsView(NewsResponse newsResponse) {
        newsAdapter = new NewsAdapter(getActivity());
        newsAdapter.setRecyclerViewItemsListener(this);
        newsAdapter.setBadgeListener(badgeListener);
        newsAdapter.setItems(newsResponse.getUserNews());
        newsAdapter.setCountAll(newsResponse.getNewsCount());
        newsAdapter.setCountToday(newsResponse.getNewsToday());
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
                return newsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapterObservable.set(newsAdapter);
    }


    private void paginationLoad() {
        newsAdapter.setLoading(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().news(paginationCallback, token, currentPage);
    }

    private Callback<NewsResponse> refreshCallback = new Callback<NewsResponse>() {
        @Override
        public void onSuccess(NewsResponse newsResponse) {
            placeholderVisibility.set(false);
            getActivity().runOnUiThread(() -> {
                currentPage = 0;
                isLastPage = newsResponse.getPages() <= currentPage;
                newsAdapter.setLoading(false);
                newsAdapter.setCountAll(newsResponse.getNewsCount());
                newsAdapter.setCountToday(newsResponse.getNewsToday());
                newsAdapter.refresh(newsResponse);
                newsAdapter.setPlaceholder(false);
                badgeListener.onBadgeChange();
                ((NewsFragmentBinding) getBinding()).newsSwipeRefresh
                        .setRefreshing(false);
                ProgressDialog.get().dismiss();
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            placeholderVisibility.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                status.set(error.getStatus());
                itemsVisibility.set(false);
                errorVisibility.set(true);
            }
            currentPage = 0;
            isLastPage = true;
            newsAdapter.setLoading(false);
            ((NewsFragmentBinding) getBinding()).newsSwipeRefresh
                    .setRefreshing(false);
            postRunnable.set(placeHolderAttachRunnable);

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super NewsResponse> observer) {

        }
    };


    private Callback<NewsResponse> paginationCallback = new Callback<NewsResponse>() {
        @Override
        public void onSuccess(NewsResponse newsResponse) {
            getActivity().runOnUiThread(() -> {
                newsAdapter.setItems(newsResponse.getUserNews());
                isLastPage = newsResponse.getPages() <= currentPage;
                newsAdapter.setLoading(false);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                getActivity().runOnUiThread(() -> {
                    isLastPage = true;
                    newsAdapter.setLoading(false);
                    BottomNavigationView bottomNavigationView = ((MainActivity)getActivity()).getMainFragment()
                            .binding.mainBottomNavView;
                    SnackbarHelper.getSnackBarFromStatus(recyclerView, error.getStatus())
                            .setAction(R.string.reload, v -> paginationLoad())
                            .setAnchorView(bottomNavigationView)
                            .show();
                });
            } else {
                if (error instanceof SingleMessageError) {
                    String message = ((SingleMessageError) error).getMessage();
                    if (message.equals("Nie ma już więcej wyników")) {
//                        postRunnable.set(placeHolderAttachRunnable);
                        getActivity().runOnUiThread(()->{
                            isLastPage = true;
                            newsAdapter.setLoading(false);
                            newsAdapter.setPlaceholder(true);
                            recyclerView.smoothScrollToPosition(newsAdapter.getItemCount() - 1);
                        });
                    }
                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super NewsResponse> observer) {

        }
    };


    private Callback<NewsResponse> callback = new Callback<NewsResponse>() {
        @Override
        public void onSuccess(NewsResponse newsResponse) {
            if (loadingVisibility.get()) {
                loadingVisibility.set(false);
                placeholderVisibility.set(false);
                itemsVisibility.set(true);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> initItemsView(newsResponse));
                }
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            itemsVisibility.set(false);
            placeholderVisibility.set(false);

            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                status.set(error.getStatus());
                errorVisibility.set(true);
            } else {
                if (error instanceof SingleMessageError) {
                    String message = ((SingleMessageError) error).getMessage();
                    if (message.equals("Brak wyników")) {
                        itemsVisibility.set(false);
                        loadingVisibility.set(false);
                        errorVisibility.set(false);
                        placeholderVisibility.set(true);
                    }
                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super NewsResponse> observer) {

        }
    };

    @Override
    public void onDetached() {

    }

    @Override
    public void backToFront() {
        postRunnable.set(backToFrontRunnable);
    }

    @Override
    public void onChangeItem(UserNews oldNews, UserNews newNews) {
        newsAdapter.onChange(oldNews, newNews);
    }

    public void updateNews() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().news(refreshCallback, token, 0);
    }

    @Override
    public void onChangeItems() {
        updateNews();
    }

    private Runnable placeHolderAttachRunnable = () -> {
        isLastPage = true;
        newsAdapter.setLoading(false);
        newsAdapter.setPlaceholder(true);
        recyclerView.smoothScrollToPosition(newsAdapter.getItemCount() - 1);
    };

    private Runnable backToFrontRunnable = () -> recyclerView.scrollToPosition(0);

}
