package com.example.footballnewsmanager.api;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.requests.auth.RegisterRequest;
import com.example.footballnewsmanager.api.requests.auth.ResetPasswordRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsResponse;

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
            @Query("count") int count
    );
}
