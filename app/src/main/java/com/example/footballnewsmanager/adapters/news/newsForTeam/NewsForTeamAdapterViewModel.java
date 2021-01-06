package com.example.footballnewsmanager.adapters.news.newsForTeam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.databinding.NewsForTeamItemLayoutBinding;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.UserNews;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsForTeamAdapterViewModel {

    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public UserNews news;
    private News newsDetails;
    private Activity activity;
    private NewsForTeamItemLayoutBinding binding;

    public void init(UserNews news, Activity activity, NewsForTeamItemLayoutBinding binding) {
        this.activity = activity;
        this.binding = binding;
        update(news);
    }

    private void update(UserNews news) {
        this.news = news;
        this.newsDetails = news.getNews();
        title.set(newsDetails.getTitle().length() > 30 ? newsDetails.getTitle().substring(0, 27) + "..." : newsDetails.getTitle());
        imageUrl.set(newsDetails.getImageUrl());
        date.set(activity.getResources().getString(R.string.added) + newsDetails.getDate().split("T")[0]);
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

        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                Snackbar snackbar = SnackbarHelper.getShortSnackBarFromStatus(binding.newsForTeamItemMainLayout, error.getStatus());
                snackbar.setAction(R.string.ok, v -> snackbar.dismiss());
                snackbar.show();
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SingleNewsResponse> observer) {

        }
    };
}
