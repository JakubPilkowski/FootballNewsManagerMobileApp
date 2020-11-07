package com.example.footballnewsmanager.fragments.main.profile;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.UserPreferences;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProfileFragmentViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel

    public void init(){

    }

    public void onClick() {

        ProgressDialog.get().show();
        String token = UserPreferences.get().getAuthToken();
        Connection.get().logout(logoutCallback, token);
    }

    private Callback<BaseResponse> logoutCallback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            ProgressDialog.get().dismiss();
            UserPreferences.get().clearAuthToken();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            ProgressDialog.get().dismiss();
            Log.d("MainActivity", ((SingleMessageError) error).getMessage());
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };
}
