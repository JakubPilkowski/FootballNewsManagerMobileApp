package pl.android.footballnewsmanager.adapters.news.additionalInfo.teams;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamBinding;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.base.BaseAdapterViewModel;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.models.UserTeam;

public class AdditionalTeamsAdapterViewModel extends BaseAdapterViewModel {


    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> logoUrl = new ObservableField<>();
    public ObservableField<String> isFavouriteText = new ObservableField<>();
    public ObservableBoolean isFavouriteBackground = new ObservableBoolean();
    public ObservableBoolean loadingButtonVisibility = new ObservableBoolean(false);
    public ObservableBoolean toggleButtonVisibility = new ObservableBoolean(true);
    public ObservableBoolean loaded = new ObservableBoolean(false);
    public ObservableField<String> errorText = new ObservableField<>();
    private UserTeam team;
    private Resources resources;
    private Activity activity;
    @Override
    public void init(Object[] values) {
        team = (UserTeam) values[0];
        name.set(team.getTeam().getName());
        AdditionalInfoTeamBinding binding = (AdditionalInfoTeamBinding) values[1];
        activity = (Activity) values[2];
        resources = binding.additionalInfoTeamMainLayout.getResources();
        logoUrl.set(team.getTeam().getLogoUrl());
        updateFavouriteState(team.isFavourite());
    }



    private void updateFavouriteState(boolean isFavourite){
        isFavouriteText.set(resources.getString(isFavourite ? R.string.delete : R.string.add ));
        isFavouriteBackground.set(isFavourite);
    }


    public void toggleFavourites(){
        toggleButtonVisibility.set(false);
        loadingButtonVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleTeam(callback, token, team.getTeam().getId());
    }

    private Callback<UserTeam> callback = new Callback<UserTeam>() {
        @Override
        public void onSuccess(UserTeam newsUserTeam) {
            loaded.set(true);
            team = newsUserTeam;
            updateFavouriteState(newsUserTeam.isFavourite());
            loadingButtonVisibility.set(false);
            toggleButtonVisibility.set(true);
            ((MainActivity)activity).reloadNews();
            ((MainActivity)activity).reloadProfile();
            activity.runOnUiThread(()->{
                String prefix = activity.getString(newsUserTeam.isFavourite() ? R.string.added_team : R.string.remove_team);
                String suffix = activity.getString(newsUserTeam.isFavourite() ? R.string.added_to : R.string.remove_from);
                String teamName = newsUserTeam.getTeam().getName();
                String fullMessage = prefix+teamName+suffix;
                Toast.makeText(activity.getApplicationContext(), fullMessage, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingButtonVisibility.set(false);
            toggleButtonVisibility.set(true);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                loaded.set(false);
                switch (error.getStatus()) {
                    case 598:
                        errorText.set("Brak połączenia z internetem");
                        break;
                    case 408:
                        errorText.set("Zbyt długi czas oczekiwania");
                        break;
                    default:
                        errorText.set("Coś poszło nie tak");
                        break;
                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super UserTeam> observer) {

        }
    };



}
