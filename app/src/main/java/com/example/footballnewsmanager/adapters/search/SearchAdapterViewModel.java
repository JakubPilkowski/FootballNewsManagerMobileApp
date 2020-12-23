package com.example.footballnewsmanager.adapters.search;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.SearchResult;
import com.example.footballnewsmanager.models.SearchType;

import java.util.Observable;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class SearchAdapterViewModel {

    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();

    private SearchResult searchResult;
    private Activity activity;

    public void init(SearchResult searchResult, Activity activity){
        this.searchResult = searchResult;
        this.activity = activity;
        imageUrl.set(searchResult.getImgUrl());
        title.set(searchResult.getName().length() > 70 ? searchResult.getName().substring(0,66)+"..." : searchResult.getName());
    }


    public void onClick(){
        if(searchResult.getType().equals(SearchType.NEWS)){
            String token = UserPreferences.get().getAuthToken();
            Long siteId =  Long.parseLong(searchResult.getId().split(" ")[0]);
            Long id = Long.parseLong(searchResult.getId().split(" ")[1]);
            Connection.get().setNewsVisited(callback, token, siteId, id);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(searchResult.getNewsUrl()));
            activity.startActivity(intent);
        }
        else {
            Intent intent = new Intent(activity, NewsForTeamActivity.class);
            intent.putExtra("id", Long.parseLong(searchResult.getId()));
            intent.putExtra("name", searchResult.getName());
            intent.putExtra("img", searchResult.getImgUrl());
            activity.startActivity(intent);
        }
    }

    private Callback<SingleNewsResponse> callback = new Callback<SingleNewsResponse>() {
        @Override
        public void onSuccess(SingleNewsResponse newsResponse) {
            Log.d("SearchAdapter", "onSuccess:");
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error instanceof SingleMessageError) {
                Log.d("SearchAdapter", ((SingleMessageError) error).getMessage());
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SingleNewsResponse> observer) {

        }
    };
}
