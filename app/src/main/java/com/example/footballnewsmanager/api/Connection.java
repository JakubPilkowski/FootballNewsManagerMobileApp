package com.example.footballnewsmanager.api;

import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.requests.auth.RegisterRequest;
import com.example.footballnewsmanager.api.requests.auth.ResetPasswordRequest;
import com.example.footballnewsmanager.api.requests.news.TeamsFromTagsRequest;
import com.example.footballnewsmanager.api.requests.proposed.UserSettingsRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;
import com.example.footballnewsmanager.api.responses.main.AllNewsResponse;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.api.responses.news.BadgesResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedSitesResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedUserResponse;
import com.example.footballnewsmanager.api.responses.search.SearchResponse;
import com.example.footballnewsmanager.api.responses.sites.SitesResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Connection {


    private static Connection INSTANCE;


    protected Client client;

    public Connection() {
        client = new Client();
    }

    public static void init() {
        INSTANCE = new Connection();
    }

    public static Connection get() {
        return INSTANCE;
    }

    public void login(Callback<LoginResponse> callback, LoginRequest loginRequest) {
        Observable<LoginResponse> loginResponseObservable = client.getService().login(loginRequest)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        loginResponseObservable.subscribe(callback);
    }

    public void isLoggedIn(Callback<BaseResponse> callback, String token) {
        Observable<BaseResponse> baseResponseObservable = client.getService().isLoggedIn(token)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        baseResponseObservable.subscribe(callback);
    }

    public void logout(Callback<BaseResponse> callback, String token) {
        Observable<BaseResponse> baseResponseObservable = client.getService().logout(token)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        baseResponseObservable.subscribe(callback);
    }

    public void sendResetPasswordMail(Callback<BaseResponse> callback, String email) {
        Observable<BaseResponse> baseResponseObservable = client.getService().sendResetPassTokenMail(email)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        baseResponseObservable.subscribe(callback);
    }

    public void resetPassword(Callback<BaseResponse> callback, ResetPasswordRequest resetPasswordRequest) {
        Observable<BaseResponse> baseResponseObservable = client.getService().resetToken(resetPasswordRequest)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        baseResponseObservable.subscribe(callback);
    }

    public void register(Callback<BaseResponse> callback, RegisterRequest registerRequest) {
        Observable<BaseResponse> registerObservable = client.getService().register(registerRequest)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        registerObservable.subscribe(callback);
    }

    public void proposedTeams(Callback<ProposedTeamsResponse> callback, String token, int page) {
        Observable<ProposedTeamsResponse> proposedTeamsObservable = client.getService().proposedTeams(token, page)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        proposedTeamsObservable.subscribe(callback);
    }

    public void proposedSites(Callback<ProposedSitesResponse> callback, String token, int page) {
        Observable<ProposedSitesResponse> proposedSitesObservable = client.getService().proposedSites(token, page)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        proposedSitesObservable.subscribe(callback);
    }

    public void userSettingsResponse(Callback<ProposedUserResponse> callback, String token, UserSettingsRequest userSettingsRequest) {
        Observable<ProposedUserResponse> proposedUserObservable = client.getService()
                .proposedUserResponse(token, userSettingsRequest).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        proposedUserObservable.subscribe(callback);
    }

    public void news(Callback<NewsResponse> callback, String token, int page) {
        Observable<NewsResponse> newsObservable = client.getService()
                .getNewsByPage(token, page).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        newsObservable.subscribe(callback);
    }

    public void toggleLikes(Callback<SingleNewsResponse> callback, String token, Long siteId, Long newsId) {
        Observable<SingleNewsResponse> toggleLikeObservable = client.getService()
                .toggleLikes(token, siteId, newsId).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        toggleLikeObservable.subscribe(callback);
    }

    public void toggleDisLikes(Callback<SingleNewsResponse> callback, String token, Long siteId, Long newsId) {
        Observable<SingleNewsResponse> toggleDislikeObservable = client.getService()
                .toggleDislikes(token, siteId, newsId).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        toggleDislikeObservable.subscribe(callback);
    }

    public void findByTags(Callback<TeamsResponse> callback, String token, TeamsFromTagsRequest request) {
        Observable<TeamsResponse> teamsObservable = client.getService()
                .findByTags(token, request).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        teamsObservable.subscribe(callback);
    }

    public void getNotifications(Callback<Long> callback, String token) {
        Observable<Long> notificationResponseObservable = client.getService()
                .getNotifications(token).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        notificationResponseObservable.subscribe(callback);
    }

    public void setNewsVisited(Callback<SingleNewsResponse> callback, String token, Long siteId, Long newsId) {
        Observable<SingleNewsResponse> notificationResponseObservable = client.getService()
                .setNewsVisited(token, siteId, newsId).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        notificationResponseObservable.subscribe(callback);
    }

    public void getNotVisitedNewsAmount(Callback<BadgesResponse> callback, String token) {
        Observable<BadgesResponse> badgesResponseObservable = client.getService()
                .getNotVisitedNewsAmount(token).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        badgesResponseObservable.subscribe(callback);
    }

    public void markAllAsVisited(Callback<BaseResponse> callback, String token) {
        Observable<BaseResponse> markAllObservable = client.getService()
                .markAllAsVisited(token).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        markAllObservable.subscribe(callback);
    }

    public void allNews(Callback<AllNewsResponse> callback, String token, int page) {
        Observable<AllNewsResponse> newsObservable = client.getService()
                .getAllNews(token, page).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        newsObservable.subscribe(callback);
    }


    public void newsForTeam(Callback<NewsResponse> callback, String token, Long teamId, int page){
        Observable<NewsResponse> newsForTeamObservable = client.getService()
                .getNewsForTeam(token, teamId, page).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        newsForTeamObservable.subscribe(callback);
    }


    public void getQueryResults(Callback<SearchResponse> callback, String token, String query) {
        Observable<SearchResponse> searchObservable = client.getService()
                .getQueryResults(token, query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        searchObservable
                .subscribe(callback);
    }

    public void getSites(Callback<SitesResponse> callback, String token, int page){
        Observable<SitesResponse> sitesObservable = client.getService()
                .getSites(token, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        sitesObservable
                .subscribe(callback);
    }
}
