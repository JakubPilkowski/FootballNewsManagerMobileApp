package com.example.footballnewsmanager.adapters.news.additionalInfo.teams;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamBinding;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

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

    private AdditionalInfoTeamBinding binding;
    private Resources resources;
    @Override
    public void init(Object[] values) {
        team = (UserTeam) values[0];
        name.set(team.getTeam().getName());
        binding = (AdditionalInfoTeamBinding) values[1];
        resources = binding.additionalInfoTeamMainLayout.getResources();
        logoUrl.set(team.getTeam().getLogoUrl());
        updateFavouriteState(team.isFavourite());
    }



    public void updateFavouriteState(boolean isFavourite){
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
//            headerRecyclerViewItemsListener.onChangeItem(new UserTeam(), newsUserTeam);
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
