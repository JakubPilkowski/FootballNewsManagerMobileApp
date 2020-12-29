package com.example.footballnewsmanager.models;

import java.util.List;

public class User {
    private String username;

    private String addedDate;

    private Language language;

    private boolean proposedNews;

    public String getUsername() {
        return username;
    }

    public Language getLanguage() {
        return language;
    }

    public boolean isProposedNews() {
        return proposedNews;
    }

    public String getAddedDate() {
        return addedDate;
    }
}
