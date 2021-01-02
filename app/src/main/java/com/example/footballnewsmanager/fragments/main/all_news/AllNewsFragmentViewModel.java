package com.example.footballnewsmanager.fragments.main.all_news;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.search.SearchActivity;
import com.example.footballnewsmanager.adapters.all_news.AllNewsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.AllNewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.AllNewsFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserNews;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class AllNewsFragmentViewModel extends BaseViewModel implements RecyclerViewItemsListener<UserNews> {

    public ObservableField<AllNewsAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableField<SwipeRefreshLayout.OnRefreshListener> swipeRefreshListenerObservable = new ObservableField<>();
    public ObservableInt swipeRefreshColor = new ObservableInt(R.color.colorPrimary);
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    private boolean isLastPage;
    private int currentPage = 0;
    private AllNewsAdapter allNewsAdapter;
    private RecyclerView recyclerView;
    private BadgeListener badgeListener;
    private SwipeRefreshLayout swipeRefreshLayout;


    // TODO: Implement the ViewModel
    public void init(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
        SearchView searchView = ((AllNewsFragmentBinding)getBinding()).allNewsSearchView;
        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getActivity().startActivity(intent);
        });
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
            }
        });
        swipeRefreshLayout = ((AllNewsFragmentBinding) getBinding()).allNewsSwipeRefresh;
        swipeRefreshListenerObservable.set(this::updateNews);
        recyclerView = ((AllNewsFragmentBinding) getBinding()).allNewsRecyclerView;
        tryAgainListener.set(listener);
        load();
    }

    public void load(){
        errorVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().allNews(callback, token, currentPage);
    }


    private void initItemsView(AllNewsResponse allNewsResponse) {
        allNewsAdapter = new AllNewsAdapter(getActivity());
        allNewsAdapter.setRecyclerViewItemsListener(this);
        allNewsAdapter.setItems(allNewsResponse.getUserNews(), allNewsResponse.getAdditionalContent());
        allNewsAdapter.setBadgeListener(badgeListener);
        allNewsAdapter.setCountAll(allNewsResponse.getNewsCount());
        allNewsAdapter.setCountToday(allNewsResponse.getNewsToday());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                currentPage++;
                Log.d("News", "loadMoreItems");
                paginationLoad();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return allNewsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapterObservable.set(allNewsAdapter);
    }

    private void paginationLoad(){
        allNewsAdapter.setLoading(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().allNews(paginationCallback, token, currentPage);
    }

    private Callback<AllNewsResponse> paginationCallback = new Callback<AllNewsResponse>() {
        @Override
        public void onSuccess(AllNewsResponse allNewsResponse) {
            getActivity().runOnUiThread(() -> {
                allNewsAdapter.setItems(allNewsResponse.getUserNews(), allNewsResponse.getAdditionalContent());
                isLastPage = allNewsResponse.getPages() <= currentPage;
                allNewsAdapter.setLoading(false);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                getActivity().runOnUiThread(() -> {
                    isLastPage = true;
                    allNewsAdapter.setLoading(false);
                    BottomNavigationView bottomNavigationView = ((MainActivity)getActivity()).getMainFragment()
                            .binding.mainBottomNavView;
                    SnackbarHelper.getInfinitiveSnackBarFromStatus(recyclerView, error.getStatus())
                            .setAction(R.string.reload, v -> paginationLoad())
                            .setAnchorView(bottomNavigationView)
                            .show();
                });
            } else {
                if (error instanceof SingleMessageError) {
                    String message = ((SingleMessageError) error).getMessage();
                    if (message.equals("Nie ma już więcej wyników")) {
                        getActivity().runOnUiThread(()->{
                            isLastPage = true;
                            allNewsAdapter.setLoading(false);
                        });
                    }
                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super AllNewsResponse> observer) {

        }
    };


    private Callback<AllNewsResponse> refreshCallback = new Callback<AllNewsResponse>() {
        @Override
        public void onSuccess(AllNewsResponse newsResponse) {
            getActivity().runOnUiThread(() -> {
                currentPage = 0;
                isLastPage = newsResponse.getPages() <= currentPage;
                allNewsAdapter.setLoading(false);
                allNewsAdapter.setCountAll(newsResponse.getNewsCount());
                allNewsAdapter.setCountToday(newsResponse.getNewsToday());
                allNewsAdapter.refresh(newsResponse);
                swipeRefreshLayout.setRefreshing(false);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            currentPage = 0;
            isLastPage = true;
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                if (swipeRefreshLayout.isRefreshing()) {
                    getActivity().runOnUiThread(() -> {
                        swipeRefreshLayout.setRefreshing(false);
                        allNewsAdapter.setLoading(false);
                    });
                    MainFragment mainFragment = ((MainActivity) getActivity()).getMainFragment();
                    SnackbarHelper.showDefaultSnackBarFromStatus(mainFragment.binding.mainBottomNavView, error.getStatus());
                }
            } else {
                getActivity().runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    allNewsAdapter.setLoading(false);
                });
                errorVisibility.set(false);
                itemsVisibility.set(false);
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super AllNewsResponse> observer) {

        }


    };


    private Callback<AllNewsResponse> callback = new Callback<AllNewsResponse>() {
        @Override
        public void onSuccess(AllNewsResponse newsResponse) {

            if (loadingVisibility.get()) {
                loadingVisibility.set(false);
                itemsVisibility.set(true);
                getActivity().runOnUiThread(() -> {
                    initItemsView(newsResponse);
                });
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {

            loadingVisibility.set(false);
            itemsVisibility.set(false);

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
                    }
                }
            }

//            getActivity().runOnUiThread(()->{
//                isLastPage = true;
//                allNewsAdapter.setLoading(false);
//            });
//            postRunnable.set(placeHolderAttachRunnable);
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super AllNewsResponse> observer) {

        }
    };

//    private Runnable placeHolderAttachRunnable = () -> {
//        allNewsAdapter.setPlaceholder(true);
//        recyclerView.smoothScrollToPosition(allNewsAdapter.getItemCount() - 1);
//    };
//
//    private Runnable placeHolderDetachRunnable = () -> {
//        isLastPage = false;
//        allNewsAdapter.setPlaceholder(false);
//    };
    private Runnable backToFrontRunnable = () -> recyclerView.scrollToPosition(0);

    @Override
    public void onDetached() {

//        postRunnable.set(placeHolderDetachRunnable);
    }

    @Override
    public void backToFront() {
        postRunnable.set(backToFrontRunnable);
    }

    @Override
    public void onChangeItem(UserNews oldNews, UserNews newNews) {
        allNewsAdapter.onChange(oldNews, newNews);
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
