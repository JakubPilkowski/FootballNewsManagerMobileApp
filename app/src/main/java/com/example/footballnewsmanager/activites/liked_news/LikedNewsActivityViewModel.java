package com.example.footballnewsmanager.activites.liked_news;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.liked_news.LikedNewsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityFavouriteNewsBinding;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserNews;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class LikedNewsActivityViewModel extends BaseViewModel implements RecyclerViewItemsListener<UserNews>{



    public ObservableField<LikedNewsAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);

    private boolean isLastPage;
    private int currentPage = 0;
    private LikedNewsAdapter likedNewsAdapter;
    private RecyclerView recyclerView;


    public void init() {
        recyclerView = ((ActivityFavouriteNewsBinding) getBinding()).favouriteNewsRecyclerView;
        load();
    }

    private void load() {
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getLikedNews(callback, token, currentPage);
    }

    private void initItemsView(NewsResponse newsResponse) {
        likedNewsAdapter = new LikedNewsAdapter(getActivity());
        likedNewsAdapter.setListener(this);
        likedNewsAdapter.setItems(newsResponse.getUserNews());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                if(likedNewsAdapter.getItems().size()<20){
                    return;
                }
                currentPage++;
                likedNewsAdapter.isLoading = true;
                String token = UserPreferences.get().getAuthToken();
                Connection.get().getLikedNews(callback, token, currentPage);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return likedNewsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapterObservable.set(likedNewsAdapter);
    }


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
                    likedNewsAdapter.setItems(newsResponse.getUserNews());
                    isLastPage = newsResponse.getPages() <= currentPage;
                    likedNewsAdapter.isLoading = false;
                });
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error instanceof SingleMessageError) {
                String message = ((SingleMessageError) error).getMessage();
                if (message.equals("Nie ma już więcej wyników")) {
                    isLastPage = true;
                    likedNewsAdapter.isLoading = false;
                }
                if (message.equals("Dla podanej frazy nie ma żadnej drużyny")) {
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
    }

    @Override
    public void backToFront() {
    }

    @Override
    public void onChangeItem(UserNews oldNews, UserNews newNews) {
    }


    @Override
    public void onChangeItems() {
//        updateNews();
    }

}
