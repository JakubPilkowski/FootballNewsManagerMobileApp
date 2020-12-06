package com.example.footballnewsmanager.adapters.news;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsHeaderAdapterViewModel {


    public ObservableField<String> countAll = new ObservableField<>();
    public ObservableField<String> countToday = new ObservableField<>();

    private NewsRecyclerViewListener newsRecyclerViewListener;
    public void init(Long countAll, Long countToday, NewsRecyclerViewListener newsRecyclerViewListener) {
        this.countAll.set("Łącznie: "+countAll);
        this.countToday.set("Dzisiaj: "+ countToday);
        this.newsRecyclerViewListener = newsRecyclerViewListener;
    }

    public void onClick(){
        String token = UserPreferences.get().getAuthToken();
        Connection.get().markAllAsVisited(callback, token);
        ProgressDialog.get().show();
    }

    private Callback<BaseResponse> callback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            newsRecyclerViewListener.onChangeItems();
        }

        @Override
        public void onSmthWrong(BaseError error) {

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };
}
