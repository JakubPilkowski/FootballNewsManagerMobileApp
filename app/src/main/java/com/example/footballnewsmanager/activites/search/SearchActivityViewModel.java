package com.example.footballnewsmanager.activites.search;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.search.SearchAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.search.SearchResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivitySearchBinding;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.KeyboardHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchActivityViewModel extends BaseViewModel {

    public ObservableField<SearchAdapter> searchAdapterObservable = new ObservableField<>();
    private SearchAdapter searchAdapter;
    public CompositeDisposable disposable = new CompositeDisposable();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);
    private SearchView searchView;
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = new ErrorView.OnTryAgainListener() {
        @Override
        public void onClick() {
            load(queryText);
        }
    };

    private String queryText;

    public void init() {
        searchView = ((ActivitySearchBinding) getBinding()).searchActivitySearchView;
        tryAgainListener.set(listener);
        RecyclerView recyclerView = ((ActivitySearchBinding) getBinding()).searchActivityRecyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@androidx.annotation.NonNull RecyclerView recyclerView, int newState) {
                KeyboardHelper.hideKeyboard(getActivity());
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        searchView.setIconified(false);
        Observable<String> observableQueryText = Observable
                .create(emitter -> searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        queryText = newText;
                        if (!emitter.isDisposed()) {
                            emitter.onNext(newText);
                        }
                        return false;
                    }
                }));


        observableQueryText
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull String newText) {
                        load(newText);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingVisibility.set(false);
                        itemsVisibility.set(false);
                        getActivity().runOnUiThread(() -> searchAdapter.removeItems());
                        if(e.getMessage() != null && e.getMessage().contains("Unable to resolve host ")){
                            status.set(598);
                        }
                        else if(e.getMessage()!=null && e.getMessage().contains("The source did not signal an event for")){
                            status.set(408);
                        }
                        errorVisibility.set(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        searchAdapter = new SearchAdapter();
        searchAdapter.setActivity(getActivity());
        searchAdapterObservable.set(searchAdapter);
        load("");
    }

    public void load(String text) {
        itemsVisibility.set(false);
        errorVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getQueryResults(callback, token, text);
    }


    private Callback<SearchResponse> callback = new Callback<SearchResponse>() {
        @Override
        public void onSuccess(SearchResponse searchResponse) {
            loadingVisibility.set(false);
            placeholderVisibility.set(false);
            itemsVisibility.set(true);
            getActivity().runOnUiThread(() -> searchAdapter.setItems(searchResponse.getResults()));
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            itemsVisibility.set(false);
            getActivity().runOnUiThread(() -> searchAdapter.removeItems());
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                errorVisibility.set(true);
                status.set(error.getStatus());
            } else {
                placeholderVisibility.set(true);
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SearchResponse> observer) {

        }
    };
}
