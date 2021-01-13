package pl.android.footballnewsmanager.fragments.main.news_info;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.adapters.news_info.NewsInfoAdapter;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.requests.news.TeamsFromTagsRequest;
import pl.android.footballnewsmanager.api.responses.proposed.TeamsResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.models.UserNews;

public class NewsInfoFragmentViewModel extends BaseViewModel {


    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);

    private UserNews news;
    private NewsInfoAdapter newsInfoAdapter;
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    public void init(UserNews news) {
        if (news != null) {
            this.news = news;
            tryAgainListener.set(listener);
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
        TeamsFromTagsRequest request = new TeamsFromTagsRequest(news.getNews().getTeamNews());
        Connection.get().findByTags(callback, token, request);
    }
}
