package pl.android.footballnewsmanager.fragments.manage_teams.main.all;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import pl.android.footballnewsmanager.adapters.manage_teams.all.ManageLeaguesAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.responses.manage_teams.LeagueResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageAllTeamsViewModel extends BaseViewModel {

    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;
    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;
    private ManageLeaguesAdapter manageLeaguesAdapter;


    public void init(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener){
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        tryAgainListener.set(listener);
        load();
    }


    public void load(){
        errorVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getLeagues(callback, token);
    }


    private Callback<LeagueResponse> callback = new Callback<LeagueResponse>() {
        @Override
        public void onSuccess(LeagueResponse leagueResponse) {

            if(loadingVisibility.get()){
                loadingVisibility.set(false);
                itemsVisibility.set(true);
                getActivity().runOnUiThread(()->{
                    manageLeaguesAdapter = new ManageLeaguesAdapter(getNavigator());
                    manageLeaguesAdapter.setRecyclerViewItemsListener(extendedRecyclerViewItemsListener);
                    manageLeaguesAdapter.setItems(leagueResponse.getLeagues());
                    adapterObservable.set(manageLeaguesAdapter);
                });
            }
            else{
                getActivity().runOnUiThread(()->{
                    manageLeaguesAdapter.setItems(leagueResponse.getLeagues());
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
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super LeagueResponse> observer) {

        }
    };

}
