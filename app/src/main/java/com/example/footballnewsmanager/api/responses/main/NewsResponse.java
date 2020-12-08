package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.UserNews;

import java.util.List;

public class NewsResponse{

    private List<UserNews> news;
    private Long newsCount;
    private Long newsToday;
    private int pages;

    public List<UserNews> getNews() {
        return news;
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
