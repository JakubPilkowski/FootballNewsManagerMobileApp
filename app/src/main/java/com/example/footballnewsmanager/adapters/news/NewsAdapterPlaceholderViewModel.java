package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;

import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;

public class NewsAdapterPlaceholderViewModel {

    private Activity activity;
    private ExtendedRecyclerViewItemsListener extendedRecyclerViewItemsListener;

    public void init(Activity activity, ExtendedRecyclerViewItemsListener newsAdapterListener){
        this.activity = activity;
        this.extendedRecyclerViewItemsListener = newsAdapterListener;
    }

    public void showTeamsFragment(){
        Intent intent = new Intent(activity, ManageTeamsActivity.class);
        activity.startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS);
    }
    public void backToFront(){
        extendedRecyclerViewItemsListener.backToFront();
    }
}
