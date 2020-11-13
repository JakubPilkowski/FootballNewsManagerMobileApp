package com.example.footballnewsmanager.models;

public class UserNews {
    private News news;
    private boolean liked;
    private boolean disliked;

    public News getNews() {
        return news;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isDisliked() {
        return disliked;
    }
}
