package pl.android.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;

import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.databinding.NewsItemsPlaceholderBinding;
import pl.android.footballnewsmanager.helpers.NewsPlaceholder;

public class NewsAdapterPlaceholderViewModel {

    public void init(Activity activity, NewsItemsPlaceholderBinding binding){
        NewsPlaceholder newsPlaceholder = binding.newsItemsPlaceholder;
        newsPlaceholder.setOnAddTeamsInterface(() -> {
            Intent intent = new Intent(activity, ManageTeamsActivity.class);
            activity.startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS);
        });

    }
}
