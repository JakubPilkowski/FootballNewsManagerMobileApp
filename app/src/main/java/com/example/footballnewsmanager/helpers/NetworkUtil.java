package com.example.footballnewsmanager.helpers;

import retrofit2.HttpException;

public class NetworkUtil {
    public static boolean isHttpStatusCode(Throwable throwable, int statusCode) {
        return throwable instanceof HttpException
                && ((HttpException) throwable).code() == statusCode;
    }
}
