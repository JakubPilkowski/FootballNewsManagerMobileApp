package pl.android.footballnewsmanager.adapters.manage_teams.teams;

import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.ManageTeamItemBinding;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.Team;
import pl.android.footballnewsmanager.models.UserTeam;

public class ManageTeamsViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> img = new ObservableField<>();
    public ObservableInt toggleImg = new ObservableInt();
    public ObservableBoolean loadingButtonVisibility = new ObservableBoolean(false);
    public ObservableBoolean toggleButtonVisibility = new ObservableBoolean(true);

    private UserTeam userTeam;
    private Team team;
    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;
    private RecyclerViewItemsListener<UserTeam> innerExtendedRecyclerViewItemsListener;
    private Activity activity;
    private LinearLayout mainLayout;

    public void init(UserTeam userTeam, Activity activity, RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener,
                     ManageTeamItemBinding binding) {
        this.activity = activity;
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        mainLayout = binding.manageTeamMainLayout;
        update(userTeam);
    }

    public void init(UserTeam userTeam, Activity activity, RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener
            , RecyclerViewItemsListener<UserTeam> innerRecyclerViewListener, ManageTeamItemBinding binding)
    {
        this.activity = activity;
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
