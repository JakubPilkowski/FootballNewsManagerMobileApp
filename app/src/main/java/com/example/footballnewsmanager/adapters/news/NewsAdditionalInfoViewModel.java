package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.adapters.news.additionalInfo.news.AdditionalNewsAdapter;
import com.example.footballnewsmanager.adapters.news.additionalInfo.teams.AdditionalTeamsAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.requests.news.TeamsFromTagsRequest;
import com.example.footballnewsmanager.api.responses.main.BaseNewsAdjustment;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;
import com.example.footballnewsmanager.databinding.AdditionalInfoNewsBinding;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamsBinding;
import com.example.footballnewsmanager.fragments.proposed_settings.teams.ProposedTeamsFragment;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.NewsTag;
import com.example.footballnewsmanager.models.Tag;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observer;

public class NewsAdditionalInfoViewModel {


    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> viewTitle = new ObservableField<>();
    public ObservableField<String> siteLogo = new ObservableField<>();
    public ObservableField<String> concernTeams = new ObservableField<>();
    private BaseNewsAdjustment newsExtras;
    private Activity activity;


    public ObservableField<RecyclerView.LayoutManager> layoutManager = new ObservableField<>();
    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public BaseNewsAdjustment teamsExtras;

    public void initForNews(BaseNewsAdjustment newsExtras, Activity activity, AdditionalInfoNewsBinding binding) {
        Log.d("News", "TeamExtras" + newsExtras.getTitle());
        this.activity = activity;
        this.newsExtras = newsExtras;
        imageUrl.set(newsExtras.getNews().getImageUrl());
        title.set(newsExtras.getNews().getTitle());
        viewTitle.set(newsExtras.getTitle());
        siteLogo.set(newsExtras.getNews().getSite().getLogoUrl());

        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(binding.additionalInfoNewsRecyclerview.getContext());
        newsLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.set(newsLayoutManager);

        List<Tag> tagList = new ArrayList<>();
        for (NewsTag tag:newsExtras.getNews().getTags()) {
            tagList.add(tag.getTag());
        }

        TeamsFromTagsRequest request = new TeamsFromTagsRequest(tagList);

        String token = UserPreferences.get().getAuthToken();
        Connection.get().findByTags(callback, token, request);

        binding.additionalInfoNewsRecyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xDistance = yDistance = 0f;
                        lastX = ev.getX();
                        lastY = ev.getY();
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float curX = ev.getX();
                        final float curY = ev.getY();
                        xDistance += Math.abs(curX - lastX);
                        yDistance += Math.abs(curY - lastY);
                        lastX = curX;
                        lastY = curY;
                        if(yDistance > xDistance)
                            rv.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Dotyczy: ");
//        for (NewsTag tag : newsExtras.getNews().getTags()) {
//            stringBuilder.append(tag.getTag().getName());
//            if (newsExtras.getNews().getTags().indexOf(tag) + 1 != newsExtras.getNews().getTags().size())
//                stringBuilder.append(", ");
//        }
        concernTeams.set("Dotyczy: ");
    }

    private Callback<ProposedTeamsResponse> callback = new Callback<ProposedTeamsResponse>() {
        @Override
        public void onSuccess(ProposedTeamsResponse proposedTeamsResponse) {
            AdditionalNewsAdapter additionalNewsAdapter = new AdditionalNewsAdapter();
            additionalNewsAdapter.setItems(proposedTeamsResponse.getTeams());
            adapterObservable.set(additionalNewsAdapter);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d("OnSmthWrongNewsAddInfo", error.getError());
        }

        @Override
        protected void subscribeActual(@io.reactivex.rxjava3.annotations.NonNull Observer<? super ProposedTeamsResponse> observer) {

        }
    };


    private float xDistance, yDistance, lastX, lastY;

    public void initForTeams(BaseNewsAdjustment teamExtras, Activity activity, AdditionalInfoTeamsBinding binding) {
        this.activity = activity;
        this.teamsExtras = teamExtras;
        viewTitle.set(teamExtras.getTitle());
        LinearLayoutManager teamsLayoutManager = new LinearLayoutManager(binding.additionalInfoTeamsRecyclerview.getContext());
        teamsLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.set(teamsLayoutManager);


        binding.additionalInfoTeamsRecyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xDistance = yDistance = 0f;
                        lastX = ev.getX();
                        lastY = ev.getY();
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float curX = ev.getX();
                        final float curY = ev.getY();
                        xDistance += Math.abs(curX - lastX);
                        yDistance += Math.abs(curY - lastY);
                        lastX = curX;
                        lastY = curY;
                        if(yDistance > xDistance)
                            rv.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        AdditionalTeamsAdapter additionalTeamsAdapter = new AdditionalTeamsAdapter();
        additionalTeamsAdapter.setItems(teamExtras.getTeams());
        adapterObservable.set(additionalTeamsAdapter);
    }

    public void onNewsClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(newsExtras.getNews().getNewsUrl()));
        activity.startActivity(intent);
    }

    public void onSiteClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(newsExtras.getNews().getSite().getSiteUrl()));
        activity.startActivity(intent);
    }
}
