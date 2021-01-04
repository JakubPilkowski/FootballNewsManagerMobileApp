package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.SingleNewsType;
import com.example.footballnewsmanager.models.UserNews;

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
