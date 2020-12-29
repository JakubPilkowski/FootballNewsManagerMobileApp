package com.example.footballnewsmanager.adapters.news.additionalInfo.teams;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.base.BaseAdapterViewModel;
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
    private UserTeam team;

    @Override
    public void init(Object[] values) {
        team = (UserTeam) values[0];
        name.set(team.getTeam().getName());
        logoUrl.set(team.getTeam().getLogoUrl());
        updateFavouriteState(team.isFavourite());
    }



    public void updateFavouriteState(boolean isFavourite){
        isFavouriteText.set(isFavourite ? "Usuń" : "Dodaj");
        isFavouriteBackground.set(isFavourite);
    }


    public void toggleFavourites(){
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleTeam(callback, token, team.getTeam().getId());
    }

    private Callback<UserTeam> callback = new Callback<UserTeam>() {
        @Override
        public void onSuccess(UserTeam newsUserTeam) {
            team = newsUserTeam;
            updateFavouriteState(newsUserTeam.isFavourite());
//            headerRecyclerViewItemsListener.onChangeItem(new UserTeam(), newsUserTeam);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d("ManageTeams", "onSmthWrong: ");
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super UserTeam> observer) {

        }
    };



}
