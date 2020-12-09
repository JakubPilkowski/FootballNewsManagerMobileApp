package com.example.footballnewsmanager.fragments.main.news;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news.NewsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;
import com.example.footballnewsmanager.models.UserNews;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsFragmentViewModel extends BaseViewModel implements NewsRecyclerViewListener {
    // TODO: Implement the ViewModel

    public ObservableField<NewsAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableField<SwipeRefreshLayout.OnRefreshListener> swipeRefreshListenerObservable = new ObservableField<>();
    public ObservableInt swipeRefreshColor = new ObservableInt(R.color.colorPrimary);

    private boolean isLastPage = false;
    private int currentPage = 0;
    private NewsAdapter newsAdapter;
    private RecyclerView recyclerView;
    private BadgeListener badgeListener;

    public void init(NewsResponse newsResponse, BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
        swipeRefreshListenerObservable.set(this::updateNews);
        recyclerView = ((NewsFragmentBinding) getBinding()).newsRecyclerView;
        newsAdapter = new NewsAdapter(getActivity());
        newsAdapter.setNewsRecyclerViewListener(this);
        newsAdapter.setBadgeListener(badgeListener);
        newsAdapter.setItems(newsResponse.getNews());
        newsAdapter.setCountAll(newsResponse.getNewsCount());
        newsAdapter.setCountToday(newsResponse.getNewsToday());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                Log.d("News", "loadMoreItems");
                currentPage++;
                newsAdapter.isLoading = true;
                String token = UserPreferences.get().getAuthToken();
                Connection.get().news(callback, token, currentPage);
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
            getActivity().runOnUiThread(() -> {
                currentPage = 0;
                isLastPage = newsResponse.getPages() <= currentPage;
                newsAdapter.isLoading = false;
                newsAdapter.setCountAll(newsResponse.getNewsCount());
                newsAdapter.setCountToday(newsResponse.getNewsToday());
                newsAdapter.refresh(newsResponse);
                badgeListener.onBadgeChange();
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
            getActivity().runOnUiThread(() -> {
                newsAdapter.setItems(newsResponse.getNews());
                isLastPage = newsResponse.getPages() <= currentPage;
                newsAdapter.isLoading = false;
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            isLastPage = true;
            newsAdapter.isLoading = false;
            postRunnable.set(placeHolderAttachRunnable);
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

    public void updateNews(){
        String token = UserPreferences.get().getAuthToken();
        Connection.get().news(refreshCallback, token, 0);
    }

    @Override
    public void onChangeItems() {
        updateNews();
    }

    private Runnable placeHolderAttachRunnable = () ->{
        newsAdapter.setPlaceholder(true);
        recyclerView.smoothScrollToPosition(newsAdapter.getItemCount() - 1);
    };

    private Runnable placeHolderDetachRunnable = () -> {
        isLastPage = false;
        newsAdapter.setPlaceholder(false);
    };
    private Runnable backToFrontRunnable = () -> recyclerView.scrollToPosition(0);

}
