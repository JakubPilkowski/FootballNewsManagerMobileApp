package com.example.footballnewsmanager.models;

public class UserNews {
    private News news;
    private boolean liked;
    private boolean disliked;
    private boolean inFavourites;
    private boolean visited;
    private boolean badged;

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

    public boolean isBadged() {
        return badged;
    }

    public boolean isInFavourites() {
        return inFavourites;
    }
}
