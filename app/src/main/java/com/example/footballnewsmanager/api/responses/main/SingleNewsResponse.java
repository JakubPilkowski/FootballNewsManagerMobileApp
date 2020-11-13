package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.UserNews;

public class SingleNewsResponse extends BaseResponse {

    private UserNews news;

    public UserNews getNews() {
        return news;
    }
}
