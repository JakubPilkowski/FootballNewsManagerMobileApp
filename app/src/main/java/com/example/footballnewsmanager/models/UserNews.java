package com.example.footballnewsmanager.models;

public class UserNews {
    private News news;
    private boolean liked;
    private boolean disliked;
    private boolean visited;

    public News getNews() {
        return news;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isDisliked() {
        return disliked;
    }

    public boolean isVisited() {
        return visited;
    }
}
