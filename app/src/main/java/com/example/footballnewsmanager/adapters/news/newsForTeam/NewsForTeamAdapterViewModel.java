package com.example.footballnewsmanager.adapters.news.newsForTeam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.UserNews;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsForTeamAdapterViewModel {

    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public UserNews news;
    private News newsDetails;
    private Activity activity;
    private RecyclerViewItemsListener listener;

    public void init(UserNews news, Activity activity, RecyclerViewItemsListener listener) {
        this.activity = activity;
        this.listener = listener;
        update(news);
    }
    public void update(UserNews news) {
        this.news = news;
        this.newsDetails = news.getNews();
        title.set(newsDetails.getTitle().length() > 30 ? newsDetails.getTitle().substring(0, 27) + "..." : newsDetails.getTitle());
        imageUrl.set(newsDetails.getImageUrl());
        date.set("Dodano: " + newsDetails.getDate().split("T")[0]);
    }

    public void onNewsClick() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().setNewsVisited(callback, token, newsDetails.getSiteId(), newsDetails.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(newsDetails.getNewsUrl()));
        activity.startActivity(intent);
    }
    private Callback<SingleNewsResponse> callback = new Callback<SingleNewsResponse>() {
        @Override
        public void onSuccess(SingleNewsResponse newsResponse) {
            activity.runOnUiThread(() -> {
                listener.onChangeItem(news, newsResponse.getNews());
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error instanceof SingleMessageError) {
                Log.d("News", ((SingleMessageError) error).getMessage());
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SingleNewsResponse> observer) {

        }
    };
}
