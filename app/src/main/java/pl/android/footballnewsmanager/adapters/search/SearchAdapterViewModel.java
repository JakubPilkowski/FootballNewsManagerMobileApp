package pl.android.footballnewsmanager.adapters.search;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import pl.android.footballnewsmanager.activites.search.SearchActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.responses.main.SingleNewsResponse;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.models.SearchResult;
import pl.android.footballnewsmanager.models.SearchType;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class SearchAdapterViewModel {

    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();

    public SearchResult searchResult;
    private Activity activity;

    public void init(SearchResult searchResult, Activity activity){
        this.searchResult = searchResult;
        this.activity = activity;
        imageUrl.set(searchResult.getImgUrl());
        title.set(searchResult.getName().length() > 70 ? searchResult.getName().substring(0,66)+"..." : searchResult.getName());
    }


    public void onClick(){
        if(searchResult.getType().equals(SearchType.NEWS)){
            String token = UserPreferences.get().getAuthToken();
            Long siteId =  Long.parseLong(searchResult.getId().split(" ")[0]);
            Long id = Long.parseLong(searchResult.getId().split(" ")[1]);
            Connection.get().setNewsVisited(callback, token, siteId, id);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(searchResult.getNewsUrl()));
            activity.startActivity(intent);
        }
        else {
            Intent intent = new Intent(activity, NewsForTeamActivity.class);
            intent.putExtra("id", Long.parseLong(searchResult.getId()));
            intent.putExtra("name", searchResult.getName());
            intent.putExtra("img", searchResult.getImgUrl());
            intent.putExtra("favourite", searchResult.isFavourite());
            activity.startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS);
        }
    }

    private Callback<SingleNewsResponse> callback = new Callback<SingleNewsResponse>() {
        @Override
        public void onSuccess(SingleNewsResponse newsResponse) {
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                RecyclerView recyclerView = ((SearchActivity)activity).binding.searchActivityRecyclerView;
                Snackbar snackbar = SnackbarHelper.getShortSnackBarFromStatus(recyclerView, error.getStatus());
                snackbar.setAction(R.string.ok, v -> snackbar.dismiss());
                snackbar.show();
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super SingleNewsResponse> observer) {

        }
    };
}
