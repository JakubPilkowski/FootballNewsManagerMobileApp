package pl.android.footballnewsmanager.api.responses.auth;

import pl.android.footballnewsmanager.api.responses.BaseResponse;

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
