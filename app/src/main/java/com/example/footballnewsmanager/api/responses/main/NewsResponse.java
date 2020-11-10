package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.UserNews;

import java.util.List;

public class NewsResponse<T extends BaseNewsAdjustment> extends BaseResponse {

    private int pages;
    private Long newsCount;
    private Long newsToday;
    private List<UserNews> allNews;
//    private T additionalContent;

    public List<UserNews> getAllNews() {
        return allNews;
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

//    public T getAdditionalContent() {
//        return additionalContent;
//    }
}
