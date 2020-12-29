package com.example.footballnewsmanager.activites.news_for_team;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.news.newsForTeam.NewsForTeamAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityNewsForTeamBinding;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsForTeamViewModel extends BaseViewModel {


    public static final String TAG = "NewsForTeam";

    public ObservableField<NewsForTeamAdapter> adapterObservable = new ObservableField<>();
    public ObservableField<Runnable> postRunnable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);


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
                if(newsForTeamAdapter.getItems().size() < 15){
                    return;
                }
                currentPage++;
                Log.d("ItemCount", "loadMoreItems" + newsForTeamAdapter.isLoading);
                newsForTeamAdapter.setLoading(true);
                String token = UserPreferences.get().getAuthToken();
                Connection.get().newsForTeam(callback, token, id, currentPage);
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
                    newsForTeamAdapter.setItems(newsResponse.getUserNews());
                    isLastPage = newsResponse.getPages() <= currentPage;
                    newsForTeamAdapter.setLoading(false);
                });
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d(TAG, "onSmthWrong: ");
            if(error instanceof SingleMessageError){
                String message = ((SingleMessageError) error).getMessage();
                if(message.equals("Nie ma już więcej wyników")){
                    getActivity().runOnUiThread(()->{
                        isLastPage = true;
                        newsForTeamAdapter.setLoading(false);
                    });
                    Log.d(TAG, "onSmthWrong: nie ma więcej wyników");
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

}
