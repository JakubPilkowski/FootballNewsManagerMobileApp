package com.example.footballnewsmanager.activites.news_for_team;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news.newsForTeam.NewsForTeamAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityNewsForTeamBinding;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsForTeamViewModel extends BaseViewModel {


    public static final String TAG = "NewsForTeam";

    public ObservableField<NewsForTeamAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener onTryAgainListener = this::load;


    private boolean isLastPage;
    private int currentPage = 0;
    private NewsForTeamAdapter newsForTeamAdapter;
    private RecyclerView recyclerView;


    private Long id;
    private String name;
    private String img;
    private boolean isFavourite;
    private RecyclerViewItemsListener<UserTeam> listener;


    public void init(Long id, String name, String img, boolean isFavourite, RecyclerViewItemsListener<UserTeam> listener) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.listener = listener;
        this.isFavourite = isFavourite;
        recyclerView = ((ActivityNewsForTeamBinding) getBinding()).newsForTeamRecyclerView;
        tryAgainListener.set(onTryAgainListener);
        load();
    }

    public void load() {
        errorVisibility.set(false);
        itemsVisibility.set(false);
        placeholderVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().newsForTeam(callback, token, id, currentPage);
    }

    private void initItemsView(NewsResponse newsResponse) {
        newsForTeamAdapter = new NewsForTeamAdapter(getActivity());
        newsForTeamAdapter.setHeaderItems(id, name, img, isFavourite);
        newsForTeamAdapter.setHeaderRecyclerViewItemsListener(listener);
        newsForTeamAdapter.setCountAll(newsResponse.getNewsCount());
        newsForTeamAdapter.setCountToday(newsResponse.getNewsToday());
        newsForTeamAdapter.setItems(newsResponse.getUserNews());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                if (newsForTeamAdapter.getItems().size() < 15) {
                    return;
                }
                currentPage++;
                paginationLoad();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return newsForTeamAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapterObservable.set(newsForTeamAdapter);
    }

    public void paginationLoad() {
        newsForTeamAdapter.setLoading(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().newsForTeam(paginationCallback, token, id, currentPage);
    }

    private Callback<NewsResponse> paginationCallback = new Callback<NewsResponse>() {
        @Override
        public void onSuccess(NewsResponse newsResponse) {
            getActivity().runOnUiThread(() -> {
                newsForTeamAdapter.setItems(newsResponse.getUserNews());
                isLastPage = newsResponse.getPages() <= currentPage;
                newsForTeamAdapter.setLoading(false);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                getActivity().runOnUiThread(() -> {
                    isLastPage = true;
                    newsForTeamAdapter.setLoading(false);
                });
                SnackbarHelper.getSnackBarFromStatus(recyclerView, error.getStatus())
                        .setAction(R.string.reload, v -> paginationLoad())
                        .show();
            } else {
                if (error instanceof SingleMessageError) {
                    String message = ((SingleMessageError) error).getMessage();
                    if (message.equals("Nie ma już więcej wyników")) {
                        getActivity().runOnUiThread(() -> {
                            isLastPage = true;
                            newsForTeamAdapter.setLoading(false);
                        });
                        Log.d(TAG, "onSmthWrong: nie ma więcej wyników");
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
                getActivity().runOnUiThread(() -> {
                    initItemsView(newsResponse);
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    newsForTeamAdapter.setItems(newsResponse.getUserNews());
                    isLastPage = newsResponse.getPages() <= currentPage;
                    newsForTeamAdapter.setLoading(false);
                });
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);

            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                status.set(error.getStatus());
                errorVisibility.set(true);
            } else {
                if (error instanceof SingleMessageError) {
                    String message = ((SingleMessageError) error).getMessage();
//                    if (message.equals("Nie ma już więcej wyników")) {
//                        getActivity().runOnUiThread(() -> {
//                            isLastPage = true;
//                            newsForTeamAdapter.setLoading(false);
//                        });
//                        Log.d(TAG, "onSmthWrong: nie ma więcej wyników");
//                    }
                    if (message.equals("Brak wyników")) {
                        placeholderVisibility.set(true);
                    }
                }
            }

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super NewsResponse> observer) {

        }
    };

}
