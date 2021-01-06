package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;

import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.databinding.NewsItemsPlaceholderBinding;
import com.example.footballnewsmanager.helpers.NewsPlaceholder;

public class NewsAdapterPlaceholderViewModel {

    public void init(Activity activity, NewsItemsPlaceholderBinding binding){
        NewsPlaceholder newsPlaceholder = binding.newsItemsPlaceholder;
        newsPlaceholder.setOnAddTeamsInterface(() -> {
            Intent intent = new Intent(activity, ManageTeamsActivity.class);
            activity.startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS);
        });

    }
}
