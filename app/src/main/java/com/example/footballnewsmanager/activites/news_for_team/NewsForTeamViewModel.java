package com.example.footballnewsmanager.activites.news_for_team;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news.newsForTeam.NewsForTeamAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityNewsForTeamBinding;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserNews;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsForTeamViewModel extends BaseViewModel implements RecyclerViewItemsListener<UserNews> {


    public static final String TAG = "NewsForTeam";

    public ObservableField<NewsForTeamAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);


    private boolean isLastPage;
    private int currentPage = 0;
    private NewsForTeamAdapter newsAdapter;
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
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().newsForTeam(callback, token, id, currentPage);
    }

    private void initItemsView(NewsResponse newsResponse) {
        newsAdapter = new NewsForTeamAdapter(getActivity());
        newsAdapter.setRecyclerViewItemsListener(this);
        newsAdapter.setHeaderItems(id, name, img, isFavourite);
        newsAdapter.setHeaderRecyclerViewItemsListener(listener);
        newsAdapter.setCountAll(newsResponse.getNewsCount());
        newsAdapter.setCountToday(newsResponse.getNewsToday());
        newsAdapter.setItems(newsResponse.getUserNews());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                currentPage++;
                Log.d("NewsForTeam", "loadMoreItems");
                newsAdapter.isLoading = true;
                String token = UserPreferences.get().getAuthToken();
                Connection.get().newsForTeam(callback, token, id, currentPage);
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


    private Callback<NewsResponse> refreshCallback = new Callback<NewsResponse>() {
        @Override
        public void onSuccess(NewsResponse newsResponse) {
            placeholderVisibility.set(false);
            getActivity().runOnUiThread(() -> {
                currentPage = 0;
                isLastPage = newsResponse.getPages() <= currentPage;
                newsAdapter.isLoading = false;
                newsAdapter.setCountAll(newsResponse.getNewsCount());
                newsAdapter.setCountToday(newsResponse.getNewsToday());
                newsAdapter.refresh(newsResponse);
                ((NewsFragmentBinding) getBinding()).newsSwipeRefresh
                        .setRefreshing(false);
                ProgressDialog.get().dismiss();
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            currentPage = 0;
            isLastPage = true;
            newsAdapter.isLoading = false;
            ((NewsFragmentBinding) getBinding()).newsSwipeRefresh
                    .setRefreshing(false);
            postRunnable.set(placeHolderAttachRunnable);
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
                    Log.d(TAG, "onSuccessFirst: ");
                    initItemsView(newsResponse);
                });
            } else {
                Log.d(TAG, "onSuccessOther: ");
                getActivity().runOnUiThread(() -> {
                    newsAdapter.setItems(newsResponse.getUserNews());
                    isLastPage = newsResponse.getPages() <= currentPage;
                    newsAdapter.isLoading = false;
                });
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d(TAG, "onSmthWrong: ");
            if(error instanceof SingleMessageError){
                String message = ((SingleMessageError) error).getMessage();
                if(message.equals("Nie ma już więcej wyników")){
                    Log.d(TAG, "onSmthWrong: nie ma więcej wyników");
                    isLastPage = true;
                    newsAdapter.isLoading = false;
                    postRunnable.set(placeHolderAttachRunnable);
                }
                if(message.equals("Dla podanej frazy nie ma żadnej drużyny"))
                {
                    loadingVisibility.set(false);
                    placeholderVisibility.set(true);
                }

            }

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super NewsResponse> observer) {

        }
    };

    @Override
    public void onDetached() {
        postRunnable.set(placeHolderDetachRunnable);
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
        Connection.get().newsForTeam(refreshCallback, token, id, 0);
    }

    @Override
    public void onChangeItems() {
        updateNews();
    }

    private Runnable placeHolderAttachRunnable = () -> {
        newsAdapter.setPlaceholder(true);
        recyclerView.smoothScrollToPosition(newsAdapter.getItemCount() - 1);
    };

    private Runnable placeHolderDetachRunnable = () -> {
        isLastPage = false;
        newsAdapter.setPlaceholder(false);
    };
    private Runnable backToFrontRunnable = () -> recyclerView.scrollToPosition(0);

}
