package com.example.footballnewsmanager.fragments.main.news_info;

import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.requests.news.TeamsFromTagsRequest;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.NewsInfoFragmentBinding;
import com.example.footballnewsmanager.databinding.NewsInfoTeamLayoutBinding;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.NewsTag;
import com.example.footballnewsmanager.models.Tag;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserNews;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsInfoFragmentViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel

    public ObservableField<String> newsTitle = new ObservableField<>();
    public ObservableField<String> siteTitle = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> likes = new ObservableField<>();
    public ObservableField<String> clicks = new ObservableField<>();

    private UserNews news;

    public void init(UserNews news) {
        this.news = news;
        newsTitle.set(news.getNews().getTitle());
        siteTitle.set(news.getNews().getSite().getName());
        String dateFormatted = news.getNews().getDate().replace("T"," ");
        date.set(dateFormatted);
        likes.set(String.valueOf(news.getNews().getLikes()));
        clicks.set(String.valueOf(news.getNews().getClicks()));
        String token = UserPreferences.get().getAuthToken();
        List<Tag> tags = new ArrayList<>();
        for (NewsTag newsTag : news.getNews().getTags()) {
            tags.add(newsTag.getTag());
        }
        TeamsFromTagsRequest request = new TeamsFromTagsRequest(tags);
        Connection.get().findByTags(callback, token, request);
    }

    private Callback<TeamsResponse> callback = new Callback<TeamsResponse>() {
        @Override
        public void onSuccess(TeamsResponse teamsResponse) {
            getActivity().runOnUiThread(() -> {
                LinearLayout layout = ((NewsInfoFragmentBinding) getBinding()).newsInfoLinearLayout;
                for (Team team : teamsResponse.getTeams()) {
                    LinearLayout cardView = (LinearLayout) LayoutInflater.from(getFragment().getContext()).inflate(R.layout.news_info_team_layout, layout, false);
                    NewsInfoTeamLayoutBinding binding = NewsInfoTeamLayoutBinding.bind(cardView);
                    NewsInfoTeamViewModel viewModel = new NewsInfoTeamViewModel();
                    binding.setViewModel(viewModel);
                    viewModel.init(team);
                    layout.addView(cardView);
                }
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super TeamsResponse> observer) {

        }
    };
}
