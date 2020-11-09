package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.News;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsAdapterViewModel {



    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> likes = new ObservableField<>();
    public ObservableField<String> dislikes = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> siteLogo = new ObservableField<>();
    public ObservableField<String> siteName = new ObservableField<>();


    private News news;
    private Activity activity;

    public void init(News news, Activity activity){
        this.news = news;
        this.activity = activity;
        title.set(news.getTitle());
        imageUrl.set(news.getImageUrl());
        likes.set(String.valueOf(news.getLikes()));
        dislikes.set(String.valueOf(news.getDislikes()));
        date.set("Opublikowano: "+news.getDate());
        siteLogo.set(news.getSite().getLogoUrl());
        siteName.set(news.getSite().getName());
    }

    public void onSiteClick(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(news.getSite().getSiteUrl()));
        activity.startActivity(intent);
    }

    public void onNewsClick(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(news.getNewsUrl()));
        activity.startActivity(intent);
    }

    public void onMoreInfoClick(){
        //navigator.attach(detailsFragment)
    }

    public void onLikeToggle(){
        //String token = UserPreferences.get().getAuthToken();
        //Connection.get().toggleLikes(callback, token, news.getSiteId(), news.getId());
    }

    public void onDislikeToggle(){
    //    String token = UserPreferences.get().getAuthToken();
      //  Connection.get().toggleDisLikes(callback, token, news.getSiteId(), news.getId());
    }

    private Callback<BaseResponse> callback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {

        }

        @Override
        public void onSmthWrong(BaseError error) {

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };
}
