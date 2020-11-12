package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;

import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;

public class NewsAdapterPlaceholderViewModel {

    private Activity activity;
    private NewsRecyclerViewListener newsRecyclerViewListener;

    public void init(Activity activity, NewsRecyclerViewListener newsAdapterListener){
        this.activity = activity;
        this.newsRecyclerViewListener = newsAdapterListener;
    }

    public void showTeamsFragment(){

    }
    public void backToFront(){
        newsRecyclerViewListener.backToFront();
    }
}
