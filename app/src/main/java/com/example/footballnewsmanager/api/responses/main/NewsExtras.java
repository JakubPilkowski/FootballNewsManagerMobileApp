package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.models.News;

public class NewsExtras extends BaseNewsAdjustment {
    private News news;

    public NewsExtras(String title, NewsInfoType type, News news) {
        super(title, type);
        this.news = news;
    }

    public News getNews() {
        return news;
    }

}
