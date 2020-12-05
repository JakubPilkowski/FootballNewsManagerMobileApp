package com.example.footballnewsmanager.api;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.requests.auth.RegisterRequest;
import com.example.footballnewsmanager.api.requests.auth.ResetPasswordRequest;
import com.example.footballnewsmanager.api.requests.news.TeamsFromTagsRequest;
import com.example.footballnewsmanager.api.requests.proposed.UserSettingsRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.api.responses.main.SingleNewsResponse;
import com.example.footballnewsmanager.api.responses.news.NotificationResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedSitesResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsAndSitesResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedUserResponse;
import com.example.footballnewsmanager.models.Tag;
import com.example.footballnewsmanager.models.User;
import com.example.footballnewsmanager.models.UserNews;

import java.util.List;

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

    public void proposedTeams(Callback<ProposedTeamsResponse> callback, String token, int count){
        Observable<ProposedTeamsResponse> proposedTeamsObservable = client.getService().proposedTeams(token, count)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        proposedTeamsObservable.subscribe(callback);
    }

    public void proposedSites(Callback<ProposedSitesResponse> callback, String token, int page){
        Observable<ProposedSitesResponse> proposedSitesObservable = client.getService().proposedSites(token, page)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        proposedSitesObservable.subscribe(callback);
    }

    public void proposedTeamsAndSites(Callback<ProposedTeamsAndSitesResponse> callback, String token, int teamsCount, int sitesPage){
        Observable<ProposedTeamsResponse> proposedTeamsObservable = client.getService().proposedTeams(token, teamsCount)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        Observable<ProposedSitesResponse> proposedSitesObservable = client.getService().proposedSites(token, sitesPage)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        Observable<ProposedTeamsAndSitesResponse> combined = Observable.zip(proposedTeamsObservable, proposedSitesObservable, ProposedTeamsAndSitesResponse::new);
        combined.subscribe(callback);
    }

    public void userSettingsResponse(Callback<ProposedUserResponse> callback, String token, UserSettingsRequest userSettingsRequest){
        Observable<ProposedUserResponse> proposedUserObservable = client.getService()
                .proposedUserResponse(token, userSettingsRequest).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        proposedUserObservable.subscribe(callback);
    }

    public void news(Callback<NewsResponse> callback, String token, int page) {
        Observable<NewsResponse> newsObservable  = client.getService()
                .getNewsByPage(token, page).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        newsObservable.subscribe(callback);
    }

    public void toggleLikes(Callback<SingleNewsResponse> callback, String token, Long siteId, Long newsId){
        Observable<SingleNewsResponse> toggleLikeObservable = client.getService()
                .toggleLikes(token, siteId, newsId).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        toggleLikeObservable.subscribe(callback);
    }

    public void toggleDisLikes(Callback<SingleNewsResponse> callback, String token, Long siteId, Long newsId){
        Observable<SingleNewsResponse> toggleDislikeObservable = client.getService()
                .toggleDislikes(token, siteId, newsId).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        toggleDislikeObservable.subscribe(callback);
    }

    public void findByTags(Callback<ProposedTeamsResponse> callback, String token, TeamsFromTagsRequest request){
        Observable<ProposedTeamsResponse> teamsObservable = client.getService()
                .findByTags(token, request).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        teamsObservable.subscribe(callback);
    }

    public void getNotifications(Callback<NotificationResponse> callback, String token){
        Observable<NotificationResponse> notificationResponseObservable = client.getService()
                .getNotifications(token).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        notificationResponseObservable.subscribe(callback);
    }

    public void setNewsVisited(Callback<UserNews> callback, String token, Long siteId, Long newsId){
        Observable<UserNews> notificationResponseObservable = client.getService()
                .setNewsVisited(token, siteId, newsId).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
        notificationResponseObservable.subscribe(callback);
    }


}
