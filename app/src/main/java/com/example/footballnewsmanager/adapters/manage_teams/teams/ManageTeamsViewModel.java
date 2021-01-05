package com.example.footballnewsmanager.adapters.manage_teams.teams;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.databinding.ManageTeamItemBinding;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ManageTeamsViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> img = new ObservableField<>();
    public ObservableInt toggleImg = new ObservableInt();
    public ObservableBoolean loadingButtonVisibility = new ObservableBoolean(false);
    public ObservableBoolean toggleButtonVisibility = new ObservableBoolean(true);

    private UserTeam userTeam;
    private Team team;
    private ExtendedRecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;
    private ExtendedRecyclerViewItemsListener<UserTeam> innerExtendedRecyclerViewItemsListener;
    private Activity activity;
    private ManageTeamItemBinding binding;
    private LinearLayout mainLayout;

    public void init(UserTeam userTeam, Activity activity, ExtendedRecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener,
                     ManageTeamItemBinding binding) {
        this.activity = activity;
        this.binding = binding;
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        mainLayout = binding.manageTeamMainLayout;
        update(userTeam);
    }

    public void init(UserTeam userTeam, Activity activity, ExtendedRecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener
            , ExtendedRecyclerViewItemsListener<UserTeam> innerRecyclerViewListener, ManageTeamItemBinding binding)
    {
        this.activity = activity;
        this.binding = binding;
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        this.innerExtendedRecyclerViewItemsListener = innerRecyclerViewListener;
        mainLayout = binding.manageTeamMainLayout;
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
        toggleButtonVisibility.set(false);
        loadingButtonVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleTeam(callback, token, team.getId());
    }

    private Callback<UserTeam> callback = new Callback<UserTeam>() {
        @Override
        public void onSuccess(UserTeam newsUserTeam) {
            loadingButtonVisibility.set(false);
            toggleButtonVisibility.set(true);
            extendedRecyclerViewItemsListener.onChangeItem(userTeam, newsUserTeam);
            if(innerExtendedRecyclerViewItemsListener != null){
                innerExtendedRecyclerViewItemsListener.onChangeItem(userTeam, newsUserTeam);
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingButtonVisibility.set(false);
            toggleButtonVisibility.set(true);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                SnackbarHelper.showDefaultSnackBarFromStatus(mainLayout,error.getStatus());
            }
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
