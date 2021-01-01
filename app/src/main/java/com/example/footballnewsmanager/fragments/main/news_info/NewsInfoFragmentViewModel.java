package com.example.footballnewsmanager.fragments.main.news_info;

import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news_info.NewsInfoAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.requests.news.TeamsFromTagsRequest;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.NewsInfoFragmentBinding;
import com.example.footballnewsmanager.databinding.NewsInfoTeamLayoutBinding;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.NewsTag;
import com.example.footballnewsmanager.models.Tag;
import com.example.footballnewsmanager.models.UserNews;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsInfoFragmentViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel


    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);

    private UserNews news;
    private NewsInfoAdapter newsInfoAdapter;
    private List<Tag> tags = new ArrayList<>();
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    public void init(UserNews news) {
        if (news != null) {
            this.news = news;
            tryAgainListener.set(listener);
            for (NewsTag newsTag : news.getNews().getTags()) {
                tags.add(newsTag.getTag());
            }
            load();
        }
    }

    public void initItemsView(TeamsResponse teamsResponse) {
        if (newsInfoAdapter == null) {
            newsInfoAdapter = new NewsInfoAdapter(getActivity());
            adapterObservable.set(newsInfoAdapter);
            newsInfoAdapter.setUserNews(news);
        }
        newsInfoAdapter.setItems(teamsResponse.getTeams());
        loadingVisibility.set(false);
        itemsVisibility.set(true);
    }

    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                initItemsView(teamsResponse);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            itemsVisibility.set(false);

            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                status.set(error.getStatus());
                errorVisibility.set(true);
            } else {
                if (error instanceof SingleMessageError) {
                    String message = ((SingleMessageError) error).getMessage();
//                    if (message.equals("Nie ma już więcej wyników")) {
//                        isLastPage = true;
//                        newsAdapter.isLoading = false;
//                        postRunnable.set(placeHolderAttachRunnable);
//                    }
//                    if (message.equals("Brak wyników")) {
//                        itemsVisibility.set(false);
//                        loadingVisibility.set(false);
//                        errorVisibility.set(false);
//                        placeholderVisibility.set(true);
//                    }
                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };

    public void load() {
        errorVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        TeamsFromTagsRequest request = new TeamsFromTagsRequest(tags);
        Connection.get().findByTags(callback, token, request);
    }
}
