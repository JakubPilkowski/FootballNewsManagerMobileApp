package com.example.footballnewsmanager.fragments.main.all_news;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.search.SearchActivity;
import com.example.footballnewsmanager.adapters.all_news.AllNewsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.main.AllNewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.AllNewsFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;
import com.example.footballnewsmanager.models.UserNews;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class AllNewsFragmentViewModel extends BaseViewModel implements NewsRecyclerViewListener {

    public ObservableField<AllNewsAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableField<SwipeRefreshLayout.OnRefreshListener> swipeRefreshListenerObservable = new ObservableField<>();
    public ObservableInt swipeRefreshColor = new ObservableInt(R.color.colorPrimary);


    private boolean isLastPage = false;
    private int currentPage = 0;
    private AllNewsAdapter newsAdapter;
    private RecyclerView recyclerView;

    // TODO: Implement the ViewModel
    public void init(AllNewsResponse newsResponse, BadgeListener badgeListener) {

        SearchView searchView = ((AllNewsFragmentBinding)getBinding()).allNewsSearchView;
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                Log.d(AllNewsFragment.TAG, "onClick: ");
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AllNewsFragment.TAG, "onSearchClick: ");
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                Log.d(AllNewsFragment.TAG, "onFocusChange: " + hasFocus);
                if(hasFocus) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(AllNewsFragment.TAG, "onQueryTextSubmit: ");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(AllNewsFragment.TAG, "onQueryTextChange: ");
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(AllNewsFragment.TAG, "onClose: ");
                return false;
            }
        });

        swipeRefreshListenerObservable.set(this::updateNews);
        recyclerView = ((AllNewsFragmentBinding) getBinding()).allNewsRecyclerView;
        newsAdapter = new AllNewsAdapter(getActivity());
        newsAdapter.setNewsRecyclerViewListener(this);
        newsAdapter.setItems(newsResponse.getNews(), newsResponse.getAdditionalContent());
        newsAdapter.setCountAll(newsResponse.getNewsCount());
        newsAdapter.setBadgeListener(badgeListener);
        newsAdapter.setCountToday(newsResponse.getNewsToday());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                Log.d("News", "loadMoreItems");
                currentPage++;
                newsAdapter.isLoading = true;
                String token = UserPreferences.get().getAuthToken();
                Connection.get().allNews(callback, token, currentPage);
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


    private Callback<AllNewsResponse> refreshCallback = new Callback<AllNewsResponse>() {
        @Override
        public void onSuccess(AllNewsResponse newsResponse) {
            getActivity().runOnUiThread(() -> {
                currentPage = 0;
                isLastPage = newsResponse.getPages() <= currentPage;
                newsAdapter.isLoading = false;
                newsAdapter.setCountAll(newsResponse.getNewsCount());
                newsAdapter.setCountToday(newsResponse.getNewsToday());
                newsAdapter.refresh(newsResponse);
                ((AllNewsFragmentBinding) getBinding()).allNewsSwipeRefresh
                        .setRefreshing(false);
                ProgressDialog.get().dismiss();
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            currentPage = 0;
            isLastPage = true;
            newsAdapter.isLoading = false;
            ((AllNewsFragmentBinding) getBinding()).allNewsSwipeRefresh
                    .setRefreshing(false);
            postRunnable.set(placeHolderAttachRunnable);
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super AllNewsResponse> observer) {

        }


    };


    private Callback<AllNewsResponse> callback = new Callback<AllNewsResponse>() {
        @Override
        public void onSuccess(AllNewsResponse newsResponse) {
            getActivity().runOnUiThread(() -> {
                newsAdapter.setItems(newsResponse.getNews(), newsResponse.getAdditionalContent());
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
        protected void subscribeActual(@NonNull Observer<? super AllNewsResponse> observer) {

        }
    };

    private Runnable placeHolderAttachRunnable = () -> {
        newsAdapter.setPlaceholder(true);
        recyclerView.smoothScrollToPosition(newsAdapter.getItemCount() - 1);
    };

    private Runnable placeHolderDetachRunnable = () -> {
        isLastPage = false;
        newsAdapter.setPlaceholder(false);
    };
    private Runnable backToFrontRunnable = () -> recyclerView.scrollToPosition(0);

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
        Connection.get().allNews(refreshCallback, token, 0);
    }

    @Override
    public void onChangeItems() {
        updateNews();
    }
}
