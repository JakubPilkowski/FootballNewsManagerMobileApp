package pl.android.footballnewsmanager.adapters.news.newsForTeam;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.NewsForTeamHeaderBinding;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.ToastManager;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.UserTeam;

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
    private RecyclerViewItemsListener<UserTeam> headerExtendedRecyclerViewItemsListener;
    private LinearLayout linearLayout;
    private Resources resources;
    private Activity activity;

    public void init(Long id, String name, String img, boolean isFavourite,
                     Long countAll, Long countToday, RecyclerViewItemsListener<UserTeam> headerExtendedRecyclerViewItemsListener,
                     NewsForTeamHeaderBinding binding, Activity activity){
        linearLayout = binding.newsForTeamHeaderLayout;
        resources = linearLayout.getResources();
        this.id = id;
        this.headerExtendedRecyclerViewItemsListener = headerExtendedRecyclerViewItemsListener;
        this.name.set(name);
        this.img.set(img);
        this.countAll.set(resources.getString(R.string.total)+countAll);
        this.countToday.set(resources.getString(R.string.today)+ countToday);
        this.activity = activity;
        updateFavouriteState(isFavourite);
    }

    public void updateFavouriteState(boolean isFavourite){
        isFavouriteText.set(resources.getString(isFavourite ? R.string.delete : R.string.add ));
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
            headerExtendedRecyclerViewItemsListener.onChangeItem(new UserTeam(), newsUserTeam);
            activity.runOnUiThread(()->{
                String prefix = activity.getString(newsUserTeam.isFavourite() ? R.string.added_team : R.string.remove_team);
                String suffix = activity.getString(newsUserTeam.isFavourite() ? R.string.added_to : R.string.remove_from);
                String teamName = newsUserTeam.getTeam().getName();
                String fullMessage = prefix+teamName+suffix;
                ToastManager.get().show(fullMessage);
            });
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
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super UserTeam> observer) {

        }
    };
}
