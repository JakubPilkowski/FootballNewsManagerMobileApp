package com.example.footballnewsmanager.api;

import android.util.Log;

import java.io.IOException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public abstract class Callback<T> extends Subject<T> {
    public abstract void onSuccess(T t);

    public abstract void onSmthWrong(Throwable message);

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }
    @Override
    public void onError(@NonNull Throwable e) {
//        Log.d("error", String.valueOf(((HttpException) e).code()));
//        if (NetworkUtil.isHttpStatusCode(e, 400) || NetworkUtil.isHttpStatusCode(e, 400)) {
//            ResponseBody body = ((HttpException) e).response().errorBody();
//            try {
//                Log.d("error body", "onError: " + body.string());
////                getMvpView().onError(body.string());
//            } catch (IOException e1) {
//                e1.printStackTrace();
////                Timber.e(e1.getMessage());
//            } finally {
//                if (body != null) {
//                    body.close();
//                }
//            }
//        }

        onSmthWrong(e);
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
