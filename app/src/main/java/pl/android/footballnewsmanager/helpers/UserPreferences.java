package pl.android.footballnewsmanager.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import pl.android.footballnewsmanager.api.responses.auth.LoginResponse;

public class UserPreferences {

    private static final String TAG = "UserPreferences";
    private static final String FILENAME = TAG + "_FootballNewsManager";
    private static final String AUTH_TOKEN = FILENAME + "_AuthToken";
    private static final String NOTIFICATION_AMOUNT = FILENAME + "_NotificationsAmount";
    private static final String LANGUAGE = FILENAME + "_Language";
    private static final String PROPOSED = FILENAME + "_Proposed";

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

    public void addToken(LoginResponse loginResponse) {
        sharedPreferences.edit().putString(AUTH_TOKEN, loginResponse.getTokenType() + " " + loginResponse.getAccessToken()).apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN, null);
    }

    public void clearAuthToken() {
        sharedPreferences.edit().putString(AUTH_TOKEN, null).apply();
    }

    public void changeNotification(Long amount) {
        sharedPreferences.edit().putLong(NOTIFICATION_AMOUNT, amount).apply();
    }

    public Long getNotificationAmount() {
        return sharedPreferences.getLong(NOTIFICATION_AMOUNT, 0);
    }

    public boolean hasUser() {
        return getAuthToken() != null && !getAuthToken().isEmpty();
    }

    public void setLanguage(String locale) {
        sharedPreferences.edit().putString(LANGUAGE, locale).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(LANGUAGE, "pl");
    }

    public void setProposed(boolean proposed) {
        sharedPreferences.edit().putBoolean(PROPOSED, proposed).apply();
    }

    public boolean getProposed() {
        return sharedPreferences.getBoolean(PROPOSED, true);
    }
}