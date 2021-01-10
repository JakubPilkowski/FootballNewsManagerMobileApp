package pl.android.footballnewsmanager.fragments.main.news;

import android.content.Intent;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import pl.android.footballnewsmanager.adapters.news.NewsAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.errors.SingleMessageError;
import pl.android.footballnewsmanager.api.responses.main.NewsResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.fragments.main.MainFragment;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.NewsPlaceholder;
import pl.android.footballnewsmanager.helpers.PaginationScrollListener;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.BadgeListener;
import pl.android.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserNews;

public class NewsFragmentViewModel extends BaseViewModel implements ExtendedRecyclerViewItemsListener<UserNews> {

    public ObservableField<NewsAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableField<SwipeRefreshLayout.OnRefreshListener> swipeRefreshListenerObservable = new ObservableField<>();
    public ObservableInt swipeRefreshColor = new ObservableInt(R.color.colorPrimary);
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);


    private boolean isLastPage;
    private int currentPage = 0;
    public NewsAdapter newsAdapter;
    private RecyclerView recyclerView;
    private BadgeListener badgeListener;
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;
    private SwipeRefreshLayout swipeRefreshLayout;

    public void init(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
        swipeRefreshLayout = ((NewsFragmentBinding) getBinding()).newsSwipeRefresh;
        recyclerView = ((NewsFragmentBinding) getBinding()).newsRecyclerView;
        tryAgainListener.set(listener);
        swipeRefreshListenerObservable.set(this::updateNews);
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
        initPlaceholder();
        load();
    }

    public void load() {
        currentPage = 0;
        isLastPage = false;
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
            Intent intent = new Intent(getActivity(), ManageTeamsActivity.class);
            getActivity().startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS);
        });
    }


    private void initItemsView(NewsResponse newsResponse) {
        newsAdapter = new NewsAdapter(getActivity());
        newsAdapter.setExtendedRecyclerViewItemsListener(this);
        newsAdapter.setBadgeListener(badgeListener);
        newsAdapter.setItems(newsResponse.getUserNews());
        newsAdapter.setCountAll(newsResponse.getNewsCount());
        newsAdapter.setCountToday(newsResponse.getNewsToday());
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
                swipeRefreshLayout.setRefreshing(false);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            placeholderVisibility.set(false);
            currentPage = 0;
            isLastPage = true;
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                if (swipeRefreshLayout.isRefreshing()) {
                    getActivity().runOnUiThread(() -> {
                        swipeRefreshLayout.setRefreshing(false);
                        newsAdapter.setLoading(false);
                    });
                    MainFragment mainFragment = ((MainActivity) getActivity()).getMainFragment();
                    SnackbarHelper.showDefaultSnackBarFromStatus(mainFragment.binding.mainBottomNavView, error.getStatus());
                }
            } else {
                getActivity().runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    newsAdapter.setLoading(false);
                });
                errorVisibility.set(false);
                itemsVisibility.set(false);
                placeholderVisibility.set(true);
            }
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
                    BottomNavigationView bottomNavigationView = ((MainActivity) getActivity()).getMainFragment()
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
                        getActivity().runOnUiThread(() -> {
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
            placeholderVisibility.set(false);
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
    public void onChangeItem(UserNews oldNews, UserNews newNews) {
        newsAdapter.onChange(oldNews, newNews);
    }

    public void updateNews() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().news(refreshCallback, token, 0);
    }

    public void backToTop(){
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onChangeItems() {
        updateNews();
    }

}
