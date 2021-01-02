package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsHeaderAdapterViewModel {


    public ObservableField<String> countAll = new ObservableField<>();
    public ObservableField<String> countToday = new ObservableField<>();
    public ObservableBoolean loading = new ObservableBoolean(false);


    private RecyclerViewItemsListener recyclerViewItemsListener;
    private Activity activity;

    public void init(Long countAll, Long countToday, RecyclerViewItemsListener recyclerViewItemsListener, Activity activity) {
        this.countAll.set("Łącznie: " + countAll);
        this.countToday.set("Dzisiaj: " + countToday);
        this.recyclerViewItemsListener = recyclerViewItemsListener;
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
            recyclerViewItemsListener.onChangeItems();
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
