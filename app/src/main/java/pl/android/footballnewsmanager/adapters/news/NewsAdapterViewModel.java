package pl.android.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.responses.main.SingleNewsResponse;
import pl.android.footballnewsmanager.fragments.main.MainFragment;
import pl.android.footballnewsmanager.fragments.main.news_info.NewsInfoFragment;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.BadgeListener;
import pl.android.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import pl.android.footballnewsmanager.models.News;
import pl.android.footballnewsmanager.models.NewsView;
import pl.android.footballnewsmanager.models.UserNews;

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
    public ObservableBoolean markEnabled = new ObservableBoolean(false);
    public ObservableBoolean isMarked = new ObservableBoolean();
    public ObservableBoolean infoEnabled = new ObservableBoolean(true);
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
        infoEnabled.set(false);
        ((MainActivity) activity).navigator.attachAdd(NewsInfoFragment.newInstance(news), NewsInfoFragment.TAG);
        infoEnabled.set(true);
    }

    public void onLikeToggle() {
        likeEnabled.set(!likeEnabled.get());
        heartVisibility.set(false);
        loadingHeartVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().toggleLikes(callback, token, newsDetails.getSiteId(), newsDetails.getId());
    }

    public void onMarkClick() {
        loadingMark.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().setNewsBadged(callback, token, newsDetails.getSiteId(), newsDetails.getId());
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
                    likeEnabled.set(!likeEnabled.get());
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
                likeEnabled.set(!likeEnabled.get());
            }
            loadingHeartVisibility.set(false);
            heartVisibility.set(true);
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SingleNewsResponse> observer) {

        }
    };
}
