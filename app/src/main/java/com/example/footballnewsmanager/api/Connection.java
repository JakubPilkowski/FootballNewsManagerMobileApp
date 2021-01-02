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
import com.example.footballnewsmanager.api.responses.manage_teams.LeagueResponse;
import com.example.footballnewsmanager.api.responses.news.BadgesResponse;
import com.example.footballnewsmanager.api.responses.profile.UserProfileResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedSitesResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;
import com.example.footballnewsmanager.api.responses.proposed.TeamsResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedUserResponse;
import com.example.footballnewsmanager.api.responses.search.SearchResponse;
import com.example.footballnewsmanager.api.responses.sites.SitesResponse;
import com.example.footballnewsmanager.models.Language;
import com.example.footballnewsmanager.models.UserTeam;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        loginResponseObservable.subscribe(callback);
    }

    public void isLoggedIn(Callback<BaseResponse> callback, String token) {
        Observable<BaseResponse> baseResponseObservable = client.getService().isLoggedIn(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);

        baseResponseObservable.subscribe(callback);
    }

    public void logout(Callback<BaseResponse> callback, String token) {
        Observable<BaseResponse> baseResponseObservable = client.getService().logout(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        baseResponseObservable.subscribe(callback);
    }

    public void sendResetPasswordMail(Callback<BaseResponse> callback, String email) {
        Observable<BaseResponse> baseResponseObservable = client.getService().sendResetPassTokenMail(email)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        baseResponseObservable.subscribe(callback);
    }

    public void resetPassword(Callback<BaseResponse> callback, ResetPasswordRequest resetPasswordRequest) {
        Observable<BaseResponse> baseResponseObservable = client.getService().resetToken(resetPasswordRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        baseResponseObservable.subscribe(callback);
    }

    public void register(Callback<BaseResponse> callback, RegisterRequest registerRequest) {
        Observable<BaseResponse> registerObservable = client.getService().register(registerRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        registerObservable.subscribe(callback);
    }

    public void proposedTeams(Callback<ProposedTeamsResponse> callback, String token, int page) {
        Observable<ProposedTeamsResponse> proposedTeamsObservable = client.getService().proposedTeams(token, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        proposedTeamsObservable.subscribe(callback);
    }

    public void proposedSites(Callback<ProposedSitesResponse> callback, String token, int page) {
        Observable<ProposedSitesResponse> proposedSitesObservable = client.getService().proposedSites(token, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        proposedSitesObservable.subscribe(callback);
    }

    public void userSettingsResponse(Callback<ProposedUserResponse> callback, String token, UserSettingsRequest userSettingsRequest) {
        Observable<ProposedUserResponse> proposedUserObservable = client.getService()
                .proposedUserResponse(token, userSettingsRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        proposedUserObservable.subscribe(callback);
    }

    public void news(Callback<NewsResponse> callback, String token, int page) {
        Observable<NewsResponse> newsObservable = client.getService()
                .getNewsByPage(token, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        newsObservable.subscribe(callback);
    }

    public void toggleLikes(Callback<SingleNewsResponse> callback, String token, Long siteId, Long newsId) {
        Observable<SingleNewsResponse> toggleLikeObservable = client.getService()
                .toggleLikes(token, siteId, newsId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        toggleLikeObservable.subscribe(callback);
    }


    public void findByTags(Callback<TeamsResponse> callback, String token, TeamsFromTagsRequest request) {
        Observable<TeamsResponse> teamsObservable = client.getService()
                .findByTags(token, request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        teamsObservable.subscribe(callback);
    }

    public void getNotifications(Callback<Long> callback, String token) {
        Observable<Long> notificationResponseObservable = client.getService()
                .getNotifications(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        notificationResponseObservable.subscribe(callback);
    }

    public void setNewsVisited(Callback<SingleNewsResponse> callback, String token, Long siteId, Long newsId) {
        Observable<SingleNewsResponse> notificationResponseObservable = client.getService()
                .setNewsVisited(token, siteId, newsId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        notificationResponseObservable.subscribe(callback);
    }

    public void getNotVisitedNewsAmount(Callback<BadgesResponse> callback, String token) {
        Observable<BadgesResponse> badgesResponseObservable = client.getService()
                .getNotVisitedNewsAmount(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        badgesResponseObservable.subscribe(callback);
    }

    public void markAllAsVisited(Callback<BaseResponse> callback, String token) {
        Observable<BaseResponse> markAllObservable = client.getService()
                .markAllAsVisited(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        markAllObservable.subscribe(callback);
    }

    public void allNews(Callback<AllNewsResponse> callback, String token, int page) {
        Observable<AllNewsResponse> newsObservable = client.getService()
                .getAllNews(token, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        newsObservable.subscribe(callback);
    }


    public void newsForTeam(Callback<NewsResponse> callback, String token, Long teamId, int page) {
        Observable<NewsResponse> newsForTeamObservable = client.getService()
                .getNewsForTeam(token, teamId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        newsForTeamObservable.subscribe(callback);
    }


    public void getQueryResults(Callback<SearchResponse> callback, String token, String query) {
        Observable<SearchResponse> searchObservable = client.getService()
                .getQueryResults(token, query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        searchObservable
                .subscribe(callback);
    }

    public void getSites(Callback<SitesResponse> callback, String token, int page) {
        Observable<SitesResponse> sitesObservable = client.getService()
                .getSites(token, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        sitesObservable
                .subscribe(callback);
    }

    public void getUserProfile(Callback<UserProfileResponse> callback, String token) {
        Observable<UserProfileResponse> userProfileObservable = client.getService()
                .getUserProfile(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        userProfileObservable.subscribe(callback);
    }

    public void deleteAccount(Callback<BaseResponse> callback, String token) {
        Observable<BaseResponse> accountObservable = client.getService()
                .deleteAccount(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        accountObservable.subscribe(callback);
    }

    public void getLeagues(Callback<LeagueResponse> callback, String token) {
        Observable<LeagueResponse> leagueObservable = client.getService()
                .getLeagues(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        leagueObservable.subscribe(callback);
    }

    public void getTeamsFromLeague(Callback<TeamsResponse> callback, String token, Long leagueId, int page) {
        Observable<TeamsResponse> teamsFromLeagueObservable = client.getService()
                .getTeamsFromLeague(token, leagueId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        teamsFromLeagueObservable.subscribe(callback);
    }

    public void getFavouriteTeams(Callback<TeamsResponse> callback, String token) {
        Observable<TeamsResponse> favouriteTeamsObservable = client.getService()
                .getFavouriteTeams(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        favouriteTeamsObservable.subscribe(callback);
    }

    public void toggleTeam(Callback<UserTeam> callback, String token, Long id) {
        Observable<UserTeam> toggleTeamObservable = client.getService()
                .toggleTeam(token, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        toggleTeamObservable.subscribe(callback);
    }

    public void getLikedNews(Callback<NewsResponse> callback, String token, int page) {
        Observable<NewsResponse> likedNewsObservable = client.getService()
                .getLikedNews(token, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .timeout(15 * 1000, TimeUnit.MILLISECONDS);
        likedNewsObservable.subscribe(callback);
    }


}

