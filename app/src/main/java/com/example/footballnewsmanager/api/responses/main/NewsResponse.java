package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.News;

import java.util.List;

public class NewsResponse extends BaseResponse {

    private List<News> news;

    public List<News> getNews() {
        return news;
    }
}
