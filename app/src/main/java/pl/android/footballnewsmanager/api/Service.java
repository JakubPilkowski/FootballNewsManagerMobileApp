package pl.android.footballnewsmanager.api;

import pl.android.footballnewsmanager.api.requests.auth.ChangePasswordRequest;
import pl.android.footballnewsmanager.api.requests.auth.LoginRequest;
import pl.android.footballnewsmanager.api.requests.auth.RegisterRequest;
import pl.android.footballnewsmanager.api.requests.auth.ResetPasswordRequest;
import pl.android.footballnewsmanager.api.requests.news.TeamsFromTagsRequest;
import pl.android.footballnewsmanager.api.requests.proposed.UserSettingsRequest;
import pl.android.footballnewsmanager.api.responses.BaseResponse;
import pl.android.footballnewsmanager.api.responses.auth.LoginResponse;
import pl.android.footballnewsmanager.api.responses.main.AllNewsResponse;
import pl.android.footballnewsmanager.api.responses.main.NewsResponse;
import pl.android.footballnewsmanager.api.responses.main.SingleNewsResponse;
import pl.android.footballnewsmanager.api.responses.manage_teams.LeagueResponse;
import pl.android.footballnewsmanager.api.responses.profile.UserProfileResponse;
import pl.android.footballnewsmanager.api.responses.proposed.TeamsResponse;
import pl.android.footballnewsmanager.api.responses.search.SearchResponse;
import pl.android.footballnewsmanager.api.responses.sites.SitesResponse;
import pl.android.footballnewsmanager.models.User;
import pl.android.footballnewsmanager.models.UserTeam;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST("users/me/password")
    Observable<BaseResponse> changePassword(
            @Header("Authorization") String token,
            @Body ChangePasswordRequest changePasswordRequest
    );

    @POST("auth/register")
    Observable<BaseResponse> register(
            @Body RegisterRequest registerRequest
    );

    @GET("teams/hot")
    Observable<TeamsResponse> proposedTeams(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    @GET("sites")
    Observable<SitesResponse> proposedSites(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    @PUT("users/me")
    Observable<User> proposedUserResponse(
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
            @Query("page") int page,
            @Query("proposed") boolean proposed
    );


    @PUT("news/like/site={sid}/id={id}")
    Observable<SingleNewsResponse> toggleLikes(
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

    @PUT("news/badge/site={sid}/id={id}")
    Observable<SingleNewsResponse> setNewsBadged(
            @Header("Authorization") String token,
            @Path("sid") Long siteId,
            @Path("id") Long newsId
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

    @GET("users/me")
    Observable<UserProfileResponse> getUserProfile(
            @Header("Authorization") String token
    );

    @DELETE("users/me")
    Observable<BaseResponse> deleteAccount(
            @Header("Authorization") String token
    );

    @GET("leagues")
    Observable<LeagueResponse> getLeagues(
            @Header("Authorization") String token
    );

    @GET("teams/league={lid}")
    Observable<TeamsResponse> getTeamsFromLeague(
            @Header("Authorization") String token,
            @Path("lid") Long leagueId,
            @Query("page") int page
    );

    @GET("teams/favouriteTeams")
    Observable<TeamsResponse> getFavouriteTeams(
            @Header("Authorization") String token
    );

    @PUT("users/me/toggleTeam/{tid}")
    Observable<UserTeam> toggleTeam(
            @Header("Authorization") String token,
            @Path("tid") Long teamId
    );

    @GET("news/liked")
    Observable<NewsResponse> getLikedNews(
            @Header("Authorization") String token,
            @Query("page") int page
    );
}
