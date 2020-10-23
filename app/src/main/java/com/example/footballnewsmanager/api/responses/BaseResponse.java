package com.example.footballnewsmanager.api.responses;

public class BaseResponse {
    public boolean success;
    public String message;


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
