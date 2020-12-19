package com.example.footballnewsmanager.activites.search;

import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.search.SearchAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.search.SearchResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivitySearchBinding;
import com.example.footballnewsmanager.helpers.KeyboardHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchActivityViewModel extends BaseViewModel {

    public ObservableField<SearchAdapter> searchAdapterObservable = new ObservableField<>();
    private SearchAdapter searchAdapter;

    public CompositeDisposable disposable = new CompositeDisposable();


    public void init() {
        SearchView searchView = ((ActivitySearchBinding) getBinding()).searchActivitySearchView;
        RecyclerView recyclerView  = ((ActivitySearchBinding)getBinding()).searchActivityRecyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@androidx.annotation.NonNull RecyclerView recyclerView, int newState) {
                Log.d("Search", "onScrollStateChanged: ");
                KeyboardHelper.hideKeyboard(getActivity());
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
                Log.d("Search", "onScrolled: ");
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        searchView.setIconified(false);
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> Log.d("Search", "onFocusChange: "));

        Observable<String> observableQueryText = Observable
                .create(emitter -> searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
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
                        String token = UserPreferences.get().getAuthToken();
                        Connection.get().getQueryResults(callback, token, newText);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        searchAdapter = new SearchAdapter();
        searchAdapter.setActivity(getActivity());
        searchAdapterObservable.set(searchAdapter);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getQueryResults(callback, token, "");
    }


    private Callback<SearchResponse> callback = new Callback<SearchResponse>() {
        @Override
        public void onSuccess(SearchResponse searchResponse) {
            Log.d("Search", "success");
            getActivity().runOnUiThread(() -> searchAdapter.setItems(searchResponse.getResults()));
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d("Search", "onSmthWrong: ");
            getActivity().runOnUiThread(() -> searchAdapter.removeItems());
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SearchResponse> observer) {

        }
    };
}
