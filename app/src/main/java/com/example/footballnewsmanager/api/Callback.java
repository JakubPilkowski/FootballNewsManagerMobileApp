package com.example.footballnewsmanager.api;

import android.util.Log;

import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.MultipleMessageError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.fragments.auth.login.LoginFragment;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class Callback<T> extends Subject<T> {
    public abstract void onSuccess(T t);

    public abstract void onSmthWrong(BaseError error);

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }
    @Override
    public void onError(@NonNull Throwable throwable) {
        try {
            Response<?> responseBody = ((HttpException) throwable).response();
            if((responseBody != null ? responseBody.errorBody() : null) !=null){
                String error = responseBody.errorBody().string();
                Gson gson = new Gson();
                if(error.contains("messages")){
                    MultipleMessageError baseError = gson.fromJson(error, MultipleMessageError.class);
                    onSmthWrong(baseError);
                }
                else {
                    SingleMessageError baseError = gson.fromJson(error, SingleMessageError.class);
                    onSmthWrong(baseError);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

//        onSmthWrong(throwable);
    }
    @Override
    public boolean hasObservers() {
        return false;
    }

    @Override
    public boolean hasThrowable() {
        return false;
    }

    @Override
    public boolean hasComplete() {
        return false;
    }

    @Override
    public @Nullable Throwable getThrowable() {
        return null;
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onComplete() {

    }
}