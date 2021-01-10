package pl.android.footballnewsmanager.api.responses.main;

import pl.android.footballnewsmanager.models.UserNews;

import java.util.List;

public class NewsResponse {

    private List<UserNews> userNews;
    private Long newsCount;
    private Long newsToday;
    private int pages;

    public List<UserNews> getUserNews() {
        return userNews;
    }

    public int getPages() {
        return pages;
    }

    public Long getNewsCount() {
        return newsCount;
    }

    public Long getNewsToday() {
        return newsToday;
    }

}
