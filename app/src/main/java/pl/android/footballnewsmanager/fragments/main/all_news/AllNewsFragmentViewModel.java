package pl.android.footballnewsmanager.fragments.main.all_news;

import android.content.Intent;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.AllNewsFragmentBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.activites.search.SearchActivity;
import pl.android.footballnewsmanager.adapters.all_news.AllNewsAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.errors.SingleMessageError;
import pl.android.footballnewsmanager.api.responses.main.AllNewsResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.fragments.main.MainFragment;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.PaginationScrollListener;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.BadgeListener;
import pl.android.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserNews;

public class AllNewsFragmentViewModel extends BaseViewModel implements ExtendedRecyclerViewItemsListener<UserNews> {

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
    public AllNewsAdapter allNewsAdapter;
    private RecyclerView recyclerView;
    private BadgeListener badgeListener;
    private SwipeRefreshLayout swipeRefreshLayout;


    public void init(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
        SearchView searchView = ((AllNewsFragmentBinding) getBinding()).allNewsSearchView;
        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getActivity().startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS);
        });
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS);
            }
        });
        swipeRefreshLayout = ((AllNewsFragmentBinding) getBinding()).allNewsSwipeRefresh;
        swipeRefreshListenerObservable.set(this::updateNews);
        recyclerView = ((AllNewsFragmentBinding) getBinding()).allNewsRecyclerView;
        tryAgainListener.set(listener);
        load();
    }

    public void load() {
        currentPage = 0;
        isLastPage = false;
        errorVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        boolean proposed = UserPreferences.get().getProposed();
        Connection.get().allNews(callback, token, currentPage, proposed);
    }


    private void initItemsView(AllNewsResponse allNewsResponse) {
        allNewsAdapter = new AllNewsAdapter(getActivity(), UserPreferences.get().getProposed());
        allNewsAdapter.setExtendedRecyclerViewItemsListener(this);
        if (allNewsResponse.getProposedTeams() != null)
            allNewsAdapter.setItems(allNewsResponse.getUserNews(), allNewsResponse.getProposedTeams());
        else allNewsAdapter.setItems(allNewsResponse.getUserNews());
        allNewsAdapter.setBadgeListener(badgeListener);
        allNewsAdapter.setCountAll(allNewsResponse.getNewsCount());
        allNewsAdapter.setCountToday(allNewsResponse.getNewsToday());
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
                return allNewsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapterObservable.set(allNewsAdapter);
    }

    private void paginationLoad() {
        allNewsAdapter.setLoading(true);
        String token = UserPreferences.get().getAuthToken();
        boolean proposed = UserPreferences.get().getProposed();
        Connection.get().allNews(paginationCallback, token, currentPage, proposed);
    }

    private Callback<AllNewsResponse> paginationCallback = new Callback<AllNewsResponse>() {
        @Override
        public void onSuccess(AllNewsResponse allNewsResponse) {
            getActivity().runOnUiThread(() -> {
                if (allNewsResponse.getProposedTeams() != null)
                    allNewsAdapter.setItems(allNewsResponse.getUserNews(), allNewsResponse.getProposedTeams());
                else allNewsAdapter.setItems(allNewsResponse.getUserNews());
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
                initItemsView(newsResponse);
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
                    if (message.equals("Nie ma już więcej wyników")) {
                        itemsVisibility.set(false);
                        loadingVisibility.set(false);
                        errorVisibility.set(false);
                    }
                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super AllNewsResponse> observer) {

        }
    };

    public void backToTop() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onChangeItem(UserNews oldNews, UserNews newNews) {
        allNewsAdapter.onChange(oldNews, newNews);
    }

    private void updateNews() {
        String token = UserPreferences.get().getAuthToken();
        boolean proposed = UserPreferences.get().getProposed();
        Connection.get().allNews(refreshCallback, token, 0, proposed);
    }

    @Override
    public void onChangeItems() {
        updateNews();
    }
}
