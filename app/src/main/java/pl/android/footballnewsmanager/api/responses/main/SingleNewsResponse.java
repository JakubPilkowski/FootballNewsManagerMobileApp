package pl.android.footballnewsmanager.api.responses.main;

import pl.android.footballnewsmanager.api.responses.BaseResponse;
import pl.android.footballnewsmanager.models.SingleNewsType;
import pl.android.footballnewsmanager.models.UserNews;

public class SingleNewsResponse extends BaseResponse {

    private UserNews news;
    private SingleNewsType type;

    public UserNews getNews() {
        return news;
    }

    public SingleNewsType getType() {
        return type;
    }
}
