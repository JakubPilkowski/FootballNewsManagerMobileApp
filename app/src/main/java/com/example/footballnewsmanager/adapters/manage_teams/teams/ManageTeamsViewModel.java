package com.example.footballnewsmanager.adapters.manage_teams.teams;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.User;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageTeamsViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> img = new ObservableField<>();
    public ObservableInt toggleImg = new ObservableInt();

    private UserTeam userTeam;
    private Team team;
    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;
    private RecyclerViewItemsListener<UserTeam> innerRecyclerViewItemsListener;
    private Activity activity;

    public void init(UserTeam userTeam,Activity activity, RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        this.activity = activity;
        this.recyclerViewItemsListener = recyclerViewItemsListener;
        update(userTeam);
    }

    public void init(UserTeam userTeam,Activity activity, RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener
            , RecyclerViewItemsListener<UserTeam> innerRecyclerViewListener)
    {
        this.activity = activity;
        this.recyclerViewItemsListener = recyclerViewItemsListener;
        this.innerRecyclerViewItemsListener = innerRecyclerViewListener;
        update(userTeam);
    }
    public void onTeamClick() {
        Intent intent = new Intent(activity, NewsForTeamActivity.class);
        intent.putExtra("id", team.getId());
        intent.putExtra("name", team.getName());
        intent.putExtra("img", team.getLogoUrl());
        intent.putExtra("favourite", userTeam.isFavourite());
        activity.startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS);
    }

    public void toggleTeam() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleTeam(callback, token, team.getId());
    }

    private Callback<UserTeam> callback = new Callback<UserTeam>() {
        @Override
        public void onSuccess(UserTeam newsUserTeam) {

            Log.d("ManageTeams", "onSuccess: userTeam "+userTeam.isFavourite());
            Log.d("ManageTeams", "onSuccess: newTeam "+ newsUserTeam.isFavourite());
            recyclerViewItemsListener.onChangeItem(userTeam, newsUserTeam);

            if(innerRecyclerViewItemsListener != null){
                innerRecyclerViewItemsListener.onChangeItem(userTeam, newsUserTeam);
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d("ManageTeams", "onSmthWrong: ");
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super UserTeam> observer) {

        }
    };

    public void update(UserTeam userTeam) {
        this.userTeam = userTeam;
        team = userTeam.getTeam();
        name.set(team.getName());
        img.set(team.getLogoUrl());
        toggleImg.set(userTeam.isFavourite() ? R.drawable.ic_add : R.drawable.ic_add_empty);
    }
}
