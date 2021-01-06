package com.example.footballnewsmanager.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.activites.error.ErrorActivity;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.helpers.ProgressDialog;
import com.example.footballnewsmanager.helpers.UserPreferences;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class SplashActivity extends AppCompatActivity {

    public static final int REQUEST_SPLASH_ERROR = 6001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loggingValidation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressDialog.init(this);
        Connection.init();
        UserPreferences.init(this);
    }

    public void loggingValidation(){
        new Handler().postDelayed(() -> {
            ProgressDialog.get().show();
            if (UserPreferences.get().hasUser()) {
                String token = UserPreferences.get().getAuthToken();
                Connection.get().isLoggedIn(isLoggedInCallback, token);
            } else {
                ProgressDialog.get().dismiss();
                Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    private Callback<BaseResponse> isLoggedInCallback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            ProgressDialog.get().dismiss();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            ProgressDialog.get().dismiss();
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                Intent intent = new Intent(SplashActivity.this, ErrorActivity.class);
                intent.putExtra("status", error.getStatus());
                intent.putExtra("requestCode", REQUEST_SPLASH_ERROR);
                startActivityForResult(intent, REQUEST_SPLASH_ERROR);
            } else {
                Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SPLASH_ERROR && resultCode == RESULT_OK){
            loggingValidation();
        }
    }
}
