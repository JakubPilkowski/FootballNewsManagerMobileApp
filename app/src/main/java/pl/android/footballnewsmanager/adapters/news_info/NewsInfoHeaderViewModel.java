package pl.android.footballnewsmanager.adapters.news_info;

import androidx.databinding.ObservableField;

import pl.android.footballnewsmanager.models.UserNews;

public class NewsInfoHeaderViewModel {
    public ObservableField<String> newsTitle = new ObservableField<>();
    public ObservableField<String> siteTitle = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> likes = new ObservableField<>();
    public ObservableField<String> clicks = new ObservableField<>();

    public void init(UserNews userNews){
        newsTitle.set(userNews.getNews().getTitle());
        siteTitle.set(userNews.getNews().getSite().getName());
        String dateFormatted = userNews.getNews().getDate().replace("T"," ");
        date.set(dateFormatted);
        likes.set(String.valueOf(userNews.getNews().getLikes()));
        clicks.set(String.valueOf(userNews.getNews().getClicks()));
    }
}
