package pl.android.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.res.Resources;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.responses.BaseResponse;
import pl.android.footballnewsmanager.fragments.main.MainFragment;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsHeaderAdapterViewModel {


    public ObservableField<String> countAll = new ObservableField<>();
    public ObservableField<String> countToday = new ObservableField<>();
    public ObservableBoolean loading = new ObservableBoolean(false);


    private ExtendedRecyclerViewItemsListener extendedRecyclerViewItemsListener;
    private Activity activity;

    public void init(Long countAll, Long countToday, ExtendedRecyclerViewItemsListener extendedRecyclerViewItemsListener, Activity activity) {
        Resources resources = activity.getResources();
        this.countAll.set(resources.getString(R.string.total) + countAll);
        this.countToday.set(resources.getString(R.string.today) + countToday);
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
        this.activity = activity;
    }

    public void onClick() {
        loading.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().markAllAsVisited(callback, token);
    }

    private Callback<BaseResponse> callback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            loading.set(false);
            extendedRecyclerViewItemsListener.onChangeItems();
            ((MainActivity)activity).reloadAllNews();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loading.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                MainFragment mainFragment = ((MainActivity) activity).getMainFragment();
                SnackbarHelper.showDefaultSnackBarFromStatus(mainFragment.binding.mainBottomNavView, error.getStatus());
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };
}
