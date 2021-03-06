package pl.android.footballnewsmanager.fragments.manage_teams.main.selected;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import pl.android.footballnewsmanager.adapters.manage_teams.selected.SelectedTeamsAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.errors.SingleMessageError;
import pl.android.footballnewsmanager.api.responses.proposed.TeamsResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageSelectedTeamsViewModel extends BaseViewModel {

    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public SelectedTeamsAdapter selectedTeamsAdapter;
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableBoolean placeholderVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;



    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    public void init(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        this.tryAgainListener.set(listener);
        load();
    }

    public void load() {
        errorVisibility.set(false);
        itemsVisibility.set(false);
        placeholderVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getFavouriteTeams(callback, token);
    }

    private void initTeamsView(TeamsResponse teamsResponse){
        selectedTeamsAdapter = new SelectedTeamsAdapter(getActivity());
        selectedTeamsAdapter.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
        adapterObservable.set(selectedTeamsAdapter);
        selectedTeamsAdapter.setItems(teamsResponse.getTeams());
    }

    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                if(loadingVisibility.get()){
                    loadingVisibility.set(false);
                    itemsVisibility.set(true);
                    getActivity().runOnUiThread(()->{
                        initTeamsView(teamsResponse);
                    });
                }
                else{
                    getActivity().runOnUiThread(()-> {
                        selectedTeamsAdapter.setItems(teamsResponse.getTeams());
                    });
                }
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            placeholderVisibility.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                itemsVisibility.set(false);
                errorVisibility.set(true);
                status.set(error.getStatus());
            }
            else {
                if (error instanceof SingleMessageError) {
                    String message = ((SingleMessageError) error).getMessage();
                    if (message.equals("Nie ma już więcej wyników")) {
                        placeholderVisibility.set(true);
                        itemsVisibility.set(true);
                        selectedTeamsAdapter = new SelectedTeamsAdapter(getActivity());
                        selectedTeamsAdapter.setExtendedRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
                        adapterObservable.set(selectedTeamsAdapter);
                    }
                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };
}
