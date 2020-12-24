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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {


    @POST("auth/login")
    Observable<LoginResponse> login(
            @Body LoginRequest loginRequest
    );

    @GET("auth/isLoggedIn")
    Observable<BaseResponse> isLoggedIn(
            @Header("Authorization") String token
    );

    @PUT("auth/logout")
    Observable<BaseResponse> logout(
            @Header("Authorization") String token
    );

    @POST("auth/sendResetPassToken/{email}")
    Observable<BaseResponse> sendResetPassTokenMail(
            @Path("email") String email
    );

    @POST("auth/resetPassword")
    Observable<BaseResponse> resetToken(
            @Body ResetPasswordRequest resetPasswordRequest
    );

    @POST("auth/register")
    Observable<BaseResponse> register(
            @Body RegisterRequest registerRequest
    );


    @GET("teams/hot")
    Observable<ProposedTeamsResponse> proposedTeams(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    @GET("sites")
    Observable<ProposedSitesResponse> proposedSites(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    @PUT("users/me")
    Observable<ProposedUserResponse> proposedUserResponse(
            @Header("Authorization") String token,
            @Body UserSettingsRequest request
    );

    @GET("news")
    Observable<NewsResponse> getNewsByPage(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    @GET("news/team={tid}")
    Observable<NewsResponse> getNewsForTeam(
            @Header("Authorization") String token,
            @Path("tid") Long teamId,
            @Query("page") int page
    );

    @GET("news/all")
    Observable<AllNewsResponse> getAllNews(
            @Header("Authorization") String token,
            @Query("page") int page
    );


    @PUT("news/like/site={sid}/id={id}")
    Observable<SingleNewsResponse> toggleLikes(
            @Header("Authorization") String token,
            @Path("sid") Long siteId,
            @Path("id") Long newsId
    );

    @PUT("news/dislike/site={sid}/id={id}")
    Observable<SingleNewsResponse> toggleDislikes(
            @Header("Authorization") String token,
            @Path("sid") Long siteId,
            @Path("id") Long newsId
    );

    @POST("teams/findByTags")
    Observable<TeamsResponse> findByTags(
            @Header("Authorization") String token,
            @Body TeamsFromTagsRequest teamsFromTagsRequest
    );

    @GET("news/notifications")
    Observable<Long> getNotifications(
            @Header("Authorization") String token
    );

    @PUT("news/visit/site={sid}/id={id}")
    Observable<SingleNewsResponse> setNewsVisited(
            @Header("Authorization") String token,
            @Path("sid") Long siteId,
            @Path("id") Long newsId
    );

    @GET("news/notVisitedNewsAmount")
    Observable<BadgesResponse> getNotVisitedNewsAmount(
            @Header("Authorization") String token
    );

    @GET("news/markAllAsVisited")
    Observable<BaseResponse> markAllAsVisited(
        @Header("Authorization") String token
    );

    @GET("news/query={query}")
    Observable<SearchResponse> getQueryResults(
            @Header("Authorization") String token,
            @Path("query") String query
    );

    @GET("sites")
    Observable<SitesResponse> getSites(
        @Header("Authorization") String token,
        @Query("page") int page
    );
}
