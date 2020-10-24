package com.example.footballnewsmanager.api;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
    ObservableField<BaseResponse> logout(
            @Header("Authorization") String token
    );
}
