package com.example.footballnewsmanager.adapters.search;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.news_for_team.NewsForTeamActivity;
import com.example.footballnewsmanager.activites.search.SearchActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.SearchResult;
import com.example.footballnewsmanager.models.SearchType;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class SearchAdapterViewModel {

    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();

    private SearchResult searchResult;
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
            activity.startActivity(intent);
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
