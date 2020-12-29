package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;

import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;

public class NewsAdapterPlaceholderViewModel {

    private Activity activity;
    private RecyclerViewItemsListener recyclerViewItemsListener;

    public void init(Activity activity, RecyclerViewItemsListener newsAdapterListener){
        this.activity = activity;
        this.recyclerViewItemsListener = newsAdapterListener;
    }

    public void showTeamsFragment(){

    }
    public void backToFront(){
        recyclerViewItemsListener.backToFront();
    }
}
