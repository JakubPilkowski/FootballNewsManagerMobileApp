package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.fragments.main.news_info.NewsInfoFragment;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.UserNews;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsAdapterViewModel {

    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> likes = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> siteLogo = new ObservableField<>();
    public ObservableField<String> siteName = new ObservableField<>();
    public ObservableBoolean likeEnabled = new ObservableBoolean(true);
    public ObservableInt heartDrawable = new ObservableInt();
    public ObservableInt isBadgeVisited = new ObservableInt();
    public ObservableInt isVisited = new ObservableInt();
    public ObservableField<Drawable> highlightedVisited = new ObservableField<>();
    public UserNews news;
    private News newsDetails;
    private Activity activity;
    private RecyclerViewItemsListener<UserNews> listener;
    private BadgeListener badgeListener;


    public void init(UserNews news, Activity activity, RecyclerViewItemsListener<UserNews> listener, BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
        this.activity = activity;
        this.listener = listener;
        update(news);
    }

    public void update(UserNews news) {
        this.news = news;
        this.newsDetails = news.getNews();
        if (news.getNews().isHighlighted()) {
            highlightedVisited.set(news.isVisited() ? activity.getResources().getDrawable(R.drawable.news_item_highlighted_visited_background_top) : activity.getResources().getDrawable(R.drawable.news_item_highlighted_background_top));
        } else {
            isVisited.set(news.isVisited() ? activity.getResources().getColor(R.color.colorVisited) : activity.getResources().getColor(R.color.colorTextPrimary));
        }

        isBadgeVisited.set(!news.isBadged() ? R.drawable.not_visited : R.drawable.visited);
        heartDrawable.set(news.isLiked() ? R.drawable.heart_with_ripple : R.drawable.heart_empty_with_ripple);

        title.set(newsDetails.getTitle().length() > 40 ? newsDetails.getTitle().substring(0, 37) + "..." : newsDetails.getTitle());
        imageUrl.set(newsDetails.getImageUrl());
        date.set("Dodano: " + newsDetails.getDate());
        siteLogo.set(newsDetails.getSite().getLogoUrl());
        siteName.set(newsDetails.getSite().getName());
    }


    public void onSiteClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(newsDetails.getSite().getSiteUrl()));
        activity.startActivity(intent);
    }

    public void onNewsClick() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().setNewsVisited(callback, token, newsDetails.getSiteId(), newsDetails.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(newsDetails.getNewsUrl()));
        activity.startActivity(intent);
    }

    public void onMoreInfoClick() {
        ((MainActivity) activity).navigator.attachAdd(NewsInfoFragment.newInstance(news), NewsInfoFragment.TAG);
    }

    public void onLikeToggle() {
        likesToggle();
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleLikes(callback, token, newsDetails.getSiteId(), newsDetails.getId());
    }

    private void likesToggle() {
        likeEnabled.set(!likeEnabled.get());
    }


    private Callback<SingleNewsResponse> callback = new Callback<SingleNewsResponse>() {
        @Override
        public void onSuccess(SingleNewsResponse newsResponse) {
            activity.runOnUiThread(() -> {
                listener.onChangeItem(news, newsResponse.getNews());
            });
            if (!newsResponse.message.equals("Odwiedzono wiadomość"))
                likesToggle();
            else {
                badgeListener.onBadgeChange();
            }
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
