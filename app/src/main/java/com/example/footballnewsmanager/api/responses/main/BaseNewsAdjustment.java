package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.List;

public class BaseNewsAdjustment {
    private String title;
    private NewsInfoType type;
    private News news;
    private List<UserTeam> teams;


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

    public List<UserTeam> getTeams() {
        return teams;
    }
}
