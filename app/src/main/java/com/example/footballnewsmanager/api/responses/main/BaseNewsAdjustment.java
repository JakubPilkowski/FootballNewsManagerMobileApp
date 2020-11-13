package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.Team;

import java.util.List;

public class BaseNewsAdjustment {
    private String title;
    private NewsInfoType type;
    private News news;
    private List<Team> teams;


    public BaseNewsAdjustment(String title, NewsInfoType type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public NewsInfoType getType() {
        return type;
    }

    public News getNews() {
        return news;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
