package com.example.footballnewsmanager.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    static final String LOCALHOST_URL = "";
    static final String FOOTBALL_NEWS_MANAGER_URL = "";

    protected Gson gson;
    private Service service;


    public Client() {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LOCALHOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        service = retrofit.create(Service.class);
    }

    public Service getService() {
        return service;
    }
}
