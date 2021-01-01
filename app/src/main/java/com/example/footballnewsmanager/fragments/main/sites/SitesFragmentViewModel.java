package com.example.footballnewsmanager.fragments.main.sites;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.adapters.news.NewsAdapter;
import com.example.footballnewsmanager.adapters.sites.SitesAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.sites.SitesResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.helpers.UserPreferences;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class SitesFragmentViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    public ObservableField<SitesAdapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);

    private SitesAdapter sitesAdapter;

    public void init() {
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
            if (error instanceof SingleMessageError) {
                String message = ((SingleMessageError) error).getMessage();
                if (message != null) {
                    if (message.equals("Nie ma już więcej wyników")) {
                        loadingVisibility.set(false);
                    }
                    if (message.equals("Brak wyników")) {
                        loadingVisibility.set(false);
                    }
                }
            }

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SitesResponse> observer) {

        }
    };

}
