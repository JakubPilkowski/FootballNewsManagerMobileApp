package pl.android.footballnewsmanager.api;

import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.errors.MultipleMessageError;
import pl.android.footballnewsmanager.api.errors.SingleMessageError;
import com.google.gson.Gson;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;
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
            if(throwable.getMessage() != null && throwable.getMessage().contains("Unable to resolve host ")){
                SingleMessageError singleMessageError = new SingleMessageError();
                singleMessageError.setMessage("No network");
                singleMessageError.setStatus(598);
                onSmthWrong(singleMessageError);
            }
            else if(throwable.getMessage()!=null && throwable.getMessage().contains("The source did not signal an event for")){
                SingleMessageError singleMessageError = new SingleMessageError();
                singleMessageError.setMessage("Request timeout");
                singleMessageError.setStatus(408);
                onSmthWrong(singleMessageError);
            }
        }
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
