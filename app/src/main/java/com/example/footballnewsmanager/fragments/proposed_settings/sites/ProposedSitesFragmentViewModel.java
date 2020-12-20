package com.example.footballnewsmanager.fragments.proposed_settings.sites;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.ProposedSitesResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.helpers.UserPreferences;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProposedSitesFragmentViewModel extends BaseViewModel {
    public ObservableField<ProposedSitesAdapter> recyclerViewAdapter = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);

    public void init(){
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedSites(callback,
                token,0);

    }


    private void initItemsView(ProposedSitesResponse proposedSitesResponse){
        ProposedSitesAdapter proposedSitesAdapter = new ProposedSitesAdapter();
        proposedSitesAdapter.setItems(proposedSitesResponse.getSites());
        recyclerViewAdapter.set(proposedSitesAdapter);
    }


    private Callback<ProposedSitesResponse> callback = new Callback<ProposedSitesResponse>() {
        @Override
        public void onSuccess(ProposedSitesResponse proposedSitesResponse) {
            loadingVisibility.set(false);
            itemsVisibility.set(true);
            getActivity().runOnUiThread(() -> {
                initItemsView(proposedSitesResponse);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super ProposedSitesResponse> observer) {

        }
    };
}
