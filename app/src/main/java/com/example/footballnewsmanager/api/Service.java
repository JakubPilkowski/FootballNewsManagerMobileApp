package com.example.footballnewsmanager.api;

import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Service {


    @POST("auth/login")
    Observable<LoginResponse> login(
            @Body LoginRequest loginRequest
            );

}
