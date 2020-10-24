package com.example.footballnewsmanager.api;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Connection {


    private static Connection INSTANCE;


    protected Client client;

    public Connection() {
        client = new Client();
    }

    public static void init(){
        INSTANCE = new Connection();
    }

    public static Connection get() {
        return INSTANCE;
    }

    public void login(Callback<LoginResponse> callback, LoginRequest loginRequest){
        Observable<LoginResponse> loginResponseObservable = client.getService().login(loginRequest)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        loginResponseObservable.subscribe(callback);
    }

    public void isLoggedIn(Callback<BaseResponse> callback, String token){
        Observable<BaseResponse> baseResponseObservable = client.getService().isLoggedIn(token)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation());
        baseResponseObservable.subscribe(callback);
    }
}
