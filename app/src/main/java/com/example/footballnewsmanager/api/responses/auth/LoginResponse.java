package com.example.footballnewsmanager.api.responses.auth;

import com.example.footballnewsmanager.api.responses.BaseResponse;

public class LoginResponse extends BaseResponse {

    private String accessToken;
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
