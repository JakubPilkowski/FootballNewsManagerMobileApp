package pl.android.footballnewsmanager.fragments.main.sites;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.SitesFragmentBinding;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.adapters.sites.SitesAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.responses.sites.SitesResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.fragments.main.MainFragment;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;

public class SitesFragmentViewModel extends BaseViewModel {
    public ObservableField<SitesAdapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt swipeRefreshColor = new ObservableInt(R.color.colorPrimary);
    public ObservableField<SwipeRefreshLayout.OnRefreshListener> swipeRefreshListenerObservable = new ObservableField<>();
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;
    private SwipeRefreshLayout swipeRefreshLayout;

    public void init() {
        swipeRefreshLayout = ((SitesFragmentBinding) getBinding()).siteSwipeRefresh;
        tryAgainListener.set(listener);
        swipeRefreshListenerObservable.set(this::refresh);
        load();
    }

    private void load() {
        itemsVisibility.set(false);
        errorVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getSites(callback, token, 0);
    }

    private void refresh() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getSites(callback, token, 0);
    }

    private void initItemsView(SitesResponse sitesResponse) {
        SitesAdapter sitesAdapter = new SitesAdapter(getActivity());
        sitesAdapter.setItems(sitesResponse.getSites());
        adapterObservable.set(sitesAdapter);
    }

    private Callback<SitesResponse> callback = new Callback<SitesResponse>() {
        @Override
        public void onSuccess(SitesResponse sitesResponse) {
                loadingVisibility.set(false);
                itemsVisibility.set(true);
                getActivity().runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    initItemsView(sitesResponse);
                });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                if (swipeRefreshLayout.isRefreshing()) {
                    itemsVisibility.set(true);
                    getActivity().runOnUiThread(() -> {
                        swipeRefreshLayout.setRefreshing(false);
                    });
                    MainFragment mainFragment = ((MainActivity) getActivity()).getMainFragment();
                    SnackbarHelper.showDefaultSnackBarFromStatus(mainFragment.binding.mainBottomNavView, error.getStatus());
                } else {
                    itemsVisibility.set(false);
                    status.set(error.getStatus());
                    errorVisibility.set(true);

                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SitesResponse> observer) {

        }
    };

}
