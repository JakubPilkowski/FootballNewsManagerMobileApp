package com.example.footballnewsmanager.api;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class Client {

    private static final String FOOTBALL_NEWS_MANAGER_URL = "http://footballnewsmanager.ddns.net:25080/api/";

    private Gson gson;
    private Service service;


    Client() {
//        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FOOTBALL_NEWS_MANAGER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        service = retrofit.create(Service.class);
    }


    Service getService() {
        return service;
    }
}
