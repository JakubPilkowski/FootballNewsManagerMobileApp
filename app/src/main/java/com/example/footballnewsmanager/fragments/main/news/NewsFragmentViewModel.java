package com.example.footballnewsmanager.fragments.main.news;

import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.news.NewsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.helpers.PaginationScrollListener;
import com.example.footballnewsmanager.helpers.UserPreferences;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsFragmentViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel

    public ObservableField<RecyclerView.OnScrollListener> scrollListenerObservable = new ObservableField<>();
    public ObservableField<NewsAdapter> adapterObservable = new ObservableField<>();
    //    public ObservableField<LinearLayoutManager> layoutManager = new ObservableField<>();
    private boolean isLastPage = false;
    private int currentPage = 0;
    private NewsAdapter newsAdapter;

    public void init(NewsResponse newsResponse) {
        newsAdapter = new NewsAdapter(getActivity());

        newsAdapter.setItems(newsResponse.getAllNews());
        newsAdapter.setCountAll(newsResponse.getNewsCount());
        newsAdapter.setCountToday(newsResponse.getNewsToday());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                currentPage++;
                newsAdapter.isLoading = true;
                newsAdapter.notifyItemChanged(newsAdapter.getItemCount());
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

        RecyclerView recyclerView = ((NewsFragmentBinding) getBinding()).newsRecyclerView;
        recyclerView.addOnScrollListener(scrollListener);

        adapterObservable.set(newsAdapter);
    }


    private Callback<NewsResponse> callback = new Callback<NewsResponse>() {
        @Override
        public void onSuccess(NewsResponse newsResponse) {
            getActivity().runOnUiThread(() -> {
                newsAdapter.setItems(newsResponse.getAllNews());
                newsAdapter.isLoading = false;
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            newsAdapter.isLoading = false;
            getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity().getApplicationContext(), error.getError(), Toast.LENGTH_LONG).show();
                    }
            );
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super NewsResponse> observer) {

        }
    };
}
