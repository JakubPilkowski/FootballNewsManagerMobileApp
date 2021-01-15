package pl.android.footballnewsmanager.activites.liked_news;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.ActivityFavouriteNewsBinding;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.adapters.liked_news.LikedNewsAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.errors.SingleMessageError;
import pl.android.footballnewsmanager.api.responses.main.NewsResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.PaginationScrollListener;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;

public class LikedNewsActivityViewModel extends BaseViewModel {


    public ObservableField<LikedNewsAdapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    private boolean isLastPage;
    private int currentPage = 0;
    private LikedNewsAdapter likedNewsAdapter;
    private RecyclerView recyclerView;


    public void init() {
        recyclerView = ((ActivityFavouriteNewsBinding) getBinding()).favouriteNewsRecyclerView;
        tryAgainListener.set(listener);
        load();
    }

    private void load() {
        errorVisibility.set(false);
        itemsVisibility.set(false);
        placeholderVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getLikedNews(callback, token, currentPage);
    }

    private void initItemsView(NewsResponse newsResponse) {
        likedNewsAdapter = new LikedNewsAdapter(getActivity());
        likedNewsAdapter.setItems(newsResponse.getUserNews());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                if (likedNewsAdapter.getItems().size() < 20) {
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
                return likedNewsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapterObservable.set(likedNewsAdapter);
    }


    private void paginationLoad() {
        likedNewsAdapter.setLoading(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getLikedNews(paginationCallback, token, currentPage);
    }

    private Callback<NewsResponse> paginationCallback = new Callback<NewsResponse>() {
        @Override
        public void onSuccess(NewsResponse newsResponse) {
            getActivity().runOnUiThread(() -> {
                likedNewsAdapter.setItems(newsResponse.getUserNews());
                isLastPage = newsResponse.getPages() <= currentPage;
                likedNewsAdapter.setLoading(false);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            getActivity().runOnUiThread(()->{
                isLastPage = true;
                likedNewsAdapter.setLoading(false);
            });
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                SnackbarHelper.getInfinitiveSnackBarFromStatus(recyclerView, error.getStatus())
                        .setAction(R.string.reload, v -> paginationLoad())
                        .show();
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
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                errorVisibility.set(true);
                status.set(error.getStatus());
            } else {
                if (error instanceof SingleMessageError) {
                    String message = ((SingleMessageError) error).getMessage();
                    if (message.equals("Nie ma już więcej wyników")) {
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
