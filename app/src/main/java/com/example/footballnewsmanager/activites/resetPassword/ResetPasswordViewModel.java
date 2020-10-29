package com.example.footballnewsmanager.activites.resetPassword;

import android.net.Uri;

import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.fragments.auth.resetPassword.ResetPasswordFragment;

public class ResetPasswordViewModel extends BaseViewModel {
    String type;
    public void init(Uri data) {
        String token="";
        if(data != null && data.isHierarchical()){
            if(data.getQueryParameter("token")!=null){
                token = data.getQueryParameter("token");
                type = "browser";
            }
        }else {
            type = "application";
        }

        getNavigator().attach(ResetPasswordFragment.newInstance(token, type), ResetPasswordFragment.TAG);
    }
}
