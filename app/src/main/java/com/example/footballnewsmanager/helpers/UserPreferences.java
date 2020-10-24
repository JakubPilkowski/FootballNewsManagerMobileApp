package com.example.footballnewsmanager.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.footballnewsmanager.api.responses.auth.LoginResponse;
import com.google.gson.reflect.TypeToken;

public class UserPreferences {

    private static final String TAG = "UserPreferences";
    private static final String FILENAME = TAG + "_FootballNewsManager";
    private static final String AUTH_TOKEN = FILENAME + "_AuthToken";

    private static UserPreferences INSTANCE;
    private SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        INSTANCE = new UserPreferences(context);
    }

    public static UserPreferences get() {
        return INSTANCE;
    }

    public void save(LoginResponse loginResponse) {
        sharedPreferences.edit().putString(AUTH_TOKEN, loginResponse.getTokenType() + " " + loginResponse.getAccessToken()).apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN, null);
    }

    public void clearAuthToken() {
        sharedPreferences.edit().putString(AUTH_TOKEN, null).apply();
    }

    public boolean hasUser() {
        return getAuthToken() != null && !getAuthToken().isEmpty();
    }

}
