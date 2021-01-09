package pl.android.footballnewsmanager.models;

public class UserNews {
    private News news;
    private boolean liked;
    private boolean inFavourites;
    private boolean visited;
    private boolean badged;

    public News getNews() {
        return news;
    }

    public boolean isLiked() {
        return liked;
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
