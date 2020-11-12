package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.responses.main.BaseNewsAdjustment;
import com.example.footballnewsmanager.api.responses.main.NewsExtras;
import com.example.footballnewsmanager.api.responses.main.TeamExtras;
import com.example.footballnewsmanager.models.Team;

import java.util.ArrayList;

public class NewsAdditionalInfoViewModel {


    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> viewTitle = new ObservableField<>();
    private BaseNewsAdjustment newsExtras;
    private Activity activity;

    public void initForNews(BaseNewsAdjustment newsExtras, Activity activity){
        Log.d("News", "TeamExtras" +newsExtras.getTitle());
        this.activity = activity;
        this.newsExtras = newsExtras;
        imageUrl.set(newsExtras.getNews().getImageUrl());
        title.set(newsExtras.getNews().getTitle());
        viewTitle.set(newsExtras.getTitle());
    }

    public void initForTeams(BaseNewsAdjustment teamExtras){
        Log.d("News", "TeamExtras" +teamExtras.getTitle());
        viewTitle.set(teamExtras.getTitle());
    }

    public void onNewsClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(newsExtras.getNews().getNewsUrl()));
        activity.startActivity(intent);
    }
}
