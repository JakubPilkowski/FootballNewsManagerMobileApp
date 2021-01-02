package com.example.footballnewsmanager.fragments.main.sites;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.adapters.news.NewsAdapter;
import com.example.footballnewsmanager.adapters.sites.SitesAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.sites.SitesResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.UserPreferences;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class SitesFragmentViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    public ObservableField<SitesAdapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    private SitesAdapter sitesAdapter;

    public void init() {
        tryAgainListener.set(listener);
        load();
    }

    private void load() {
        itemsVisibility.set(false);
        errorVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getSites(callback, token, 0);
    }

    private void initItemsView(SitesResponse sitesResponse) {
        sitesAdapter = new SitesAdapter(getActivity());
        sitesAdapter.setItems(sitesResponse.getSites());
        adapterObservable.set(sitesAdapter);
    }

    private Callback<SitesResponse> callback = new Callback<SitesResponse>() {
        @Override
        public void onSuccess(SitesResponse sitesResponse) {
            if (loadingVisibility.get()) {
                loadingVisibility.set(false);
                itemsVisibility.set(true);
                getActivity().runOnUiThread(() -> {
                    Log.d("News", "onSuccessFirst");
                    initItemsView(sitesResponse);
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
//                    loadingVisibility.set(false);
                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SitesResponse> observer) {

        }
    };

}
