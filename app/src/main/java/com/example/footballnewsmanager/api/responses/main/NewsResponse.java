package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.UserNews;

import java.util.List;

public class NewsResponse extends BaseResponse {

    private int pages;
    private Long newsCount;
    private Long newsToday;
    private List<UserNews> allNews;
    private BaseNewsAdjustment additionalContent;

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

    public BaseNewsAdjustment getAdditionalContent() {
        return additionalContent;
    }
}
