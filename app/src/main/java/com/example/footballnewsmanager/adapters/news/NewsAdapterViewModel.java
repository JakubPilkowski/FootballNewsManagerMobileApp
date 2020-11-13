package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.databinding.NewsHighlightedLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsLayoutBinding;
import com.example.footballnewsmanager.fragments.main.news_info.NewsInfoFragment;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;
import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.UserNews;

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
    public ObservableBoolean likeEnabled = new ObservableBoolean(true);
    public ObservableBoolean dislikeEnabled = new ObservableBoolean(true);
    public ObservableInt heartDrawable = new ObservableInt();
    public ObservableInt heartbreakDrawable = new ObservableInt();
    public ObservableInt heartAnimation = new ObservableInt();
    public ObservableInt heartbreakAnimation = new ObservableInt();

    private UserNews news;
    private News newsDetails;
    private Activity activity;
    private NewsRecyclerViewListener listener;

    public void init(UserNews news, Activity activity, NewsRecyclerViewListener listener) {
        this.activity = activity;
        this.listener = listener;
        update(news);
    }

    void update(UserNews news){
        this.news = news;
        this.newsDetails = news.getNews();

        if(news.isLiked()){
            heartDrawable.set(R.drawable.heart_with_ripple);
        }
        else{
            heartDrawable.set(R.drawable.heart_empty_with_ripple);
        }
        if(news.isDisliked()){
            heartbreakDrawable.set(R.drawable.heartbreak_with_ripple);
        }
        else{
            heartbreakDrawable.set(R.drawable.heartbreak_empty_with_ripple);
        }

        title.set(newsDetails.getTitle());
        imageUrl.set(newsDetails.getImageUrl());
        setLikes();
        setDislikes();
        date.set("Dodano: " + newsDetails.getDate());
        siteLogo.set(newsDetails.getSite().getLogoUrl());
        siteName.set(newsDetails.getSite().getName());
    }


    private void setLikes() {
        if(newsDetails.getLikes()<=999){
            likes.set(String.valueOf(newsDetails.getLikes()));
        }
        else{
            likes.set("999+");
        }
    }
    private void setDislikes(){
        if(newsDetails.getDislikes()<=999){
            dislikes.set(String.valueOf(newsDetails.getDislikes()));
        }
        else{
            dislikes.set("999+");
        }
    }

    public void onSiteClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(newsDetails.getSite().getSiteUrl()));
        activity.startActivity(intent);
    }

    public void onNewsClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(newsDetails.getNewsUrl()));
        activity.startActivity(intent);
    }

    public void onMoreInfoClick() {
        ((MainActivity) activity).navigator.attachAdd(NewsInfoFragment.newInstance(news), NewsInfoFragment.TAG);
    }

    public void onLikeToggle() {
        likesToggle();
//        View view = binding.newsHeart;
//        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_repeat));
        heartAnimation.set(R.anim.scale_repeat);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleLikes(callback, token, newsDetails.getSiteId(), newsDetails.getId());
    }

    public void onDislikeToggle() {
        likesToggle();
//        View view = binding.newsHeartBreak;
//        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_repeat));
        heartbreakAnimation.set(R.anim.scale_repeat);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleDisLikes(callback, token, newsDetails.getSiteId(), newsDetails.getId());
    }

    private void likesToggle(){
        likeEnabled.set(!likeEnabled.get());
        dislikeEnabled.set(!dislikeEnabled.get());
    }

    private Callback<SingleNewsResponse> callback = new Callback<SingleNewsResponse>() {
        @Override
        public void onSuccess(SingleNewsResponse newsResponse) {
            activity.runOnUiThread(()->{
                listener.onChange(news, newsResponse.getNews());
            });
            likesToggle();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error instanceof SingleMessageError) {
                Log.d("News", ((SingleMessageError) error).getMessage());
            }
            likesToggle();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SingleNewsResponse> observer) {

        }
    };
}
