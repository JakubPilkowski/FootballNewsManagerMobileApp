package pl.android.footballnewsmanager.fragments.manage_teams.all_teams;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.adapters.manage_teams.teams.ManageTeamsAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.responses.proposed.TeamsResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ManageTeamsFromLeagueFragmentBinding;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.PaginationScrollListener;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageTeamsFromLeagueViewModel extends BaseViewModel {


    public ObservableField<RecyclerView.Adapter> recyclerViewAdapter = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public RecyclerView recyclerView;
    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    private ManageTeamsAdapter manageTeamsAdapter;
    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;
    private Long id;
    private boolean isLastPage = false;
    private int currentPage = 0;

    public void init(Long id, RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        recyclerView = ((ManageTeamsFromLeagueFragmentBinding) getBinding()).manageTeamsFromLeagueRecyclerView;
        this.id = id;
        tryAgainListener.set(listener);
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        load();
    }

    public void load() {
        currentPage = 0;
        errorVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getTeamsFromLeague(callback, token, id, currentPage);
    }


    private void initTeamsView(TeamsResponse teamsResponse) {
        manageTeamsAdapter = new ManageTeamsAdapter(getActivity());
        manageTeamsAdapter.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
        manageTeamsAdapter.setItems(teamsResponse.getTeams());
        PaginationScrollListener scrollListener = new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                currentPage++;
                paginationLoad();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return manageTeamsAdapter.isLoading;
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapterObservable.set(manageTeamsAdapter);
    }


    private void paginationLoad(){
        manageTeamsAdapter.setLoading(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getTeamsFromLeague(paginationCallback, token, id, currentPage);
    }

    private Callback<TeamsResponse> paginationCallback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                manageTeamsAdapter.setItems(teamsResponse.getTeams());
                isLastPage = teamsResponse.getPages() <= currentPage;
                manageTeamsAdapter.setLoading(false);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                SnackbarHelper.getInfinitiveSnackBarFromStatus(recyclerView, error.getStatus())
                        .setAction(R.string.reload, v -> paginationLoad())
                        .show();
            }
            getActivity().runOnUiThread(() -> {
                isLastPage = true;
                manageTeamsAdapter.setLoading(false);
            });
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };


    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                if (loadingVisibility.get()) {
                    loadingVisibility.set(false);
                    itemsVisibility.set(true);
                    getActivity().runOnUiThread(() -> initTeamsView(teamsResponse));
                }
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                status.set(error.getStatus());
                errorVisibility.set(true);
            } else {
                getActivity().runOnUiThread(() -> {
                    isLastPage = true;
                    manageTeamsAdapter.setLoading(false);
                });
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };
}
