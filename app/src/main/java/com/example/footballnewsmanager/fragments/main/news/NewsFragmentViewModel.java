package com.example.footballnewsmanager.fragments.main.news;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.FabScrollListener;
import com.example.footballnewsmanager.helpers.NewsPlaceholder;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserNews;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsFragmentViewModel extends BaseViewModel implements ExtendedRecyclerViewItemsListener<UserNews> {
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
    public NewsAdapter newsAdapter;
    private RecyclerView recyclerView;
    private BadgeListener badgeListener;
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton newsFab;
    private Animation animation;
    private boolean isAnimated = false;

    private Animation.AnimationListener enterAnimation = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            isAnimated = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            newsFab.setAlpha(1f);
            newsFab.setVisibility(View.GONE);
            isAnimated = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            onAnimationEnd(animation);
        }
    };

    private Animation.AnimationListener closeAnimation = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            isAnimated = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            newsFab.setVisibility(View.GONE);
            isAnimated = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            onAnimationEnd(animation);
        }
    };

    private Animation.AnimationListener openAnimation = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            newsFab.setVisibility(View.VISIBLE);
            isAnimated = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isAnimated = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            onAnimationEnd(animation);
        }
    };


    public void init(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
        swipeRefreshLayout = ((NewsFragmentBinding) getBinding()).newsSwipeRefresh;
        newsFab = ((NewsFragmentBinding)getBinding()).newsFab;
        recyclerView = ((NewsFragmentBinding) getBinding()).newsRecyclerView;
        newsFab.setAlpha(0f);
        animation = AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_down);
        animation.setAnimationListener(enterAnimation);
        newsFab.startAnimation(animation);
        tryAgainListener.set(listener);
        swipeRefreshListenerObservable.set(this::updateNews);
        initPlaceholder();
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
        newsAdapter.setExtendedRecyclerViewItemsListener(this);
        newsAdapter.setBadgeListener(badgeListener);
        newsAdapter.setItems(newsResponse.getUserNews());
        newsAdapter.setCountAll(newsResponse.getNewsCount());
        newsAdapter.setCountToday(newsResponse.getNewsToday());
        FabScrollListener scrollListener = new FabScrollListener() {
            @Override
            public void onScrollStateChanged(@androidx.annotation.NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(position == 0 && newsFab.isShown() && !isAnimated){
                    animation = AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_down);
                    animation.setAnimationListener(closeAnimation);
                    newsFab.startAnimation(animation);
                }
            }


            @Override
            protected void onScroll(int dx, int dy) {
                if (dy < -20 && !newsFab.isShown() && !isAnimated) {
                    animation = AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_up);
                    animation.setAnimationListener(openAnimation);
                    newsFab.startAnimation(animation);
                } else if (dy > 12 && newsFab.isShown() && !isAnimated) {
                    animation = AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_down);
                    animation.setAnimationListener(closeAnimation);
                    newsFab.startAnimation(animation);
                }
            }

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
    public void onDetached() {

    }

    @Override
    public void backToFront() {
    }

    @Override
    public void onChangeItem(UserNews oldNews, UserNews newNews) {
        newsAdapter.onChange(oldNews, newNews);
    }

    public void updateNews() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().news(refreshCallback, token, 0);
    }

    public void onBackToTop(){
        recyclerView.scrollToPosition(0);
        animation = AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_down);
        animation.setAnimationListener(closeAnimation);
        newsFab.startAnimation(animation);
    }

    @Override
    public void onChangeItems() {
        updateNews();
    }

}
