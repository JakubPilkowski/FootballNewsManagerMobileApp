package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;

import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;

public class NewsAdapterPlaceholderViewModel {

    private Activity activity;
    private RecyclerViewItemsListener recyclerViewItemsListener;

    public void init(Activity activity, RecyclerViewItemsListener newsAdapterListener){
        this.activity = activity;
        this.recyclerViewItemsListener = newsAdapterListener;
    }

    public void showTeamsFragment(){
        Intent intent = new Intent(activity, ManageTeamsActivity.class);
        activity.startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS);
    }
    public void backToFront(){
        recyclerViewItemsListener.backToFront();
    }
}
