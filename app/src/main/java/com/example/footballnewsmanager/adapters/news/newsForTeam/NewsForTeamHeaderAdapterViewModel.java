package com.example.footballnewsmanager.adapters.news.newsForTeam;

import android.util.Log;
import android.widget.LinearLayout;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.databinding.NewsForTeamHeaderBinding;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsForTeamHeaderAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> img = new ObservableField<>();
    public ObservableField<String> countAll = new ObservableField<>();
    public ObservableField<String> countToday = new ObservableField<>();
    public ObservableField<String> isFavouriteText = new ObservableField<>();
    public ObservableBoolean isFavouriteBackground = new ObservableBoolean();
    public ObservableBoolean loadingButtonVisibility = new ObservableBoolean(false);
    public ObservableBoolean toggleButtonVisibility = new ObservableBoolean(true);


    private Long id;
    private RecyclerViewItemsListener<UserTeam> headerRecyclerViewItemsListener;
    private LinearLayout linearLayout;


    public void init(Long id, String name, String img, boolean isFavourite,
                     Long countAll, Long countToday, RecyclerViewItemsListener<UserTeam> headerRecyclerViewItemsListener,
                     NewsForTeamHeaderBinding binding){
        this.id = id;
        this.headerRecyclerViewItemsListener = headerRecyclerViewItemsListener;
        this.name.set(name);
        this.img.set(img);
        this.countAll.set("Łącznie: "+countAll);
        this.countToday.set("Dzisiaj: "+ countToday);
        linearLayout = binding.newsForTeamHeaderLayout;
        updateFavouriteState(isFavourite);
    }

    public void updateFavouriteState(boolean isFavourite){
        isFavouriteText.set(isFavourite ? "Usuń" : "Dodaj");
        isFavouriteBackground.set(isFavourite);
    }


    public void toggleFavourites(){
        toggleButtonVisibility.set(false);
        loadingButtonVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleTeam(callback, token, id);
    }

    private Callback<UserTeam> callback = new Callback<UserTeam>() {
        @Override
        public void onSuccess(UserTeam newsUserTeam) {
            updateFavouriteState(newsUserTeam.isFavourite());
            loadingButtonVisibility.set(false);
            toggleButtonVisibility.set(true);
            headerRecyclerViewItemsListener.onChangeItem(new UserTeam(), newsUserTeam);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingButtonVisibility.set(false);
            toggleButtonVisibility.set(true);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                Snackbar snackbar = SnackbarHelper.getShortSnackBarFromStatus(linearLayout, error.getStatus());
                snackbar.setAction(R.string.ok, v -> snackbar.dismiss());
                snackbar.show();
            }
            Log.d("ManageTeams", "onSmthWrong: ");
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super UserTeam> observer) {

        }
    };
}
