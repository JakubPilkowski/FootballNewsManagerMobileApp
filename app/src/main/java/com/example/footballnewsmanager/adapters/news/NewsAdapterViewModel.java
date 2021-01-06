package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.fragments.main.news_info.NewsInfoFragment;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.NewsView;
import com.example.footballnewsmanager.models.UserNews;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsAdapterViewModel {

    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> likes = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> siteLogo = new ObservableField<>();
    public ObservableField<String> siteName = new ObservableField<>();
    public ObservableBoolean likeEnabled = new ObservableBoolean(true);
    public ObservableBoolean heartVisibility = new ObservableBoolean(true);
    public ObservableBoolean loadingHeartVisibility = new ObservableBoolean(false);
    public ObservableInt heartDrawable = new ObservableInt();
    public ObservableInt isBadgeVisited = new ObservableInt();
    public ObservableInt isVisited = new ObservableInt();
    public ObservableBoolean loadingMark = new ObservableBoolean(false);
    public ObservableField<Drawable> highlightedVisited = new ObservableField<>();
    public ObservableBoolean markEnabled = new ObservableBoolean(false);
    public ObservableBoolean isMarked = new ObservableBoolean();
    public UserNews news;
    private News newsDetails;
    private Activity activity;
    private ExtendedRecyclerViewItemsListener<UserNews> listener;
    private BadgeListener badgeListener;
    private NewsView newsView;

    public void init(UserNews news, Activity activity, ExtendedRecyclerViewItemsListener<UserNews> listener,
                     BadgeListener badgeListener, NewsView newsView) {
        this.badgeListener = badgeListener;
        this.activity = activity;
        this.listener = listener;
        this.newsView = newsView;
        update(news);
    }

    public void update(UserNews news) {
        this.news = news;
        this.newsDetails = news.getNews();
        isVisited.set(news.isVisited() ? activity.getResources().getColor(R.color.colorVisited) : activity.getResources().getColor(R.color.colorTextPrimary));
        isBadgeVisited.set(!news.isBadged() ? R.drawable.not_visited : R.drawable.visited);
        markEnabled.set(!news.isVisited());
        heartDrawable.set(news.isLiked() ? R.drawable.heart_with_ripple : R.drawable.heart_empty_with_ripple);
        isMarked.set(!news.isBadged());
        title.set(newsDetails.getTitle().length() > 40 ? newsDetails.getTitle().substring(0, 37) + "..." : newsDetails.getTitle());
        imageUrl.set(newsDetails.getImageUrl());
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
    }

    public void onMoreInfoClick() {
        ((MainActivity) activity).navigator.attachAdd(NewsInfoFragment.newInstance(news), NewsInfoFragment.TAG);
    }

    public void onLikeToggle() {
        likesToggle();
        heartVisibility.set(false);
        loadingHeartVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleLikes(callback, token, newsDetails.getSiteId(), newsDetails.getId());
    }

    public void onMarkClick() {
//        markEnabled.set(!markEnabled.get());
        loadingMark.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().setNewsBadged(callback, token, newsDetails.getSiteId(), newsDetails.getId());
    }

    private void likesToggle() {
        likeEnabled.set(!likeEnabled.get());
    }


    private Callback<SingleNewsResponse> callback = new Callback<SingleNewsResponse>() {
        @Override
        public void onSuccess(SingleNewsResponse newsResponse) {
            activity.runOnUiThread(() -> {
                listener.onChangeItem(news, newsResponse.getNews());
                if (newsView.equals(NewsView.SELECTED))
                    ((MainActivity) activity).updateNews(news, newsResponse.getNews()
                            , NewsView.ALL);
                else if(newsView.equals(NewsView.ALL)){
                    ((MainActivity) activity).updateNews(news, newsResponse.getNews()
                            , NewsView.SELECTED);
                }
            });
            switch (newsResponse.getType()) {
                case LIKE:
                    likesToggle();
                    loadingHeartVisibility.set(false);
                    heartVisibility.set(true);
                    ((MainActivity) activity).reloadProfile();
                    break;
                case MARK:
                    loadingMark.set(false);
                    badgeListener.onBadgeChange();
                    break;
                case NEWS:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(newsDetails.getNewsUrl()));
                    activity.startActivity(intent);
                    badgeListener.onBadgeChange();
                    break;
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {

            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                MainFragment mainFragment = ((MainActivity) activity).getMainFragment();
                SnackbarHelper.getShortSnackBarFromStatus(mainFragment.binding.mainBottomNavView, error.getStatus())
                        .setAnchorView(mainFragment.binding.mainBottomNavView)
                        .show();
            }
            if (!likeEnabled.get()) {
                likesToggle();
            }
            loadingHeartVisibility.set(false);
            heartVisibility.set(true);
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SingleNewsResponse> observer) {

        }
    };
}
