package pl.android.footballnewsmanager.fragments.proposed_settings.sites;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import pl.android.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.responses.sites.SitesResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.UserPreferences;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProposedSitesFragmentViewModel extends BaseViewModel {
    public ObservableField<ProposedSitesAdapter> recyclerViewAdapter = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    public void init(){
        tryAgainListener.set(listener);
        load();
    }

    public void load() {
        itemsVisibility.set(false);
        errorVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedSites(callback,
                token,0);
    }
    private void initItemsView(SitesResponse sitesResponse){
        ProposedSitesAdapter proposedSitesAdapter = new ProposedSitesAdapter();
        proposedSitesAdapter.setItems(sitesResponse.getSites());
        recyclerViewAdapter.set(proposedSitesAdapter);
    }


    private Callback<SitesResponse> callback = new Callback<SitesResponse>() {
        @Override
        public void onSuccess(SitesResponse sitesResponse) {
            loadingVisibility.set(false);
            itemsVisibility.set(true);
            getActivity().runOnUiThread(() -> {
                initItemsView(sitesResponse);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                status.set(error.getStatus());
                errorVisibility.set(true);
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SitesResponse> observer) {

        }
    };
}
