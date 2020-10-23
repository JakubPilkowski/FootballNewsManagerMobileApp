package com.example.footballnewsmanager.api;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;

public abstract class Callback<T> extends Subject<T> {
    public abstract void onSuccess(T t);

    public abstract void onSmthWrong(Throwable message);

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }
    @Override
    public void onError(@NonNull Throwable e) {
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
